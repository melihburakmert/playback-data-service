package mbm.playback_data_service.playback.spotify.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spotify-service")
public class SpotifyServiceProperties {

    private String recentlyPlayedUri;
    private int recentlyPlayedLimit;
}