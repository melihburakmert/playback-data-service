package mbm.playback_data_service.auth.service.imp;

import mbm.playback_data_service.auth.web.SpotifyTokenResponse;
import mbm.playback_data_service.auth.configuration.SpotifyAuthProperties;
import mbm.playback_data_service.auth.service.SpotifyTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class SpotifyTokenServiceImp implements SpotifyTokenService {

    private static final String GRANT_TYPE = "grant_type";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CODE = "code";
    private static final String REDIRECT_URI = "redirect_uri";

    private final SpotifyAuthProperties authProperties;
    private final RestClient restClient;

    public SpotifyTokenServiceImp(final SpotifyAuthProperties authProperties, final RestClient restClient) {
        this.authProperties = authProperties;
        this.restClient = restClient;
    }

    @Override
    public SpotifyTokenResponse exchangeAuthorizationCode(final String authorizationCode) {
        final HttpHeaders headers = getHttpHeaders();
        final MultiValueMap<String, String> body = getBody(authorizationCode);

        return restClient
                .post()
                .uri(authProperties.getTokenUri())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(body)
                .retrieve()
                .body(SpotifyTokenResponse.class);
    }

    private MultiValueMap<String, String> getBody(final String authorizationCode) {
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE, AUTHORIZATION_CODE);
        body.add(CODE, authorizationCode);
        body.add(REDIRECT_URI, authProperties.getRedirectUri());
        return body;
    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(authProperties.getClientId(), authProperties.getClientSecret());
        return headers;
    }
}
