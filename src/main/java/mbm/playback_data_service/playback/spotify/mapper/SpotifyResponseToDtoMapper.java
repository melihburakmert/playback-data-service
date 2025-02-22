package mbm.playback_data_service.playback.spotify.mapper;

import mbm.playback_data_service.playback.PlaybackDataDto;
import mbm.playback_data_service.playback.spotify.domain.Artist;
import mbm.playback_data_service.playback.spotify.domain.PlayedItem;
import mbm.playback_data_service.domain.Track;
import mbm.playback_data_service.playback.spotify.domain.SpotifyRecentlyPlayedResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpotifyResponseToDtoMapper {

    public PlaybackDataDto mapToPlaybackDataDto(final SpotifyRecentlyPlayedResponse response) {
        final List<Track> items = response.items().stream()
                .map(this::buildTrack)
                .toList();

        return new PlaybackDataDto(items);
    }

    private Track buildTrack(final PlayedItem item) {
        final String trackName = item.track().name();
        final String firstArtistName = getFirstArtistNameOrDefault(item);

        return Track.builder()
                .artist(firstArtistName)
                .title(trackName)
                .build();
    }

    private String getFirstArtistNameOrDefault(final PlayedItem item) {
        return item.track().artists().stream().findFirst().map(Artist::name).orElse(null);
    }
}
