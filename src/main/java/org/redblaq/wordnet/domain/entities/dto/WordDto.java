package org.redblaq.wordnet.domain.entities.dto;

import java.util.ArrayList;
import java.util.List;

/* package */ class WordDto {
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

    /* package */ String getBaseForm() {
        return baseForm;
    }

    /* package */ List<TextEntryDto> getMatchingWords() {
        return matchingWords;
    }
}