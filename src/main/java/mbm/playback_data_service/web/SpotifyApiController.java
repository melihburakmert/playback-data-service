package mbm.playback_data_service.web;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.playback.PlaybackDataService;
import mbm.playback_data_service.publisher.PublisherService;
import mbm.playback_data_service.web.mapper.PlaybackDataResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/playback")
public class SpotifyApiController {

    private static final String ACCESS_TOKEN_SESSION_KEY = "accessToken";
    private static final String REDIRECT_URI = "/playback/test";
    // TODO: Reuse this constant. It is duplicated in SessionAuthInterceptor.
    private static final String LOGIN_REDIRECT_URL = "/playback/auth/login?redirect=";

    private final PlaybackDataService spotifyApiService;
    private final PlaybackDataResponseMapper playbackDataResponseMapper;
    private final PublisherService publisherService;

    public SpotifyApiController(final PlaybackDataService spotifyApiService,
                                final PlaybackDataResponseMapper playbackDataResponseMapper,
                                final PublisherService publisherService) {
        this.spotifyApiService = spotifyApiService;
        this.playbackDataResponseMapper = playbackDataResponseMapper;
        this.publisherService = publisherService;
    }

    // TODO: Rename better
    @GetMapping("/recently-played/publish")
    public ResponseEntity<PlaybackDataResponse> getRecentlyPlayedTracks(final HttpSession session) {
        final String accessToken = getAccessTokenFromSession(session);
        if (accessToken == null) {
            log.info("Access token not found. Redirecting to login");
            return buildRedirectResponse();
        }

        final PlaybackDataDto playbackDataDto = spotifyApiService.getRecentlyPlayedTracks(accessToken);
        publisherService.publishPlaybackData(playbackDataDto);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/recently-played")
    public ResponseEntity<PlaybackDataResponse> test(final HttpSession session) {
        final String accessToken = getAccessTokenFromSession(session);
        if (accessToken == null) {
            log.info("Access token not found. Redirecting to login");
            return buildRedirectResponse();
        }

        return fetchRecentlyPlayedTracks(accessToken);
    }

    private String getAccessTokenFromSession(final HttpSession session) {
        return (String) session.getAttribute(ACCESS_TOKEN_SESSION_KEY);
    }

    private ResponseEntity<PlaybackDataResponse> fetchRecentlyPlayedTracks(final String accessToken) {
        try {
            final PlaybackDataDto playbackDataDto = spotifyApiService.getRecentlyPlayedTracks(accessToken);
            final PlaybackDataResponse playbackDataResponse = playbackDataResponseMapper.mapToPlaybackDataResponse(playbackDataDto);
            return ResponseEntity.ok(playbackDataResponse);
        } catch (final Exception e) {
            log.error("Failed to fetch recently played tracks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<PlaybackDataResponse> buildRedirectResponse() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", LOGIN_REDIRECT_URL + REDIRECT_URI)
                .build();
    }
}
