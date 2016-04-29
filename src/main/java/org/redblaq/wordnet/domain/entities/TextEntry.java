package org.redblaq.wordnet.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class TextEntry {
    private final Word word;
    private final List<Entry> entries = new ArrayList<>();

    public TextEntry(Word word) {
        this.word = word;
    }

    public void addEntry(int startPosition, String form) {
        entries.add(new Entry(startPosition, form));
    }

    public Word getWord() {
        return word;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public static class Entry {
        private final int startPosition;
        private final String form;

        public Entry(int startPosition, String form) {
            this.startPosition = startPosition;
            this.form = form;
        }

        public int getStartPosition() {
            return startPosition;
        }

        public String getForm() {
            return form;
        }
    }
}
