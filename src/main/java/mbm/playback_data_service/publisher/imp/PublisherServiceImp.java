package mbm.playback_data_service.publisher.imp;

import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.publisher.PublisherService;
import mbm.playback_data_service.publisher.configuration.PublisherMqProperties;
import mbm.playback_data_service.publisher.mapper.PlaybackDataMessageMapper;
import mbm.playback_data_service.publisher.message.PlaybackDataMessage;
import mbm.playback_data_service.serialization.SerializationService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImp implements PublisherService {

    private final StreamBridge streamBridge;
    private final SerializationService serializationService;
    private final PublisherMqProperties publisherMqProperties;
    private final PlaybackDataMessageMapper playbackDataMessageMapper;

    public PublisherServiceImp(final StreamBridge streamBridge,
                               final SerializationService serializationService,
                               final PublisherMqProperties publisherMqProperties,
                               final PlaybackDataMessageMapper playbackDataMessageMapper) {
        this.streamBridge = streamBridge;
        this.serializationService = serializationService;
        this.publisherMqProperties = publisherMqProperties;
        this.playbackDataMessageMapper = playbackDataMessageMapper;
    }


    @Override
    public void publishPlaybackData(final PlaybackDataDto playbackDataDto) {
        final PlaybackDataMessage playbackDataMessage = playbackDataMessageMapper.map(playbackDataDto);
        final String serializedMessage = serializationService.serialize(playbackDataMessage);
        streamBridge.send(publisherMqProperties.getPlaybackDataTopicName(), serializedMessage);
    }
}
