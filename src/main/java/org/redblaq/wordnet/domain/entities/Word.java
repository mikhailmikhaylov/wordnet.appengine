package org.redblaq.wordnet.domain.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Word {

    // Mutability -> Objectify

    @Id
    private String baseWord;
    private final ArrayList<String> forms = new ArrayList<>();

    private final ArrayList<String> allForms = new ArrayList<>();

    // Objectify constructor
    private Word() {}

    public Word(String value) {
        this.baseWord = value;
    }

    public String getBaseWord() {
        return baseWord;
    }

    public void addForm(String form) {
        if (allForms.size() > 0) {
            allForms.clear();
        }
        this.forms.add(form);
    }

    public void addForms(String[] forms) {
        if (allForms.size() > 0) {
            allForms.clear();
        }
        this.forms.addAll(Arrays.asList(forms));
    }

    @SuppressWarnings("unchecked")
    private List<String> getForms() {
        return (List<String>) forms.clone();
    }

    /**
     * Includes base form.
     */
    @SuppressWarnings("unchecked")
    private List<String> getAllForms() {
        if (allForms.size() == 0) {
            allForms.add(baseWord);
            allForms.addAll(forms);
        }
        return (List<String>) allForms.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Word)) {
            return false;
        }
        if (baseWord == null || baseWord.isEmpty()) {
            return false;
        }
        final Word other = (Word) obj;
        if (other.baseWord == null) {
            return false;
        }
        // This case should be faster than getAllForms in case of a single run.
        final List<String> thisForms = new ArrayList<>();
        thisForms.add(baseWord);
        thisForms.addAll(getForms());
        final List<String> otherForms = other.getAllForms();
        for (String form : thisForms) {
            if (otherForms.contains(form)) {
                return true;
            }
        }
        return false;
    }
}
