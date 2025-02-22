package mbm.playback_data_service.publisher;

import mbm.playback_data_service.playback.PlaybackDataDto;

public interface PublisherService {

        void publishPlaybackData(final PlaybackDataDto playbackDataDto);
}