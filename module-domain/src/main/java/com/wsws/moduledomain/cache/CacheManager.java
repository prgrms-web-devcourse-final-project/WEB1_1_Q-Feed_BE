package com.wsws.moduledomain.cache;

import com.fasterxml.jackson.core.type.TypeReference;

public interface CacheManager {

    <T> T get(String key, Class<T> type);

    void set(String key, Object value, long ttlInMinutes);

    void evict(String key);

    void evictAllByPrefix(String prefix);

    <T> T getJson(String key, TypeReference<T> typeRef);

    void setJson(String key, Object value, long ttlInMinutes);
}
