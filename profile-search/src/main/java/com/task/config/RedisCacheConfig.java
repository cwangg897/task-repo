package com.task.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisCacheConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int database;

    @Value("${profile.cache.minute}")
    private int profileCacheMinute;

    @Value("${profiles.cache.minute}")
    private int profilesCacheMinute;

    @Value("${default.cache.minute}")
    private int defaultCacheMinute;

    @Bean("redisCacheConnectionFactory")
    public RedisConnectionFactory redisCacheConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setDatabase(database);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @ConditionalOnExpression("'${spring.cache.type}' != 'none'")
    public CacheManager redisCacheManager(@Qualifier("redisCacheConnectionFactory")
    RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultConfiguration())
            .withInitialCacheConfigurations(customConfigurationMap())
            .build();
    }

    private RedisCacheConfiguration defaultConfiguration() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext
                .SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .entryTtl(Duration.ofMinutes(defaultCacheMinute));
        return redisCacheConfiguration;
    }

    private Map<String, RedisCacheConfiguration> customConfigurationMap() {
        Map<String, RedisCacheConfiguration> configurationsMap = new HashMap<>();
        configurationsMap.put("profile", defaultConfiguration().entryTtl(Duration.ofMinutes(profileCacheMinute)));
        configurationsMap.put("profiles", defaultConfiguration().entryTtl(Duration.ofMinutes(profilesCacheMinute)));
        return configurationsMap;
    }


}
