package mbm.playback_data_service.domain;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Track(String artist, String title) implements Serializable {}
