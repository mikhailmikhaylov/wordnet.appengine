package org.redblaq.wordnet.domain.services;

import com.google.appengine.repackaged.com.google.common.base.Joiner;

import java.util.List;

public class ProcessorService {

    private static final String OUTPUT_SEPARATOR = "\n";

    static {
        WordProcessor.preConfigureWordNet();
    }

    public String process(String input) {
        final List<String> words = new WordProcessor(input).obtainUniqueWords();
        return Joiner.on(OUTPUT_SEPARATOR).join(words);
    }
}
