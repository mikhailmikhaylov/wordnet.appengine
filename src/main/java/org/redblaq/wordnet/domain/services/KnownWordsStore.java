package org.redblaq.wordnet.domain.services;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import org.redblaq.wordnet.domain.entities.Word;

import java.util.List;

public class KnownWordsStore {

    private boolean preCachePerformed;
    private ImmutableList<Word> knownWords;

    public KnownWordsStore() {
    }

    public void saveWord(Word word) {
        // No need to build forms, I suppose. Needs result comparison
        // final Word word = CompositionRoot.INSTANCE.resolve(WordService.class).buildWord(wordRaw);
        OfyService.objectify().save().entity(word).now();
    }

    @SuppressWarnings("WeakerAccess") // API
    public List<Word> getKnownWords() {
        preCacheKnownWords();
        return knownWords;
    }

    private void preCacheKnownWords() {
        if (!preCachePerformed) {
            synchronized (this) {
                if (!preCachePerformed) {
                    final List<Word> readWords = OfyService.objectify().load().type(Word.class).list();
                    knownWords = new ImmutableList.Builder<Word>().addAll(readWords).build();
                    preCachePerformed = true;
                }
            }
        }
    }
}
