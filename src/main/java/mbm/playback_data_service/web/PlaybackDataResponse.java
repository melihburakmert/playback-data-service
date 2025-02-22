package mbm.playback_data_service.web;

import mbm.playback_data_service.domain.Track;

import java.util.List;

public record PlaybackDataResponse(List<Track> tracks) {}
