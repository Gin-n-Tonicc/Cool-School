package com.coolSchool.coolSchool.config;

import com.coolSchool.coolSchool.exceptions.rateLimiting.RateLimitExceededException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Aspect
@Component
public class RateLimitAspect {

    private final int MAX_REQUESTS_PER_MINUTE = 10; // 10 requests per minute - /60000
    private final MessageSource messageSource;
    private final ConcurrentHashMap<String, AtomicInteger> requestCounters = new ConcurrentHashMap<>();

    public RateLimitAspect(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Before("@annotation(com.coolSchool.coolSchool.interfaces.RateLimited)")
    public void enforceRateLimit() {
        String key = String.valueOf(System.currentTimeMillis() / 60000);
        AtomicInteger counter = requestCounters.computeIfAbsent(key, k -> new AtomicInteger(0));

        if (counter.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            throw new RateLimitExceededException(messageSource);
        }
    }
}
