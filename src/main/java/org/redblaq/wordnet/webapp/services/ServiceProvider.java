package org.redblaq.wordnet.webapp.services;

import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.services.ProcessorService;

/**
 * Service Injection anti-pattern instead
 */
public enum ServiceProvider {
    INSTANCE;

    private final ProcessorService processorService = new ProcessorService();

    private static final ImmutableMap<Class<?>, ?> services =
            new ImmutableMap.Builder<Class<?>, Object>()
                    .put(CacheService.class, new CacheService())
                    .build();

    public ProcessorService obtainProcessorService() {
        return processorService;
    }

    public <T>T obtain(Class<T> clazz) {
        //noinspection unchecked
        return (T) services.get(clazz);
    }
}
