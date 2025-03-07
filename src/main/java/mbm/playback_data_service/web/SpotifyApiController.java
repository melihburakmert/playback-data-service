package mbm.playback_data_service.web;

import lombok.extern.slf4j.Slf4j;
import mbm.playback_data_service.domain.PlaybackDataState;
import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.playback.PlaybackDataService;
import mbm.playback_data_service.publisher.PublisherService;
import mbm.playback_data_service.session.SessionStateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestController
@RequestMapping("/playback")
public class SpotifyApiController {

    private static final String SESSION_HEADER = "X-Session-Id";

    private final PlaybackDataService spotifyApiService;
    private final PublisherService publisherService;
    private final SessionStateService sessionStateService;

    public SpotifyApiController(final PlaybackDataService spotifyApiService,
                                final PublisherService publisherService, final SessionStateService sessionStateService) {
        this.spotifyApiService = spotifyApiService;
        this.publisherService = publisherService;
        this.sessionStateService = sessionStateService;
    }

    @GetMapping("/recently-played")
    public ResponseEntity<PlaybackDataResponse> getRecentlyPlayedTracks(
            @RequestHeader(value = SESSION_HEADER) final String sessionId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String spotifyToken) {

        if (isNullOrBlank(spotifyToken)) {
            log.error("Unauthorized request: No Spotify token provided.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (isPlaybackDataPublished(sessionId)) {
            log.info("Playback data already published for session: " + sessionId);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            final PlaybackDataDto playbackDataDto = spotifyApiService.getRecentlyPlayedTracks(spotifyToken);
            publisherService.publishPlaybackData(playbackDataDto, sessionId);
            return ResponseEntity.accepted().build();

        } catch (final HttpClientErrorException.Unauthorized e) {
            log.error("Unauthorized request: Invalid Spotify token provided.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (final Exception e) {
            log.error("Error occurred while fetching recently played tracks.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isNullOrBlank(final String spotifyToken) {
        return spotifyToken == null || spotifyToken.isBlank();
    }

    private boolean isPlaybackDataPublished(final String sessionId) {
        final Object playbackState = sessionStateService.getSessionState(sessionId, PlaybackDataState.KEY);
        return playbackState != null && PlaybackDataState.VALUE.equalsIgnoreCase(playbackState.toString());
    }
}
