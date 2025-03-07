package mbm.playback_data_service.publisher.imp;

import mbm.playback_data_service.domain.PlaybackDataState;
import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.publisher.PublisherService;
import mbm.playback_data_service.publisher.configuration.PublisherMqProperties;
import mbm.playback_data_service.publisher.mapper.PlaybackDataMessageMapper;
import mbm.playback_data_service.publisher.message.PlaybackDataMessage;
import mbm.playback_data_service.serialization.SerializationService;
import mbm.playback_data_service.session.SessionStateService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImp implements PublisherService {

    private final StreamBridge streamBridge;
    private final SerializationService serializationService;
    private final PublisherMqProperties publisherMqProperties;
    private final PlaybackDataMessageMapper playbackDataMessageMapper;
    private final SessionStateService sessionStateService;

    public PublisherServiceImp(final StreamBridge streamBridge,
                               final SerializationService serializationService,
                               final PublisherMqProperties publisherMqProperties,
                               final PlaybackDataMessageMapper playbackDataMessageMapper, final SessionStateService sessionStateService) {
        this.streamBridge = streamBridge;
        this.serializationService = serializationService;
        this.publisherMqProperties = publisherMqProperties;
        this.playbackDataMessageMapper = playbackDataMessageMapper;
        this.sessionStateService = sessionStateService;
    }


    @Override
    public void publishPlaybackData(final PlaybackDataDto playbackDataDto, final String sessionId) {
        final PlaybackDataMessage playbackDataMessage = playbackDataMessageMapper.map(playbackDataDto, sessionId);
        final String serializedMessage = serializationService.serialize(playbackDataMessage);
        streamBridge.send(publisherMqProperties.getPlaybackDataTopicName(), serializedMessage);

        sessionStateService.updateSessionState(sessionId, PlaybackDataState.KEY, PlaybackDataState.VALUE);
    }
}
