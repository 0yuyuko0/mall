package com.yuyuko.mall.redis.core;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RedisExpires {
    private Long expires;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private Long randomRange;

    public long getExpires() {
        return expires;
    }

    public void init() {
        assert expires != null;
        assert randomRange != null;
        assert timeUnit != null;
        this.expires = timeUnit.toMillis(expires);
        this.randomRange = timeUnit.toMillis(randomRange);
        assert expires >= randomRange;
        long random = ThreadLocalRandom.current().nextLong(randomRange) * 2 - randomRange;
        this.expires = expires + random;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setRandomRange(Long randomRange) {
        this.randomRange = randomRange;
    }

    @Override
    public String toString() {
        return "RedisExpires{" +
                "expires=" + expires +
                ", timeUnit=" + timeUnit +
                ", randomRange=" + randomRange +
                '}';
    }
}