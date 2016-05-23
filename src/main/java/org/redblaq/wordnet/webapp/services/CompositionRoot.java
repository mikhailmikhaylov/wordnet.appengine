package org.redblaq.wordnet.webapp.services;

import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.services.KnownWordsStore;
import org.redblaq.wordnet.domain.services.ProcessorService;
import org.redblaq.wordnet.domain.services.WordService;

public enum CompositionRoot {

    INSTANCE;

    static {
        preConfigureWordNet();
        INSTANCE.compose();
    }

    private ImmutableMap<Class<?>, ?> registry;

    private void compose() {
        final CacheService cacheService = new CacheService();
        final ChannelService channelService = new ChannelService();
        final WordService wordService = new WordService();
        final ProcessorService processorService = new ProcessorService();
        final KnownWordsStore knownWordsStore = new KnownWordsStore();

        registry = new ImmutableMap.Builder<Class<?>, Object>()
                .put(CacheService.class, cacheService)
                .put(WordService.class, wordService)
                .put(ProcessorService.class, processorService)
                .put(ChannelService.class, channelService)
                .put(KnownWordsStore.class, knownWordsStore)
                .build();
    }

    // TODO:SEVERE:mikhail: Reduce the number of usages.
    public <T> T resolve(Class<T> clazz) {
        if (!registry.containsKey(clazz)) {
            throw new IllegalArgumentException("No registry found for class: " + clazz.getName());
        }
        //noinspection unchecked
        return (T) registry.get(clazz);
    }

    private static void preConfigureWordNet() {
        System.setProperty("wordnet.database.dir", "stuff/WordNet-3.0/dict");
    }
}
