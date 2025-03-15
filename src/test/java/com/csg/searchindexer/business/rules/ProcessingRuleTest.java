package com.csg.searchindexer.business.rules;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ProcessingRule interface
 * Testing with concrete implementations to verify interface behavior
 */
public class ProcessingRuleTest {

    @Test
    void testProcessingRuleBasicFunctionality() {
        // Create a simple concrete implementation for testing
        ProcessingRule testRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Test Rule";
            }

            @Override
            public Object process(List<String> words) {
                return words.size();
            }
        };

        // Test the rule name
        assertEquals("Test Rule", testRule.getRuleName());

        // Test processing functionality
        List<String> testWords = Arrays.asList("one", "two", "three");
        assertEquals(3, testRule.process(testWords));
    }

    @Test
    void testProcessingRuleEmptyList() {
        // Create another test implementation
        ProcessingRule testRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Empty List Test";
            }

            @Override
            public Object process(List<String> words) {
                return words.isEmpty() ? "Empty" : "Not Empty";
            }
        };

        // Test with empty list
        List<String> emptyList = Arrays.asList();
        assertEquals("Empty", testRule.process(emptyList));

        // Test with non-empty list
        List<String> nonEmptyList = Arrays.asList("word");
        assertEquals("Not Empty", testRule.process(nonEmptyList));
    }

    @Test
    void testProcessingRuleReturnTypes() {
        // Test rule that returns a String
        ProcessingRule stringRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "String Return Rule";
            }

            @Override
            public Object process(List<String> words) {
                return String.join("-", words);
            }
        };

        List<String> testWords = Arrays.asList("test", "words");
        assertEquals("test-words", stringRule.process(testWords));

        // Test rule that returns an Integer
        ProcessingRule intRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Integer Return Rule";
            }

            @Override
            public Object process(List<String> words) {
                return words.stream()
                        .mapToInt(String::length)
                        .sum();
            }
        };

        assertEquals(9, intRule.process(testWords));

        // Test rule that returns a Boolean
        ProcessingRule boolRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Boolean Return Rule";
            }

            @Override
            public Object process(List<String> words) {
                return words.stream()
                        .anyMatch(word -> word.length() > 4);
            }
        };

        assertEquals(true, boolRule.process(testWords));
    }

    @Test
    void testProcessingRuleHandlesNull() {
        // Create a null-safe implementation
        ProcessingRule nullSafeRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Null Safe Rule";
            }

            @Override
            public Object process(List<String> words) {
                if (words == null) return "Null input";
                return words.size();
            }
        };

        // Test with null input
        assertDoesNotThrow(() -> {
            Object result = nullSafeRule.process(null);
            assertEquals("Null input", result);
        });

        // Test with valid input
        List<String> validList = Arrays.asList("one", "two");
        assertEquals(2, nullSafeRule.process(validList));
    }

    @Test
    void testMultipleImplementations() {
        // Test multiple implementations working together
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date");

        ProcessingRule countRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Count Rule";
            }

            @Override
            public Object process(List<String> wordList) {
                return wordList.size();
            }
        };

        ProcessingRule lengthRule = new ProcessingRule() {
            @Override
            public String getRuleName() {
                return "Total Length Rule";
            }

            @Override
            public Object process(List<String> wordList) {
                return wordList.stream()
                        .mapToInt(String::length)
                        .sum();
            }
        };

        // Verify each implementation works correctly
        assertEquals(4, countRule.process(words));
        assertEquals(21, lengthRule.process(words)); // apple(5) + banana(6) + cherry(6) + date(4) = 21

        // Verify they have distinct names
        assertNotEquals(countRule.getRuleName(), lengthRule.getRuleName());
    }
}