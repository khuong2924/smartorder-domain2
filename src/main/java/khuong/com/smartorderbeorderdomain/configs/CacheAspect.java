package khuong.com.smartorderbeorderdomain.configs;

import khuong.com.smartorderbeorderdomain.menu.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheAspect {
    private final CacheService cacheService;

    @AfterReturning(
            pointcut = "@annotation(org.springframework.cache.annotation.CachePut)",
            returning = "result"
    )
    public void logCachePut(JoinPoint joinPoint, Object result) {
        log.debug("Cache updated for method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict)",
            returning = "result"
    )
    public void logCacheEvict(JoinPoint joinPoint, Object result) {
        log.debug("Cache evicted for method: {}", joinPoint.getSignature().getName());
    }
}
