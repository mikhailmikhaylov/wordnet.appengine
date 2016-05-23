package org.redblaq.wordnet.domain.entities.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // Jackson
public class WordDto {
    private String baseForm;
    private List<TextEntryDto> matchingWords = new ArrayList<>();

    private WordDto() {
    }

    /* package */ WordDto(String baseForm) {
        this(baseForm, new ArrayList<TextEntryDto>());
    }

    /* package */ WordDto(String baseForm, List<TextEntryDto> matchingWords) {
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