package com.csg.searchindexer.handler;

import com.csg.searchindexer.business.RuleFactory;
import com.csg.searchindexer.business.rules.LengthFilterRule;
import com.csg.searchindexer.business.rules.ProcessingRule;
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
 * Unit tests for the FileProcessor class
 */
public class FileProcessorTest {

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

        // Set up a processor
        processor = new FileProcessor();
    }

    @Test
    void testProcessFileWithNoRules() throws IOException {
        Map<String, Object> results = processor.processFile(testFile.toString());

        // Should return an empty map since no rules were added
        assertTrue(results.isEmpty());
    }

    @Test
    void testProcessFileWithRules() throws IOException {
        // Add some rules
        processor.addRule(RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, 'M', true));
        processor.addRule(RuleFactory.createRule(RuleFactory.RuleType.LENGTH_FILTER, 5, LengthFilterRule.FilterType.GREATER_THAN));

        Map<String, Object> results = processor.processFile(testFile.toString());

        // Should have 2 results
        assertEquals(2, results.size());
        assertTrue(results.containsKey("Words starting with M/m"));
        assertTrue(results.containsKey("Words with length > 5"));
    }

    @Test
    void testProcessFileWithNonExistentFile() {
        assertThrows(IOException.class, () -> {
            processor.processFile("non-existent-file.txt");
        });
    }

    @Test
    void testProcessFileWithEmptyFile() throws IOException {
        // Create an empty file
        Path emptyFile = tempDir.resolve("empty-file.txt");
        Files.createFile(emptyFile);

        // Add a rule
        processor.addRule(RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, 'M', true));

        Map<String, Object> results = processor.processFile(emptyFile.toString());

        // Should have 1 result with count 0
        assertEquals(1, results.size());
        assertEquals(0L, results.get("Words starting with M/m"));
    }

    @Test
    void testProcessFileWithSpecialCharacters() throws IOException {
        // Create a file with special characters
        Path specialFile = tempDir.resolve("special-chars.txt");
        List<String> lines = Arrays.asList(
                "This has punctuation! And, some; other: chars.",
                "Multiple   spaces   should   be   handled.",
                "What about (parentheses) and [brackets]?",
                "Maybe even some \"quotes\" to test"
        );
        Files.write(specialFile, lines);

        // Add a rule to count words with 'a'
        processor.addRule(RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, 'a', true));

        Map<String, Object> results = processor.processFile(specialFile.toString());

        // Should count words starting with 'a' or 'A' properly
        assertEquals(3L, results.get("Words starting with A/a"));
    }
}