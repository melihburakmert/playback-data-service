package mbm.playback_data_service.publisher.mapper;

import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.publisher.message.PlaybackDataMessage;
import org.springframework.stereotype.Component;

@Component
public class PlaybackDataMessageMapper {
    public PlaybackDataMessage map(final PlaybackDataDto playbackDataDto, final String sessionId) {
        return new PlaybackDataMessage(playbackDataDto.tracks(), sessionId);
    }
}
