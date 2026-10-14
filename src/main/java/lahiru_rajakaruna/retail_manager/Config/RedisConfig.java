package lahiru_rajakaruna.retail_manager.Config;

import lahiru_rajakaruna.retail_manager.Authentication.DTOs.AuthSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, AuthSession> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, AuthSession> template = new RedisTemplate<>();

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new GenericJacksonJsonRedisSerializer(new ObjectMapper()));
        template.setHashValueSerializer(new GenericJacksonJsonRedisSerializer(new ObjectMapper()));

        return template;
    }
}
