package mbm.playback_data_service.playback;

public interface PlaybackDataService {

    PlaybackDataDto getRecentlyPlayedTracks(final String accessToken);
}
