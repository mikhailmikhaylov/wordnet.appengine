package org.redblaq.wordnet.webapp.services;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * MemCache service wrapper.
 */
public class CacheService {

    private final MemcacheService memCache = MemcacheServiceFactory.getMemcacheService();

    /**
     * Store some value in memory cache.
     */
    public void store(String id, String value) {
        memCache.put(id, value);
    }

    /**
     * Retrieve some value from memory cache.
     * @param id entry key
     * @return value, if nothing is fount returns empty string
     */
    public String retrieve(String id) {
        final Object cachedValue = memCache.get(id);
        if (cachedValue == null) {
            return "";
        }
        return (String) cachedValue;
    }
}
