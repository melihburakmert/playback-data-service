package mbm.playback_data_service.playback.spotify.imp;

import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.playback.spotify.configuration.SpotifyServiceProperties;
import mbm.playback_data_service.playback.spotify.domain.SpotifyRecentlyPlayedResponse;
import mbm.playback_data_service.playback.spotify.mapper.SpotifyResponseToDtoMapper;
import mbm.playback_data_service.playback.PlaybackDataService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SpotifyApiServiceImp implements PlaybackDataService {
    private static final String LIMIT = "limit";

    private final RestClient restClient;
    private final SpotifyResponseToDtoMapper spotifyResponseToDtoMapper;
    private final SpotifyServiceProperties properties;

    public SpotifyApiServiceImp(final RestClient restClient,
                                final SpotifyResponseToDtoMapper spotifyResponseToDtoMapper,
                                final SpotifyServiceProperties properties) {
        this.restClient = restClient;
        this.spotifyResponseToDtoMapper = spotifyResponseToDtoMapper;
        this.properties = properties;
    }

    @Override
    public PlaybackDataDto getRecentlyPlayedTracks(final String accessToken) {
        final String url = String.format("%s?" + LIMIT + "=%d",
                properties.getRecentlyPlayedUri(),
                properties.getRecentlyPlayedLimit());

        final SpotifyRecentlyPlayedResponse response = getRecentlyPlayedResponse(accessToken, url);

        return spotifyResponseToDtoMapper.mapToPlaybackDataDto(response);
    }

    private SpotifyRecentlyPlayedResponse getRecentlyPlayedResponse(final String accessToken, final String url) {
        return restClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(SpotifyRecentlyPlayedResponse.class);
    }
}



