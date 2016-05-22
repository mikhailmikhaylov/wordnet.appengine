package org.redblaq.wordnet.domain.entities.dto;

/* package */ class TextEntryDto {
    private String word;
    private int position;

    @SuppressWarnings("unused") // Explicitly defined for Jackson
    private TextEntryDto() {
    }

    /* package */ TextEntryDto(String word, int position) {
        this.word = word;
        this.position = position;
    }

    /* package */ String getWord() {
        return word;
    }

    /* package */ int getPosition() {
        return position;
    }
}
