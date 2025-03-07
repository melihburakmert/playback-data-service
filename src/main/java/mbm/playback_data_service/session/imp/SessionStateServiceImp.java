package mbm.playback_data_service.session.imp;

import mbm.playback_data_service.session.SessionStateService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SessionStateServiceImp implements SessionStateService {

    private static final String SESSION_PREFIX = "session:";
    private static final long EXPIRATION_TIME = 10;

    private final StringRedisTemplate redisTemplate;

    public SessionStateServiceImp(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void updateSessionState(final String sessionId, final String key, final String value) {
        final String sessionKey = SESSION_PREFIX + sessionId;
        redisTemplate.opsForHash().put(sessionKey, key, value);
        redisTemplate.expire(sessionKey, EXPIRATION_TIME, TimeUnit.MINUTES);
    }

    @Override
    public String getSessionState(final String sessionId, final String key) {
        return (String) redisTemplate.opsForHash().get(SESSION_PREFIX + sessionId, key);
    }
}
