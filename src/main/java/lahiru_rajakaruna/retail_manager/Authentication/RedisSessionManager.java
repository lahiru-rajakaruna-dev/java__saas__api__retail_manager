package lahiru_rajakaruna.retail_manager.Authentication;

import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;
import lahiru_rajakaruna.retail_manager.Common.SessionManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class RedisSessionManager implements SessionManager {
    private final RedisTemplate<String, AuthSession> redis;

    public RedisSessionManager(RedisTemplate<String, AuthSession> redis) {
        this.redis = redis;
    }

    @Override
    public void set(String token, AuthSession session) {
        this.set(token, session, SecurityConstants.DEFAULT_SESSION_TTL_24);
    }

    public void set(String token, AuthSession session, int ttl) {
        redis.opsForValue()
             .set(token, session, Duration.ofHours(ttl));
    }
    
    public AuthSession get(String token) {
        return redis.opsForValue()
                    .get(token);
    }

    public AuthSession delete(String token) {
        return redis.opsForValue()
                    .getAndDelete(token);
    }
}
