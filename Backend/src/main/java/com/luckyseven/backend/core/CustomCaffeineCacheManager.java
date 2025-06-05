package com.luckyseven.backend.core;

import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CustomCaffeineCacheManager extends CaffeineCacheManager {
    private final Map<String, Caffeine<Object, Object>> cacheConfigs = new ConcurrentHashMap<>();

    public void setCacheConfig(String cacheName, Caffeine<Object, Object> cacheConfig) {
        cacheConfigs.put(cacheName, cacheConfig);
    }

    @Override
    protected Cache createCaffeineCache(@NotNull String name) {
        Caffeine<Object, Object> cacheConfig = cacheConfigs.getOrDefault(
                name,
                Caffeine.newBuilder()
                        .expireAfterWrite(2, TimeUnit.HOURS)
                        .maximumSize(10_000)
                        .recordStats()
        );
        return new CaffeineCache(name, cacheConfig.build());
    }
}
