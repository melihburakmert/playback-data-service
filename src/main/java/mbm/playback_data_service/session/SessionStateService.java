package mbm.playback_data_service.session;


public interface SessionStateService {
    void updateSessionState(String sessionId, String key, String value);

    String getSessionState(String sessionId, String key);
}
