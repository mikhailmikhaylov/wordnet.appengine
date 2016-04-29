package org.redblaq.wordnet.webapp.services;

import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.services.ProcessorService;
import org.redblaq.wordnet.domain.services.WordService;

/**
 * Service Injection anti-pattern instead
 */
public enum ServiceProvider {
    INSTANCE;

    static {
        preConfigureWordNet();
    }

    private final ProcessorService processorService = new ProcessorService();

    private static final ImmutableMap<Class<?>, ?> services =
            new ImmutableMap.Builder<Class<?>, Object>()
                    .put(CacheService.class, new CacheService())
                    .put(WordService.class, new WordService())
                    .build();

    public ProcessorService obtainProcessorService() {
        return processorService;
    }

    public <T> T obtain(Class<T> clazz) {
        //noinspection unchecked
        return (T) services.get(clazz);
    }

    public static <T> T obtainService(Class<T> clazz) {
        return INSTANCE.obtain(clazz);
    }

    private static void preConfigureWordNet() {
        System.setProperty("wordnet.database.dir", "stuff/WordNet-3.0/dict");
    }
}
