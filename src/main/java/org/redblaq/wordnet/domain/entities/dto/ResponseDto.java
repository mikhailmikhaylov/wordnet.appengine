package org.redblaq.wordnet.domain.entities.dto;

import org.redblaq.wordnet.domain.entities.TextEntry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ResponseDto {

    private final List<WordDto> unknownWords = new ArrayList<>();

    private ResponseDto() {
    }

    public List<WordDto> getUnknownWords() {
        return unknownWords;
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
        private final String baseForm;
        private final List<TextEntryDto> matchingWords = new ArrayList<>();

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
        private final String word;
        private final int position;

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
