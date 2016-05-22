package org.redblaq.wordnet.domain.services;

import org.redblaq.wordnet.domain.entities.TextEntry;
import org.redblaq.wordnet.domain.processor.WordProcessor;

import java.util.List;

/**
 * Main service, which provides an API for text processing.
 */
public class ProcessorService {

    /**
     * Starts processing text or text chunk. To have correct positions for entries in chunks,
     * provide an appropriate offset.
     *
     * @param input       text to process
     * @param chunkOffset offst to apply to entries
     * @return list of text entries with word forms and positions in text
     */
    public List<TextEntry> process(String input, int chunkOffset) {
        return new WordProcessor(input, chunkOffset).obtainUniqueEntries();
    }
}
