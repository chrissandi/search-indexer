package com.csg.searchindexer.business.rules;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LengthFilterRuleTest {

    private final List<String> testWords = Arrays.asList("a", "ab", "abc", "abcd", "abcde");

    @Test
    public void testEqualToFilter() {
        LengthFilterRule rule = new LengthFilterRule(3, LengthFilterRule.FilterType.EQUAL_TO);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(testWords);

        assertEquals(1, result.size());
        assertTrue(result.contains("abc"));
        assertEquals("Words with length = 3", rule.getRuleName());
    }

    @Test
    public void testLessThanFilter() {
        LengthFilterRule rule = new LengthFilterRule(3, LengthFilterRule.FilterType.LESS_THAN);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(testWords);

        assertEquals(2, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("ab"));
        assertEquals("Words with length < 3", rule.getRuleName());
    }

    @Test
    public void testGreaterThanFilter() {
        LengthFilterRule rule = new LengthFilterRule(3, LengthFilterRule.FilterType.GREATER_THAN);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(testWords);

        assertEquals(2, result.size());
        assertTrue(result.contains("abcd"));
        assertTrue(result.contains("abcde"));
        assertEquals("Words with length > 3", rule.getRuleName());
    }

    @Test
    public void testLessThanOrEqualFilter() {
        LengthFilterRule rule = new LengthFilterRule(3, LengthFilterRule.FilterType.LESS_THAN_OR_EQUAL);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(testWords);

        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("ab"));
        assertTrue(result.contains("abc"));
        assertEquals("Words with length <= 3", rule.getRuleName());
    }

    @Test
    public void testGreaterThanOrEqualFilter() {
        LengthFilterRule rule = new LengthFilterRule(3, LengthFilterRule.FilterType.GREATER_THAN_OR_EQUAL);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(testWords);

        assertEquals(3, result.size());
        assertTrue(result.contains("abc"));
        assertTrue(result.contains("abcd"));
        assertTrue(result.contains("abcde"));
        assertEquals("Words with length >= 3", rule.getRuleName());
    }

    @Test
    public void testEmptyList() {
        LengthFilterRule rule = new LengthFilterRule(3, LengthFilterRule.FilterType.EQUAL_TO);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(Arrays.asList());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testNoMatchingWords() {
        LengthFilterRule rule = new LengthFilterRule(10, LengthFilterRule.FilterType.EQUAL_TO);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) rule.process(testWords);

        assertTrue(result.isEmpty());
    }
}