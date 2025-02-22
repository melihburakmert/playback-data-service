package mbm.playback_data_service.web.mapper;

import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.web.PlaybackDataResponse;
import org.springframework.stereotype.Component;

@Component
public class PlaybackDataResponseMapper {

        public PlaybackDataResponse mapToPlaybackDataResponse(final PlaybackDataDto playbackDataDto) {
            return new PlaybackDataResponse(playbackDataDto.tracks());
        }
}
