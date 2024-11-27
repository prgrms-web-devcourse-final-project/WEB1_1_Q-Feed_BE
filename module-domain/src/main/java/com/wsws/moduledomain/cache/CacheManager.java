package com.wsws.moduledomain.cache;

public interface CacheManager {

    Integer get(String key);

    void set(String key, Integer value, long ttlInMinutes);
}
