package org.redblaq.wordnet.domain.processor;

import org.redblaq.wordnet.domain.entities.TextEntry;
import org.redblaq.wordnet.domain.entities.Word;
import org.redblaq.wordnet.domain.services.KnownWordsStore;
import org.redblaq.wordnet.domain.services.WordService;
import org.redblaq.wordnet.webapp.services.CompositionRoot;

import java.util.*;

// TODO:SEVERE:mikhail.mikhaylov: Refactoring needed.

/**
 * Word Processor itself.
 * <p>Performs input processing for the whole text or a single chunk.
 */
public class WordProcessor {

    private final String input;
    private final int offset;

    private final WordService wordService;
    private final KnownWordsStore knownWordsStore;
    private List<TextEntry> inputWords;
    private List<TextEntry> unknownTextWords;

    private Set<String> knownForms = new HashSet<>();

    public WordProcessor(String input, int offset) {
        this.input = input;
        this.offset = offset;
        wordService = CompositionRoot.INSTANCE.resolve(WordService.class);
        knownWordsStore = CompositionRoot.INSTANCE.resolve(KnownWordsStore.class);

        prepareBaseWords();
        prepareInput();
        processTextInput();
    }

    public List<TextEntry> obtainUniqueEntries() {
        final List<TextEntry> result = new ArrayList<>();
        for (TextEntry entry : unknownTextWords) {
            result.add(entry);
        }
        translateOffset(result, offset);
        return result;
    }

    private static void translateOffset(List<TextEntry> entries, int offset) {
        for (TextEntry textEntry : entries) {
            for (TextEntry.Entry entry : textEntry.getEntries()) {
                entry.mutateStartPositionRelative(offset);
            }
        }
    }

    private void processTextInput() {
        final List<TextEntry> uniqueTextInput = new ArrayList<>();

        for (TextEntry entry : inputWords) {
            boolean known = false;
            for (String form : entry.getWord().getAllForms()) {
                if (knownForms.contains(form)) {
                    known = true;
                    break;
                }
            }
            if (!known) {
                uniqueTextInput.add(entry);
            }
        }

        this.unknownTextWords = uniqueTextInput;
    }

    private void prepareInput() {
        this.inputWords = convertInput(this.input);
    }

    private void prepareBaseWords() {
        final List<Word> knownWords = knownWordsStore.getKnownWords();

        // Each of these words only has baseForm now
        for (Word knownWord : knownWords) {
            knownForms.add(knownWord.getBaseWord());
        }
    }

    private List<TextEntry> convertInput(String input) {
        return wordService.convertRawInput(input);
    }
}
