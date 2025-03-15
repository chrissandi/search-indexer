package com.csg.searchindexer.business.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the StartsWithLetterRule class
 */
public class StartsWithLetterRuleTest {

    @Test
    void testGetRuleNameCaseSensitive() {
        StartsWithLetterRule rule = new StartsWithLetterRule('A', false);
        assertEquals("Words starting with A", rule.getRuleName());
    }

    @Test
    void testGetRuleNameCaseInsensitive() {
        StartsWithLetterRule rule = new StartsWithLetterRule('M', true);
        assertEquals("Words starting with M/m", rule.getRuleName());
    }

    @Test
    void testProcessCaseSensitiveUppercase() {
        StartsWithLetterRule rule = new StartsWithLetterRule('A', false);
        List<String> words = Arrays.asList("Apple", "apple", "Banana", "Avocado", "apricot");

        // Only uppercase 'A' words should be counted
        long result = (long) rule.process(words);
        assertEquals(2, result);
    }

    @Test
    void testProcessCaseSensitiveLowercase() {
        StartsWithLetterRule rule = new StartsWithLetterRule('a', false);
        List<String> words = Arrays.asList("Apple", "apple", "Banana", "Avocado", "apricot");

        // Only lowercase 'a' words should be counted
        long result = (long) rule.process(words);
        assertEquals(2, result);
    }

    @Test
    void testProcessCaseInsensitive() {
        StartsWithLetterRule rule = new StartsWithLetterRule('A', true);
        List<String> words = Arrays.asList("Apple", "apple", "Banana", "Avocado", "apricot");

        // Both uppercase and lowercase 'A' words should be counted
        long result = (long) rule.process(words);
        assertEquals(4, result);
    }

    @Test
    void testProcessWithEmptyList() {
        StartsWithLetterRule rule = new StartsWithLetterRule('A', true);
        List<String> emptyList = Collections.emptyList();

        long result = (long) rule.process(emptyList);
        assertEquals(0, result);
    }

    @Test
    void testProcessWithEmptyStrings() {
        StartsWithLetterRule rule = new StartsWithLetterRule('A', true);
        List<String> wordsWithEmpty = Arrays.asList("Apple", "", "  ", "apricot");

        // Empty strings should be skipped
        long result = (long) rule.process(wordsWithEmpty);
        assertEquals(2, result);
    }

    @Test
    void testProcessWithNonLetters() {
        StartsWithLetterRule rule = new StartsWithLetterRule('1', false);
        List<String> numberedWords = Arrays.asList("1st", "2nd", "3rd", "first");

        long result = (long) rule.process(numberedWords);
        assertEquals(1, result);
    }

    @Test
    void testProcessWithSpecialCharacters() {
        StartsWithLetterRule rule = new StartsWithLetterRule('$', false);
        List<String> specialCharWords = Arrays.asList("$money", "cash", "$dollar", "€euro");

        long result = (long) rule.process(specialCharWords);
        assertEquals(2, result);
    }

    @ParameterizedTest
    @CsvSource({
            "A, false, Apple apple Avocado banana, 2",
            "a, false, Apple apple Avocado banana, 1",
            "A, true, Apple apple Avocado banana, 3",
            "b, true, Apple apple Avocado banana, 1",
            "z, true, Apple apple Avocado banana, 0"
    })
    void testParameterizedProcess(char letter, boolean ignoreCase, String wordsStr, long expected) {
        StartsWithLetterRule rule = new StartsWithLetterRule(letter, ignoreCase);
        List<String> words = Arrays.asList(wordsStr.split(" "));

        long result = (long) rule.process(words);
        assertEquals(expected, result);
    }

    @Test
    void testWithDifferentLetters() {
        List<String> words = Arrays.asList(
                "Alpha", "beta", "Charlie", "delta", "Echo",
                "foxtrot", "Golf", "hotel", "India"
        );

        // Test with different letters
        assertEquals(1L, new StartsWithLetterRule('A', false).process(words));
        assertEquals(1L, new StartsWithLetterRule('b', false).process(words));
        assertEquals(1L, new StartsWithLetterRule('C', false).process(words));
        assertEquals(1L, new StartsWithLetterRule('d', false).process(words));

        // Test case insensitive
        assertEquals(1L, new StartsWithLetterRule('a', true).process(words));
        assertEquals(1L, new StartsWithLetterRule('B', true).process(words));
        assertEquals(1L, new StartsWithLetterRule('c', true).process(words));
        assertEquals(1L, new StartsWithLetterRule('E', true).process(words));
    }

    @Test
    void testEdgeCases() {
        StartsWithLetterRule rule = new StartsWithLetterRule('X', true);

        // Test with very long word
        String veryLongWord = "X" + "a".repeat(10000);
        List<String> wordsWithLongWord = Arrays.asList("Apple", veryLongWord);
        long result = (long) rule.process(wordsWithLongWord);
        assertEquals(1, result);
    }

    @Test
    void testBoundaryTests() {
        // Test with Unicode characters
        StartsWithLetterRule unicodeRule = new StartsWithLetterRule('α', true); // Greek alpha
        List<String> greekWords = Arrays.asList("αβγ", "Αβγ", "βγδ"); // lowercase alpha, uppercase Alpha, beta
        long unicodeResult = (long) unicodeRule.process(greekWords);
        assertEquals(2, unicodeResult);

        // Test with all letters of the alphabet
        List<String> alphabet = Arrays.asList(
                "Alpha", "Bravo", "Charlie", "Delta", "Echo",
                "Foxtrot", "Golf", "Hotel", "India", "Juliet",
                "Kilo", "Lima", "Mike", "November", "Oscar",
                "Papa", "Quebec", "Romeo", "Sierra", "Tango",
                "Uniform", "Victor", "Whiskey", "X-ray", "Yankee", "Zulu"
        );

        // Each letter should match exactly one word
        for (char c = 'A'; c <= 'Z'; c++) {
            StartsWithLetterRule letterRule = new StartsWithLetterRule(c, false);
            assertEquals(1L, letterRule.process(alphabet), "Failed for letter: " + c);
        }
    }

    @Test
    void testDifferentCasePermutations() {
        List<String> mixedCaseWords = Arrays.asList(
                "Apple", "aPPle", "APPle", "APPLe", "APPLE", "apple"
        );

        // Case-sensitive should only match exactly
        assertEquals(4L, new StartsWithLetterRule('A', false).process(mixedCaseWords));
        assertEquals(2L, new StartsWithLetterRule('a', false).process(mixedCaseWords));

        // Case-insensitive should match both
        assertEquals(6L, new StartsWithLetterRule('A', true).process(mixedCaseWords));
        assertEquals(6L, new StartsWithLetterRule('a', true).process(mixedCaseWords));
    }
}