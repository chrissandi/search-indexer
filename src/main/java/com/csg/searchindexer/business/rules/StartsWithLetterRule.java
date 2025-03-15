package com.csg.searchindexer.business.rules;

import java.util.List;

/**
 * Rule to count words starting with a specific letter
 * Can be configured for case sensitivity
 */
public class StartsWithLetterRule implements ProcessingRule {
    private final char letter;
    private final boolean ignoreCase;

    public StartsWithLetterRule(char letter, boolean ignoreCase) {
        this.letter = letter;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public String getRuleName() {
        return "Words starting with " + (ignoreCase ? Character.toUpperCase(letter) + "/" + Character.toLowerCase(letter) : letter);
    }

    @Override
    public Object process(List<String> words) {
        if (ignoreCase) {
            char upperCase = Character.toUpperCase(letter);
            char lowerCase = Character.toLowerCase(letter);
            return words.stream()
                    .filter(word -> !word.isEmpty() && (word.charAt(0) == upperCase || word.charAt(0) == lowerCase))
                    .count();
        } else {
            return words.stream()
                    .filter(word -> !word.isEmpty() && word.charAt(0) == letter)
                    .count();
        }
    }
}
