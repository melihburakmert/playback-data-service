package mbm.playback_data_service.playback.spotify.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Track(String name, List<Artist> artists) {
}