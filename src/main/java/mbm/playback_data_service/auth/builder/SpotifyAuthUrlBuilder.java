package mbm.playback_data_service.auth.builder;

import mbm.playback_data_service.auth.configuration.SpotifyAuthProperties;
import org.springframework.stereotype.Component;

@Component
public class SpotifyAuthUrlBuilder {

    private static final String CLIENT_ID = "client_id";
    private static final String RESPONSE_TYPE = "response_type";
    private static final String CODE = "code";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String SCOPE = "scope";
    private static final String STATE = "state";

    private final SpotifyAuthProperties authProperties;

    public SpotifyAuthUrlBuilder(final SpotifyAuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public String buildAuthorizationUrl(final String state) {
        return authProperties.getAuthUri() +
                "?" + CLIENT_ID + "=" + authProperties.getClientId() +
                "&" + RESPONSE_TYPE + "=" + CODE +
                "&" + REDIRECT_URI + "=" + authProperties.getRedirectUri() +
                "&" + SCOPE + "=" + authProperties.getScope() +
                "&" + STATE + "=" + state;
    }
}
