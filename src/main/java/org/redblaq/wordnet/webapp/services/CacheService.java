package org.redblaq.wordnet.webapp.services;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CacheService {

    private final MemcacheService memCache = MemcacheServiceFactory.getMemcacheService();

    public void store(String id, String value) {
        memCache.put(id, value);
    }

    public String retrieve(String id) {
        final Object cachedValue = memCache.get(id);
        if (cachedValue == null) {
            return "";
        }
        return (String) cachedValue;
    }
}
