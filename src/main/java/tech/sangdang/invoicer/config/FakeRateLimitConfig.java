package tech.sangdang.invoicer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fake rate limiting configuration for development and testing.
 * This provides basic rate limiting simulation without production dependencies.
 */
@Slf4j
@Configuration
@Profile({"local", "development", "test"})
public class FakeRateLimitConfig {

    @Value("${system.config.rate-limiting.requests-per-minute:100}")
    private int requestsPerMinute;

    @Value("${system.config.rate-limiting.burst-capacity:150}")
    private int burstCapacity;

    @Bean
    public FakeRateLimiter fakeRateLimiter() {
        return new FakeRateLimiter(requestsPerMinute, burstCapacity);
    }

    /**
     * Simple fake rate limiter for development testing
     */
    public static class FakeRateLimiter {
        private final int maxRequestsPerMinute;
        private final int burstCapacity;
        private final ConcurrentHashMap<String, RateLimitBucket> buckets;

        public FakeRateLimiter(int maxRequestsPerMinute, int burstCapacity) {
            this.maxRequestsPerMinute = maxRequestsPerMinute;
            this.burstCapacity = burstCapacity;
            this.buckets = new ConcurrentHashMap<>();
            log.info("Initialized fake rate limiter: {}/min, burst: {}", 
                    maxRequestsPerMinute, burstCapacity);
        }

        public boolean isAllowed(String apiKey) {
            // Always allow test keys to pass for development
            if (apiKey.contains("test") || apiKey.contains("fake")) {
                log.debug("Allowing test API key: {}...", apiKey.substring(0, 8));
                return true;
            }

            RateLimitBucket bucket = buckets.computeIfAbsent(apiKey, k -> new RateLimitBucket());
            
            long now = System.currentTimeMillis();
            long windowStart = now - Duration.ofMinutes(1).toMillis();
            
            // Clean old requests
            bucket.cleanOldRequests(windowStart);
            
            int currentCount = bucket.getRequestCount();
            
            if (currentCount >= maxRequestsPerMinute) {
                log.warn("Rate limit exceeded for API key: {}... ({}/{})", 
                        apiKey.substring(0, 8), currentCount, maxRequestsPerMinute);
                return false;
            }
            
            bucket.addRequest(now);
            log.debug("Rate limit check passed: {}/{} for key: {}...", 
                    currentCount + 1, maxRequestsPerMinute, apiKey.substring(0, 8));
            
            return true;
        }

        public int getCurrentCount(String apiKey) {
            RateLimitBucket bucket = buckets.get(apiKey);
            return bucket != null ? bucket.getRequestCount() : 0;
        }
    }

    private static class RateLimitBucket {
        private final ConcurrentHashMap<Long, AtomicInteger> requests = new ConcurrentHashMap<>();

        void addRequest(long timestamp) {
            // Group by minute
            long minute = timestamp / 60000;
            requests.computeIfAbsent(minute, k -> new AtomicInteger(0)).incrementAndGet();
        }

        void cleanOldRequests(long windowStart) {
            long windowStartMinute = windowStart / 60000;
            requests.entrySet().removeIf(entry -> entry.getKey() < windowStartMinute);
        }

        int getRequestCount() {
            return requests.values().stream().mapToInt(AtomicInteger::get).sum();
        }
    }
}
