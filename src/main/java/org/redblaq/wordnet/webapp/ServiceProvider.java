package org.redblaq.wordnet.webapp;

import org.redblaq.wordnet.domain.ProcessorService;

import java.util.HashMap;

/**
 * Service Injection anti-pattern instead
 */
public enum ServiceProvider {
    INSTANCE;

    private final ProcessorService processorService = new ProcessorService();

    private final HashMap services = new HashMap() {{
        put(CacheService.class, new CacheService());
    }};

    public ProcessorService obtainProcessorService() {
        return processorService;
    }

    public <T>T obtain(Class<T> clazz) {
        return (T) services.get(clazz);
    }
}
