package org.redblaq.wordnet.domain.services;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import org.redblaq.wordnet.domain.InputUtil;
import org.redblaq.wordnet.domain.entities.TextEntry;
import org.redblaq.wordnet.domain.entities.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WordService {

    private final WordNetDatabase database;

    public WordService() {
        database = WordNetDatabase.getFileInstance();
    }

    @SuppressWarnings("WeakerAccess") // API
    public Word buildWord(String word) {
        if (word == null || word.isEmpty() || word.equals(" ")) {
            return null;
        }

        final Word knownWord = new Word(word);
        knownWord.addForms(getBaseFormCandidates(word));

        final String wordTillApostrophe = truncateApostrophe(word);
        if (!wordTillApostrophe.isEmpty() && !word.equals(wordTillApostrophe)) {
            knownWord.addForm(wordTillApostrophe);
            knownWord.addForms(getBaseFormCandidates(wordTillApostrophe));
        }

        return knownWord;
    }

    @SuppressWarnings("WeakerAccess") // API
    public List<TextEntry> convertRawInput(String rawInput) {
        // Built words cache (input duplicates)
        final Map<String, Word> builtWords = new HashMap<>();
        // Text entry cache (entry duplicates)
        final Map<Word, TextEntry> entries = new HashMap<>();
        // Ready to ship
        final List<TextEntry> orderedEntries = new ArrayList<>();
        final String[] splitInput = rawInput.split(InputUtil.WORDS_SEPARATOR);
        int currentWordPosition = 0;
        for (String rawWord : splitInput) {
            final String simplifiedRawWord = simplifyRawWord(rawWord);
            if (!simplifiedRawWord.isEmpty()) {
                Word word = builtWords.get(simplifiedRawWord);
                if (word == null) {
                    word = buildWord(simplifiedRawWord);
                    builtWords.put(simplifiedRawWord, word);
                }
                TextEntry entry = entries.get(word);
                if (entry == null) {
                    entry = new TextEntry(word);
                    entries.put(word, entry);
                    orderedEntries.add(entry);
                }
                entry.addEntry(currentWordPosition, rawWord);
            }
            currentWordPosition += rawWord.length() + InputUtil.WORDS_SEPARATOR.length();
        }
        return orderedEntries;
    }

    private List<String> getBaseFormCandidates(String word) {
        final List<String> baseFormCandidates = new ArrayList<>();
        final Synset[] synsets = database.getSynsets(word);

        if (synsets.length > 0) {
            Collections.addAll(baseFormCandidates,
                    database.getBaseFormCandidates(word, synsets[0].getType()));
        }
        return baseFormCandidates;
    }

    private String truncateApostrophe(String word) {
        if (!word.contains("'")) {
            return word;
        }
        int index = word.indexOf("'");
        log.info(word + " --- " + word.substring(0, index));
        return word.substring(0, index);
    }

    private String simplifyRawWord(String rawWord) {
        return rawWord.toLowerCase()
                .replace(".", "")
                .replace(",", "")
                .replace("!", "")
                .replace("-", "")
                .replace("?", "")
                .replace("\"", "")
                .replace("\n", "");
    }

    private final Logger log = LoggerFactory.getLogger(WordService.class);
}
