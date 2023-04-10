package com.creativityfactory.swiftserver.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RouteUtilsTest {
    @Test
    public void testAllSegmentWithEmptyString() {
        List<String> segments = RouteUtils.allSegment("");
        assertEquals(0, segments.size());
    }

    @Test
    public void testAllSegmentWithSingleSegment() {
        List<String> segments = RouteUtils.allSegment("/users");
        assertEquals(1, segments.size());
        assertEquals("users", segments.get(0));
    }

    @Test
    public void testAllSegmentWithMultipleSegments() {
        List<String> segments = RouteUtils.allSegment("/users/1/books");
        assertEquals(3, segments.size());
        assertEquals("users", segments.get(0));
        assertEquals("1", segments.get(1));
        assertEquals("books", segments.get(2));
    }

    @Test
    public void testAllSegmentWithLeadingSlash() {
        List<String> segments = RouteUtils.allSegment("/users/1");
        assertEquals(2, segments.size());
        assertEquals("users", segments.get(0));
        assertEquals("1", segments.get(1));
    }

    @Test
    public void testAllSegmentWithTrailingSlash() {
        List<String> segments = RouteUtils.allSegment("users/1/");
        assertEquals(2, segments.size());
        assertEquals("users", segments.get(0));
        assertEquals("1", segments.get(1));
    }

    @Test
    public void testAllSegmentWithConsecutiveSlashes() {
        List<String> segments = RouteUtils.allSegment("/users//1//books/");
        assertEquals(3, segments.size());
        assertEquals("users", segments.get(0));
        assertEquals("1", segments.get(1));
        assertEquals("books", segments.get(2));
    }

    @Test
    public void testAllSegmentWithNullInput() {
        List<String> segments = RouteUtils.allSegment(null);
        assertEquals(0, segments.size());
    }
    // pattern patching
    @Test
    public void testMatchingPatternWithSameUrl() {
        String pattern = "/users/:userId";
        String url = "/users/123";
        assertTrue(RouteUtils.isUrlPatternMatched(pattern, url));
    }

    @Test
    public void testMatchingPatternWithDifferentUrl() {
        String pattern = "/users/:userId";
        String url = "/users/abc";
        assertTrue(RouteUtils.isUrlPatternMatched(pattern, url));
    }

    @Test
    public void testNonMatchingPatternWithSameUrl() {
        String pattern = "/users/:userId/posts";
        String url = "/users/123";
        assertFalse(RouteUtils.isUrlPatternMatched(pattern, url));
    }

    @Test
    public void testNonMatchingPatternWithDifferentUrl() {
        String pattern = "/users/:userId/posts";
        String url = "/users/abc/comments";
        assertFalse(RouteUtils.isUrlPatternMatched(pattern, url));
    }

    @Test
    public void testMatchingPatternWithNoPlaceholders() {
        String pattern = "/users";
        String url = "/users";
        assertTrue(RouteUtils.isUrlPatternMatched(pattern, url));
    }

    @Test
    public void testNonMatchingPatternWithNoPlaceholders() {
        String pattern = "/users";
        String url = "/users/123";
        assertFalse(RouteUtils.isUrlPatternMatched(pattern, url));
    }
    // extract params
    @Test
    public void testExtractParamsWithMatchingUrlAndPattern() {
        String pattern = "/users/:userId/posts/:postId";
        String url = "/users/123/posts/456";
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("userId", "123");
        expectedParams.put("postId", "456");
        assertEquals(expectedParams, RouteUtils.extractParams(pattern, url));
    }

    @Test
    public void testExtractParamsWithMismatchedUrlAndPattern() {
        String pattern = "/users/:userId/posts/:postId";
        String url = "/users/123/comments/456";
        Map<String, String> expectedParams = new HashMap<>();
        assertEquals(expectedParams, RouteUtils.extractParams(pattern, url));
    }

    @Test
    public void testExtractParamsWithDuplicatePlaceholder() {
        String pattern = "/users/:userId/posts/:userId";
        String url = "/users/123/posts/456";
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("userId", "456");
        assertEquals(expectedParams, RouteUtils.extractParams(pattern, url));
    }

    @Test
    public void testExtractParamsWithNoPlaceholders() {
        String pattern = "/users";
        String url = "/users";
        Map<String, String> expectedParams = new HashMap<>();
        assertEquals(expectedParams, RouteUtils.extractParams(pattern, url));
    }

    // extractQueries
    @Test
    public void testExtractQueriesWithEmptyInput() {
        Map<String, String> actual = RouteUtils.extractQueries("");
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testExtractQueriesWithNullInput() {
        Map<String, String> actual = RouteUtils.extractQueries(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testExtractQueriesWithSingleQuery() {
        Map<String, String> expected = new HashMap<>();
        expected.put("id", "123");

        Map<String, String> actual = RouteUtils.extractQueries("id=123");

        assertEquals(expected, actual);
    }

    @Test
    public void testExtractQueriesWithMultipleQueries() {
        Map<String, String> expected = new HashMap<>();
        expected.put("id", "123");
        expected.put("name", "John Doe");
        expected.put("age", "30");

        Map<String, String> actual = RouteUtils.extractQueries("id=123&name=John%20Doe&age=30");

        assertEquals(expected, actual);
    }

    @Test
    public void testExtractQueriesWithQueryWithoutValue() {
        Map<String, String> expected = new HashMap<>();
        expected.put("id", "");

        Map<String, String> actual = RouteUtils.extractQueries("id=");

        assertEquals(expected, actual);
    }

    @Test
    public void testExtractQueriesWithQueryWithoutKey() {
        Map<String, String> expected = new HashMap<>();

        Map<String, String> actual = RouteUtils.extractQueries("=123");

        assertEquals(expected, actual);
    }
}