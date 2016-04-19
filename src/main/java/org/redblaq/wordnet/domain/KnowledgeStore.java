package org.redblaq.wordnet.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Knowledge base store.
 */
public class KnowledgeStore {

    // TODO Implement someday
    // TODO Wow, migrate to non-static API after introducing some DI
    public static List<Word> getKnownWords() {
        return new ArrayList<>();
    }
}
