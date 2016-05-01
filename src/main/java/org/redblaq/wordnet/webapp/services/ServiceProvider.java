package org.redblaq.wordnet.webapp.services;

import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.services.ProcessorService;
import org.redblaq.wordnet.domain.services.WordService;

/**
 * Service Injection anti-pattern.
 * <p>Provides an access to services. Is used instead of DI, as we don't have to manage services
 * lifecycle and need them all running during the application lifetime.
 */
public enum ServiceProvider {
    INSTANCE;

    static {
        // Preconfigures system variable for WordNet library.
        preConfigureWordNet();
    }

    private static final ImmutableMap<Class<?>, ?> services =
            new ImmutableMap.Builder<Class<?>, Object>()
                    .put(CacheService.class, new CacheService())
                    .put(WordService.class, new WordService())
                    .put(ProcessorService.class, new ProcessorService())
                    .build();

    /**
     * Obtains a pre-registered service by it's Class.
     */
    public <T> T obtain(Class<T> clazz) {
        //noinspection unchecked
        return (T) services.get(clazz);
    }

    /**
     * Static delegate to {@link #obtain(Class)} method.
     */
    public static <T> T obtainService(Class<T> clazz) {
        return INSTANCE.obtain(clazz);
    }

    private static void preConfigureWordNet() {
        System.setProperty("wordnet.database.dir", "stuff/WordNet-3.0/dict");
    }
}
