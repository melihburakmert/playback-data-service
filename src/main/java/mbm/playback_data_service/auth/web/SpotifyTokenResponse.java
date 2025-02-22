package mbm.playback_data_service.auth.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("scope") String scope
) {}