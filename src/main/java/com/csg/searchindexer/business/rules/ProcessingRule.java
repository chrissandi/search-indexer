package com.csg.searchindexer.business.rules;

import java.util.List;

/**
 * Interface for text processing rules
 * Allows for easy extension of business rules
 */
public interface ProcessingRule {
    String getRuleName();

    Object process(List<String> words);
}
