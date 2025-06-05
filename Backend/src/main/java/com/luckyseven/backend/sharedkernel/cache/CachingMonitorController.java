package com.luckyseven.backend.sharedkernel.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 캐시 모니터링을 위한 컨트롤러
 */
@RestController
@RequestMapping("/api/admin/cache")
@RequiredArgsConstructor
public class CachingMonitorController {

    private final CacheManager cacheManager;

    /**
     * 캐시 통계 정보를 반환
     * 
     * @return 캐시별 통계 정보
     */
    @GetMapping("/stats")
    public Map<String, Map<String, Object>> getCacheStats() {
        Map<String, Map<String, Object>> result = new HashMap<>();

        // teamDashboards 캐시 통계 추출
        CaffeineCache dashboardCache = (CaffeineCache) cacheManager.getCache("teamDashboards");
        if (dashboardCache != null) {
            Cache<Object, Object> nativeCache = dashboardCache.getNativeCache();
            CacheStats stats = nativeCache.stats();

            Map<String, Object> cacheStats = new HashMap<>();
            cacheStats.put("hitCount", stats.hitCount());
            cacheStats.put("missCount", stats.missCount());
            cacheStats.put("hitRate", stats.hitRate());
            cacheStats.put("missRate", 1.0 - stats.hitRate());
            cacheStats.put("evictionCount", stats.evictionCount());
            cacheStats.put("estimatedSize", nativeCache.estimatedSize());

            result.put("teamDashboards", cacheStats);
        }

        // 추가 캐시에 대한 통계 확장 가능

        return result;
    }
}
