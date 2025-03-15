package com.csg.searchindexer.business.rules;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Rule to filter words based on length
 * Can be configured for different comparison types
 */
public class LengthFilterRule implements ProcessingRule {
    public enum FilterType {
        EQUAL_TO, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL
    }

    private final int length;
    private final LengthFilterRule.FilterType filterType;

    public LengthFilterRule(int length, LengthFilterRule.FilterType filterType) {
        this.length = length;
        this.filterType = filterType;
    }

    @Override
    public String getRuleName() {
        String operator;
        switch (filterType) {
            case EQUAL_TO:
                operator = "=";
                break;
            case LESS_THAN:
                operator = "<";
                break;
            case GREATER_THAN:
                operator = ">";
                break;
            case LESS_THAN_OR_EQUAL:
                operator = "<=";
                break;
            case GREATER_THAN_OR_EQUAL:
                operator = ">=";
                break;
            default:
                operator = "?";
        }
        return "Words with length " + operator + " " + length;
    }

    @Override
    public Object process(List<String> words) {
        return words.stream()
                .filter(word -> {
                    switch (filterType) {
                        case EQUAL_TO:
                            return word.length() == length;
                        case LESS_THAN:
                            return word.length() < length;
                        case GREATER_THAN:
                            return word.length() > length;
                        case LESS_THAN_OR_EQUAL:
                            return word.length() <= length;
                        case GREATER_THAN_OR_EQUAL:
                            return word.length() >= length;
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
