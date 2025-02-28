package org.yeopcm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

        RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
                .<String, String>newSerializationContext(new StringRedisSerializer())
                .key(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .value(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .hashKey(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .hashValue(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }

    @Bean
    public ReactiveValueOperations<String, String> reactiveValueOperations(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        return reactiveRedisTemplate.opsForValue();
    }
}
