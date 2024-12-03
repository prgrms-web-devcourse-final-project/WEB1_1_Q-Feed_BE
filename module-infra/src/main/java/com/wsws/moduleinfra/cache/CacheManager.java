package com.wsws.moduleinfra.cache;

public interface CacheManager {

    <T> T get(String key, Class<T> type);

    void set(String key, Object value, long ttlInMinutes);

    void evict(String key);

}
