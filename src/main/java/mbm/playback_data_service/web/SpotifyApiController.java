package mbm.playback_data_service.web;

import lombok.extern.slf4j.Slf4j;
import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.playback.PlaybackDataService;
import mbm.playback_data_service.publisher.PublisherService;
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

    public SpotifyApiController(final PlaybackDataService spotifyApiService,
                                final PublisherService publisherService) {
        this.spotifyApiService = spotifyApiService;
        this.publisherService = publisherService;
    }

    @GetMapping("/recently-played")
    public ResponseEntity<PlaybackDataResponse> getRecentlyPlayedTracks(
            @RequestHeader(value = SESSION_HEADER) final String sessionId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String spotifyToken) {

        if (spotifyToken == null || spotifyToken.isBlank()) {
            log.error("Unauthorized request: No Spotify token provided.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
}
