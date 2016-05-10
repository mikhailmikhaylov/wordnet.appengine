package org.redblaq.wordnet.domain;

import java.util.Arrays;
import java.util.UUID;

/**
 * Static common input rules and utilities.
 */
@SuppressWarnings("UtilityClass")
public class InputUtil {
    /**
     * Common separation sequence.
     */
    public static final String WORDS_SEPARATOR = " ";
    // Word processor chunk size. The number of words to be processed as a single task.
    private static final int PROCESSOR_CHUNK_SIZE = 300;

    private InputUtil() {
    }

    /**
     * Prepares input parts for processing chunks.
     * <p>Input part includes all the words fro a single chunk.
     */
    public static String[] splitIntoParts(String input) {
        final String[] words = split(input);

        int fullChunks = words.length / PROCESSOR_CHUNK_SIZE;
        int lastChunkSize = words.length % PROCESSOR_CHUNK_SIZE;

        final int resultSize = fullChunks + (lastChunkSize > 0 ? 1 : 0);
        final String[] result = new String[resultSize];

        for (int i = 0; i < fullChunks; i++) {
            final int from = i * PROCESSOR_CHUNK_SIZE;
            final int to = (i + 1) * PROCESSOR_CHUNK_SIZE;
            final String[] chunk = Arrays.copyOfRange(words, from, to);
            final String chunkString = join(chunk, WORDS_SEPARATOR);
            result[i] = chunkString;
        }
        if (lastChunkSize > 0) {
            final int from = words.length - lastChunkSize;
            final int to = words.length;
            final String[] chunk = Arrays.copyOfRange(words, from, to);
            final String chunkString = join(chunk, WORDS_SEPARATOR);
            result[fullChunks] = chunkString;
        }

        return result;
    }

    /**
     * Splits input using common input separator.
     */
    public static String[] split(String input) {
        return input.split(WORDS_SEPARATOR);
    }

    /**
     * Joins String array into a string using common input separator.
     */
    public static String join(String[] stringArray) {
        return join(stringArray, WORDS_SEPARATOR);
    }

    /**
     * Joint String array.
     * <p>Uses StringBuilder. Consider using Strings.join after migrating to Java 8.
     */
    public static String join(String[] stringArray, String delimiter) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringArray.length - 1; i++) {
            stringBuilder.append(stringArray[i]).append(delimiter);
        }
        stringBuilder.append(stringArray[stringArray.length - 1]);
        return stringBuilder.toString();
    }

    /**
     * Prepares an array of UUIDs.
     * @param count number of required UUIDs
     */
    public static String[] prepareIds(int count) {
        final String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = UUID.randomUUID().toString();
        }
        return result;
    }
}
