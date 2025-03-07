package mbm.playback_data_service.auth.web;

import lombok.extern.slf4j.Slf4j;
import mbm.playback_data_service.auth.service.SpotifyTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/playback/auth")
public class SpotifyAuthController {

    private static final String SESSION_HEADER = "X-Session-Id";

    private final SpotifyTokenService tokenService;

    public SpotifyAuthController(final SpotifyTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    public ResponseEntity<SpotifyTokenResponse> getSpotifyToken(
            @RequestParam("code") final String code,
            @RequestHeader(value = SESSION_HEADER) final String sessionId) {

        return handleTokenExchange(code);
    }

    private ResponseEntity<SpotifyTokenResponse> handleTokenExchange(final String code) {
        final SpotifyTokenResponse tokenResponse = tokenService.exchangeAuthorizationCode(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
