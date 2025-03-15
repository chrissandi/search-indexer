/**
 * SearchIndexer - A robust text file indexing system for search applications
 *
 * This system processes text files and applies configurable business rules to index content.
 * The architecture follows SOLID principles and uses the Strategy and Factory pattern to make it easy
 * to add new business rules without modifying existing code.
 */
package com.csg.searchindexer;

import com.csg.searchindexer.business.RuleFactory;
import com.csg.searchindexer.business.rules.LengthFilterRule;
import com.csg.searchindexer.handler.FileProcessor;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main entry point for the CSG Search Indexer application
 */
public class SearchIndexer {
    private static final Logger LOGGER = Logger.getLogger(SearchIndexer.class.getName());

    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.log(Level.SEVERE, "Usage: java SearchIndexer <path-to-file>");
            System.exit(1);
        }

        String filePath = args[0];

        try {
            // Create the file processor
            FileProcessor processor = new FileProcessor();

            // Add rules using the factory
            processor.addRule(
                    RuleFactory.createRule(
                            RuleFactory.RuleType.STARTS_WITH_LETTER,
                            'M', true)
            );

            processor.addRule(
                    RuleFactory.createRule(
                            RuleFactory.RuleType.LENGTH_FILTER,
                            5, LengthFilterRule.FilterType.GREATER_THAN)
            );

            // Process the file
            Map<String, Object> results = processor.processFile(filePath);

            // Output the results
            for (Map.Entry<String, Object> result : results.entrySet()) {
                System.out.println(result.getKey() + ": " + result.getValue());
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing file: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}

