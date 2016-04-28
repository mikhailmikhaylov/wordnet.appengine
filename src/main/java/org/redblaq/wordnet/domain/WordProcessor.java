package org.redblaq.wordnet.domain;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

import java.util.*;

/* package */ class WordProcessor {

    private final String input;

    private final WordNetDatabase database;
    private List<Word> knownWords;
    private List<Word> inputWords;
    private List<Word> uniqueInput;

    WordProcessor(String input, boolean demo) {
        this.input = input;
        database = WordNetDatabase.getFileInstance();
        if (demo) {
            prepareDemoWords();
        } else {
            prepareKnownWords();
        }
        prepareInput();
        obtainUniqueInput();
    }

    static void preConfigureWordNet() {
        System.setProperty("wordnet.database.dir", "stuff/WordNet-3.0/dict");
    }

    List<String> obtainUniqueWords() {
        final List<String> uniqueWords = new ArrayList<>(uniqueInput.size());
        for (Word word : uniqueInput) {
            uniqueWords.add(word.getBaseWord());
        }
        return uniqueWords;
    }

    private void obtainUniqueInput() {
        final List<Word> uniqueInput = new ArrayList<>();

        for (Word word : inputWords) {
            boolean known = false;
            for (Word knownWord : knownWords) {
                if (word.equals(knownWord)) {
                    known = true;
                    break;
                }
            }
            if (!known) {
                uniqueInput.add(word);
            }
        }

        this.uniqueInput = uniqueInput;
    }

    private void prepareInput() {
        this.inputWords = convertInput(this.input);
    }

    private String truncateApostrophe(String word) {
        if (!word.contains("'")) {
            return word;
        }
        int index = word.indexOf("'");
        return word.substring(0, index);
    }

    private void prepareDemoWords() {
        prepareKnownOrDemoWords(BaseWordsStore.DEMO_VALUE);
    }

    private void prepareKnownWords() {
        prepareKnownOrDemoWords(BaseWordsStore.VALUE);
    }

    private void prepareKnownOrDemoWords(String input) {
        String[] splitKnownWords = input.split(" ");
        final List<Word> knownWords = new ArrayList<>(splitKnownWords.length);

        for (String word : splitKnownWords) {
            Word w = buildWord(word);
            if (w != null) {
                knownWords.add(w);
            }
        }

        this.knownWords = knownWords;

        prepareUserWords();
    }

    private void prepareUserWords() {
// TODO wow much commented code
//        final File customWordsFile = new File(KNOWLEDGE_BASE_PATH);
//        if (customWordsFile.exists()) {
//            BufferedReader br = null;
//            try {
//                br = new BufferedReader(new FileReader(KNOWLEDGE_BASE_PATH));
//                StringBuilder sb = new StringBuilder();
//                String line = br.readLine();
//
//                while (line != null) {
//                    sb.append(line);
//                    sb.append(System.lineSeparator());
//                    line = br.readLine();
//                }
//                final String input = sb.toString();
//                knownWords.addAll(convertInput(input));
//            } catch (IOException ignored) {
//                // Nope, I don't care.
//            } finally {
//                if (br != null) {
//                    try {
//                        br.close();
//                    } catch (IOException ignored) {
//                        // Well done, Closeable API!
//                    }
//                }
//            }
//        }
    }

    private List<Word> convertInput(String input) {
        String thisInput = input.toLowerCase().replace(".", " ").replace(",", " ").replace("!", " ")
                .replace("-", " ").replace("?", " ").replace("\"", " ").replace("\n", " ");
        String[] splitInput = thisInput.trim().split(" ");

        final Set<String> uniqueInput = new LinkedHashSet<>();
        Collections.addAll(uniqueInput, splitInput);

        final List<Word> inputWords = new ArrayList<>(uniqueInput.size());
        for (String word : uniqueInput) {
            Word builtWord = buildWord(word);
            if (builtWord == null) {
                continue;
            }
            final String tWord = truncateApostrophe(word);
            if (!word.equals(tWord)) {
                builtWord.addForm(tWord);
            }
            inputWords.add(builtWord);
        }

        return inputWords;
    }

    private Word buildWord(String word) {
        if (word == null || word.isEmpty() || word.equals(" ")) {
            return null;
        }

        final Word knownWord = new Word(word);
        Synset[] synsets = database.getSynsets(word);
        if (synsets.length > 0) {
            String[] baseFormCandidates = database.getBaseFormCandidates(word, synsets[0].getType());
            knownWord.addForms(baseFormCandidates);
        }
        return knownWord;
    }
}
