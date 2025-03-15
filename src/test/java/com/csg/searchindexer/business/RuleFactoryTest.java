package com.csg.searchindexer.business;

import com.csg.searchindexer.business.rules.LengthFilterRule;
import com.csg.searchindexer.business.rules.ProcessingRule;
import com.csg.searchindexer.business.rules.StartsWithLetterRule;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RuleFactory class
 */
public class RuleFactoryTest {

    @Test
    void testCreateStartsWithLetterRule() {
        // Test creating a case-sensitive rule
        ProcessingRule rule1 = RuleFactory.createStartsWithLetterRule('M', false);
        assertNotNull(rule1);
        assertTrue(rule1 instanceof StartsWithLetterRule);
        assertEquals("Words starting with M", rule1.getRuleName());

        // Test creating a case-insensitive rule
        ProcessingRule rule2 = RuleFactory.createStartsWithLetterRule('M', true);
        assertNotNull(rule2);
        assertTrue(rule2 instanceof StartsWithLetterRule);
        assertEquals("Words starting with M/m", rule2.getRuleName());
    }

    @Test
    void testCreateLengthFilterRule() {
        // Test creating different filter types
        ProcessingRule rule1 = RuleFactory.createLengthFilterRule(5, LengthFilterRule.FilterType.EQUAL_TO);
        assertNotNull(rule1);
        assertTrue(rule1 instanceof LengthFilterRule);
        assertEquals("Words with length = 5", rule1.getRuleName());

        ProcessingRule rule2 = RuleFactory.createLengthFilterRule(5, LengthFilterRule.FilterType.GREATER_THAN);
        assertNotNull(rule2);
        assertTrue(rule2 instanceof LengthFilterRule);
        assertEquals("Words with length > 5", rule2.getRuleName());
    }

    @Test
    void testCreateRuleWithStartsWithLetter() {
        // Test creating a rule using the generic method
        ProcessingRule rule = RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, 'M', true);
        assertNotNull(rule);
        assertTrue(rule instanceof StartsWithLetterRule);

        // Test functionality
        List<String> words = Arrays.asList("Man", "woman", "Mouse", "cat");
        assertEquals(2L, rule.process(words));
    }

    @Test
    void testCreateRuleWithLengthFilter() {
        // Test creating a rule using the generic method
        ProcessingRule rule = RuleFactory.createRule(
                RuleFactory.RuleType.LENGTH_FILTER,
                5,
                LengthFilterRule.FilterType.GREATER_THAN
        );
        assertNotNull(rule);
        assertTrue(rule instanceof LengthFilterRule);

        // Test functionality
        List<String> words = Arrays.asList("cat", "elephant", "dog", "hippopotamus");
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(words);
        assertEquals(2, result.size());
        assertTrue(result.contains("elephant"));
        assertTrue(result.contains("hippopotamus"));
    }

    @Test
    void testCreateRuleWithInvalidParameters() {
        // Test with no parameters
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER);
        });

        // Test with wrong parameter type
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, "M", true);
        });

        // Test with insufficient parameters
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.LENGTH_FILTER, 5);
        });

        // Test with wrong parameter type
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.LENGTH_FILTER, "5", LengthFilterRule.FilterType.GREATER_THAN);
        });
    }

    @Test
    void testCreateRuleWithNullParameters() {
        // Test with null parameter
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.STARTS_WITH_LETTER, null, true);
        });

        // Test with null filter type
        assertThrows(IllegalArgumentException.class, () -> {
            RuleFactory.createRule(RuleFactory.RuleType.LENGTH_FILTER, 5, null);
        });
    }
}
