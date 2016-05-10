package org.redblaq.wordnet.domain.entities.dto;

import org.redblaq.wordnet.domain.entities.TextEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class ResponseDto {

    private final List<WordDto> unknownWords = new ArrayList<>();

    private ResponseDto() {
    }

    public List<WordDto> getUnknownWords() {
        return unknownWords;
    }

    public static ResponseDto merge(ResponseDto[] args) {
        final Map<String, WordDto> resultWords = new HashMap<>();
        final List<WordDto> resultWordsSorted = new ArrayList<>();
        for (ResponseDto arg : args) {
            for (WordDto word : arg.unknownWords) {
                final String baseForm = word.getBaseForm();
                WordDto resultWord = resultWords.get(baseForm);
                if (resultWord == null) {
                    resultWord = new WordDto(baseForm);
                    resultWords.put(baseForm, resultWord);
                    resultWordsSorted.add(resultWord);
                }
                resultWord.getMatchingWords().addAll(word.getMatchingWords());
            }
        }

        final ResponseDto result = new ResponseDto();
        result.unknownWords.addAll(resultWordsSorted);
        return result;
    }

    public static ResponseDto of(List<TextEntry> textEntries) {
        final ResponseDto result = new ResponseDto();
        for (TextEntry textEntry : textEntries) {
            final List<TextEntryDto> entries = new ArrayList<>();
            for (TextEntry.Entry entry : textEntry.getEntries()) {
                entries.add(new TextEntryDto(entry.getForm(), entry.getStartPosition()));
            }
            final WordDto word = new WordDto(textEntry.getWord().getBaseWord(), entries);
            result.unknownWords.add(word);
        }
        return result;
    }

    public static class WordDto {
        private String baseForm;
        private List<TextEntryDto> matchingWords = new ArrayList<>();

        private WordDto() {
        }

        private WordDto(String baseForm) {
            this(baseForm, new ArrayList<TextEntryDto>());
        }

        private WordDto(String baseForm, List<TextEntryDto> matchingWords) {
            this.baseForm = baseForm;
            this.matchingWords.addAll(matchingWords);
        }

        public String getBaseForm() {
            return baseForm;
        }

        public List<TextEntryDto> getMatchingWords() {
            return matchingWords;
        }
    }

    public static class TextEntryDto {
        private String word;
        private int position;

        private TextEntryDto() {
        }

        private TextEntryDto(String word, int position) {
            this.word = word;
            this.position = position;
        }

        public String getWord() {
            return word;
        }

        public int getPosition() {
            return position;
        }
    }
}
