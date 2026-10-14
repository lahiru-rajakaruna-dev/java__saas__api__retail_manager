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

    public void set(String token, AuthSession session) {
        redis.opsForValue()
             .set(token, session, Duration.ofHours(12));
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
