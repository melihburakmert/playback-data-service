package mbm.playback_data_service.playback.spotify.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Artist(String name) {
}