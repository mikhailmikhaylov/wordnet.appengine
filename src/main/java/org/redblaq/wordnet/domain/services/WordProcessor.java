package org.redblaq.wordnet.domain.services;

import org.redblaq.wordnet.domain.entities.TextEntry;
import org.redblaq.wordnet.domain.entities.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.redblaq.wordnet.webapp.services.ServiceProvider.obtainService;

// TODO refactor
/* package */ class WordProcessor {

    private final String input;
    private final int offset;

    private final WordService wordService;
    private List<TextEntry> inputWords;
    private List<TextEntry> unknownTextWords;

    private Set<String> knownForms = new HashSet<>();

    WordProcessor(String input, int offset) {
        this.input = input;
        this.offset = offset;
        wordService = obtainService(WordService.class);

        prepareBaseWords();
        prepareInput();
        processTextInput();
    }

    List<String> obtainUniqueWords() {
        final List<String> uniqueWords = new ArrayList<>(unknownTextWords.size());
        for (TextEntry word : unknownTextWords) {
            uniqueWords.add(word.getWord().getBaseWord());
        }
        return uniqueWords;
    }

    List<TextEntry> obtainUniqueEntries() {
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
        final List<Word> knownWords = OfyService.objectify().load().type(Word.class).list();

        // Each of these words only has baseForm now
        for (Word knownWord : knownWords) {
            knownForms.add(knownWord.getBaseWord());
        }
    }

    private List<TextEntry> convertInput(String input) {
        return wordService.convertRawInput(input);
    }

    private final Logger log = LoggerFactory.getLogger(WordProcessor.class);
}
