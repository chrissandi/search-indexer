package com.csg.searchindexer.handler;

import com.csg.searchindexer.business.rules.ProcessingRule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * FileProcessor handles loading and tokenizing text files
 * Applies processing rules and collects results
 */
public class FileProcessor {
    private static final Logger LOGGER = Logger.getLogger(FileProcessor.class.getName());
    private final List<ProcessingRule> rules;
    private static final Pattern WORD_DELIMITER = Pattern.compile("\\s+|[,.;:!?\"()\\[\\]{}]");

    public FileProcessor() {
        this.rules = new ArrayList<>();
    }

    public void addRule(ProcessingRule rule) {
        rules.add(rule);
    }

    public Map<String, Object> processFile(String filePath) throws IOException {
        LOGGER.log(Level.INFO, "Processing file: {0}", filePath);

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File does not exist: " + filePath);
        }

        List<String> words = tokenizeFile(path);
        LOGGER.log(Level.INFO, "Extracted {0} words from file", words.size());

        Map<String, Object> results = new HashMap<>();
        for (ProcessingRule rule : rules) {
            Object result = rule.process(words);
            results.put(rule.getRuleName(), result);
        }

        return results;
    }

    private List<String> tokenizeFile(Path filePath) throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = WORD_DELIMITER.split(line);
                words.addAll(Arrays.stream(lineWords)
                        .filter(word -> !word.trim().isEmpty())
                        .collect(Collectors.toList()));
            }
        }

        return words;
    }
}
