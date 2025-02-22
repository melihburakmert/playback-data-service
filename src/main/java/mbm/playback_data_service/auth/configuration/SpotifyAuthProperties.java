package mbm.playback_data_service.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spotify-auth")
public class SpotifyAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authUri;
    private String tokenUri;
    private String scope;
}