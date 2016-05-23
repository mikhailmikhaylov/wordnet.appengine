package org.redblaq.wordnet.domain.entities.dto;

@SuppressWarnings("WeakerAccess") // Jackson
public class TextEntryDto {
    private String word;
    private int position;

    @SuppressWarnings("unused") // Explicitly defined for Jackson
    private TextEntryDto() {
    }

    /* package */ TextEntryDto(String word, int position) {
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
