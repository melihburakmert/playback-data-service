package mbm.playback_data_service.auth.service;

import mbm.playback_data_service.auth.web.SpotifyTokenResponse;

public interface SpotifyTokenService {

    SpotifyTokenResponse exchangeAuthorizationCode(final String authorizationCode);
}
