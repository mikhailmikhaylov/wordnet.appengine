package org.redblaq.wordnet.domain.services;

import com.google.appengine.repackaged.com.google.common.base.Joiner;
import org.redblaq.wordnet.domain.entities.TextEntry;

import java.util.List;

public class ProcessorService {

    private static final String OUTPUT_SEPARATOR = "\n";

    public String process(String input) {
        final List<String> words = new WordProcessor(input).obtainUniqueWords();
        return Joiner.on(OUTPUT_SEPARATOR).join(words);
    }

    public List<TextEntry> processText(String input) {
        return new WordProcessor(input).obtainUniqueEntries();
    }
}
