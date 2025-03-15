package com.csg.searchindexer;

import com.csg.searchindexer.business.RuleFactory;
import com.csg.searchindexer.business.rules.LengthFilterRule;
import com.csg.searchindexer.business.rules.ProcessingRule;
import com.csg.searchindexer.handler.FileProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SearchIndexer components
 */
public class SearchIndexerTest {

    @TempDir
    Path tempDir;

    private Path testFile;
    private FileProcessor processor;

    @BeforeEach
    void setUp() throws IOException {
        // Create a test file with sample content
        testFile = tempDir.resolve("test-content.txt");
        List<String> lines = Arrays.asList(
                "This is a test file with multiple words",
                "Some words start with M like Monday and march",
                "Mississippi is longer than five characters",
                "Mathematics and mechanism are both long M-words",
                "Short m-word: mad, me, my"
        );
        Files.write(testFile, lines);

        // Set up a processor with default rules using the factory
        processor = new FileProcessor();
        processor.addRule(RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, 'M', true));
        processor.addRule(RuleFactory.createRule(RuleFactory.RuleType.LENGTH_FILTER, 5, LengthFilterRule.FilterType.GREATER_THAN));
    }

    @Test
    void testFileProcessorWithValidFile() throws IOException {
        Map<String, Object> results = processor.processFile(testFile.toString());

        // Should find 6 words starting with M/m
        assertEquals(12L, results.get("Words starting with M/m"));

        // Should find words longer than 5 characters
        @SuppressWarnings("unchecked")
        List<String> longWords = (List<String>) results.get("Words with length > 5");

        assertTrue(longWords.contains("multiple"));
        assertTrue(longWords.contains("Monday"));
        assertTrue(longWords.contains("Mississippi"));
        assertTrue(longWords.contains("characters"));
        assertTrue(longWords.contains("Mathematics"));
        assertTrue(longWords.contains("mechanism"));
        assertEquals(9, longWords.size());
    }

    @Test
    void testFileProcessorWithNonExistentFile() {
        assertThrows(IOException.class, () -> {
            processor.processFile("non-existent-file.txt");
        });
    }

    @Test
    void testRuleFactory() {
        List<String> testWords = Arrays.asList("Man", "woman", "Mouse", "cat", "dog", "Monkey", "mammoth");

        // Test creating a case-sensitive StartsWithLetterRule
        ProcessingRule caseSensitiveRule = RuleFactory.createRule(
                RuleFactory.RuleType.STARTS_WITH_LETTER, 'M', false);
        assertEquals(3L, caseSensitiveRule.process(testWords));

        // Test creating a case-insensitive StartsWithLetterRule
        ProcessingRule caseInsensitiveRule = RuleFactory.createRule(
                RuleFactory.RuleType.STARTS_WITH_LETTER, 'M', true);
        assertEquals(4L, caseInsensitiveRule.process(testWords));

        // Test creating different LengthFilterRules
        List<String> lengthTestWords = Arrays.asList("cat", "elephant", "dog", "hippopotamus", "ant", "rhinoceros");

        ProcessingRule equalRule = RuleFactory.createRule(
                RuleFactory.RuleType.LENGTH_FILTER, 3, LengthFilterRule.FilterType.EQUAL_TO);
        List<?> equalResult = (List<?>) equalRule.process(lengthTestWords);
        assertEquals(3, equalResult.size());

        ProcessingRule greaterRule = RuleFactory.createRule(
                RuleFactory.RuleType.LENGTH_FILTER, 5, LengthFilterRule.FilterType.GREATER_THAN);
        List<?> greaterResult = (List<?>) greaterRule.process(lengthTestWords);
        assertEquals(3, greaterResult.size());
    }

    @Test
    void testInvalidRuleFactoryParameters() {
        // Test with invalid parameters
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.LENGTH_FILTER, 5);
        });
    }
}
