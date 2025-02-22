package mbm.playback_data_service.playback;

import mbm.playback_data_service.domain.Track;

import java.util.List;

public record PlaybackDataDto(List<Track> tracks) {
}
