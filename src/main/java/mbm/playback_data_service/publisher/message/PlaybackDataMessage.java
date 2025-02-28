package mbm.playback_data_service.publisher.message;

import mbm.playback_data_service.domain.Track;

import java.io.Serializable;
import java.util.List;

public record PlaybackDataMessage(List<Track> tracks, String sessionId) implements Serializable {
}
