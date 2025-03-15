package com.csg.searchindexer.business;

import com.csg.searchindexer.business.rules.LengthFilterRule;
import com.csg.searchindexer.business.rules.ProcessingRule;
import com.csg.searchindexer.business.rules.StartsWithLetterRule;

public class RuleFactory {

    /**
     * Available rule types that can be created by the factory
     */
    public enum RuleType {
        STARTS_WITH_LETTER,
        LENGTH_FILTER
    }

    /**
     * Creates a rule for counting words that start with a specific letter
     *
     * @param letter the letter to check for
     * @param ignoreCase whether to ignore case when matching
     * @return a new StartsWithLetterRule
     */
    public static ProcessingRule createStartsWithLetterRule(char letter, boolean ignoreCase) {
        return new StartsWithLetterRule(letter, ignoreCase);
    }

    /**
     * Creates a rule for filtering words based on their length
     *
     * @param length the length to compare against
     * @param filterType the type of comparison to perform
     * @return a new LengthFilterRule
     */
    public static ProcessingRule createLengthFilterRule(int length, LengthFilterRule.FilterType filterType) {
        return new LengthFilterRule(length, filterType);
    }

    /**
     * Creates a rule based on the specified type and parameters
     *
     * @param type the type of rule to create
     * @param params parameters for the rule (interpretation depends on rule type)
     * @return a new ProcessingRule instance
     * @throws IllegalArgumentException if the parameters are invalid for the specified rule type
     */
    public static ProcessingRule createRule(RuleType type, Object... params) {
        switch (type) {
            case STARTS_WITH_LETTER:
                if (params.length < 1 || !(params[0] instanceof Character)) {
                    throw new IllegalArgumentException("StartsWithLetterRule requires a character parameter");
                }
                boolean ignoreCase = params.length > 1 && params[1] instanceof Boolean ? (Boolean) params[1] : false;
                return createStartsWithLetterRule((Character) params[0], ignoreCase);

            case LENGTH_FILTER:
                if (params.length < 2 || !(params[0] instanceof Integer) || !(params[1] instanceof LengthFilterRule.FilterType)) {
                    throw new IllegalArgumentException("LengthFilterRule requires an integer and a FilterType parameter");
                }
                return createLengthFilterRule((Integer) params[0], (LengthFilterRule.FilterType) params[1]);

            default:
                throw new IllegalArgumentException("Unknown rule type: " + type);
        }
    }
}