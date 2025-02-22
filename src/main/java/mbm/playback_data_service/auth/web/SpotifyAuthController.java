package mbm.playback_data_service.auth.web;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import mbm.playback_data_service.auth.builder.SpotifyAuthUrlBuilder;
import mbm.playback_data_service.auth.service.SpotifyTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/playback/auth")
public class SpotifyAuthController {

    private static final String REDIRECT_URI = "redirect_uri";
    private static final String OAUTH_STATE = "oauth_state";
    private static final String ACCESS_TOKEN_SESSION_KEY = "accessToken";
    private static final String LOCATION_HEADER = "Location";

    private final SpotifyAuthUrlBuilder authUrlBuilder;
    private final SpotifyTokenService tokenService;

    public SpotifyAuthController(final SpotifyAuthUrlBuilder authUrlBuilder, final SpotifyTokenService tokenService) {
        this.authUrlBuilder = authUrlBuilder;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam(value = "redirect", required = false) final String redirectUrl,
            final HttpSession session) {

        log.info("Authorization started");

        final String state = storeStateInSession(session);
        storeRedirectUriInSession(redirectUrl, session);

        final String authorizationUrl = authUrlBuilder.buildAuthorizationUrl(state);
        return buildRedirectResponse(authorizationUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(
            @RequestParam("code") final String code,
            @RequestParam("state") final String receivedState,
            final HttpSession session) {

        if (isInvalidState(receivedState, session)) {
            log.warn("Invalid state received");
            return ResponseEntity.badRequest().build();
        }

        return handleTokenExchange(code, session);
    }

    private ResponseEntity<Void> handleTokenExchange(final String code, final HttpSession session) {
        try {
            final SpotifyTokenResponse tokenResponse = tokenService.exchangeAuthorizationCode(code);
            storeAccessTokenInSession(tokenResponse, session);

            final String redirectUri = (String) session.getAttribute(REDIRECT_URI);
            if (redirectUri != null) {
                return redirectToUriAndClearSession(redirectUri, session);
            }

            return ResponseEntity.ok().build();
        } catch (final Exception e) {
            log.error("Authorization failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<Void> redirectToUriAndClearSession(final String redirectUri, final HttpSession session) {
        log.info("Redirecting to {}", redirectUri);
        session.removeAttribute(REDIRECT_URI);
        return ResponseEntity.status(HttpStatus.FOUND).header(LOCATION_HEADER, redirectUri).build();
    }

    private void storeAccessTokenInSession(final SpotifyTokenResponse tokenResponse, final HttpSession session) {
        session.setAttribute(ACCESS_TOKEN_SESSION_KEY, tokenResponse.accessToken());
        log.info("Authorized and access token stored");
    }

    private boolean isInvalidState(final String receivedState, final HttpSession session) {
        final String originalState = (String) session.getAttribute(OAUTH_STATE);
        return (originalState == null || !originalState.equals(receivedState));
    }

    private String storeStateInSession(final HttpSession session) {
        final String state = UUID.randomUUID().toString();
        session.setAttribute(OAUTH_STATE, state);
        return state;
    }

    private void storeRedirectUriInSession(final String redirectUrl, final HttpSession session) {
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            session.setAttribute(REDIRECT_URI, redirectUrl);
        }
    }

    private ResponseEntity<Void> buildRedirectResponse(final String authorizationUrl) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(LOCATION_HEADER, authorizationUrl)
                .build();
    }
}
