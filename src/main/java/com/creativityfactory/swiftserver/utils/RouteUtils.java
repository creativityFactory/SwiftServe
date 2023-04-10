package com.creativityfactory.swiftserver.utils;

import java.net.URLDecoder;
import java.util.*;

/**
 * This class is responsible for handling route operations, such as URL segmentation and pattern matching.
 * It provides methods for parsing a URL and determining if it matches a specific pattern, as well as extracting
 *  variables from the URL for further processing.
 */
public class RouteUtils {
    /**
     * This method takes a URL string and returns a list of segments extracted from it by splitting it using the "/" delimiter. Each segment is a string.
     *
     * @param url The input URL string.
     * @return The list of URL segments.
     */
    public static List<String> allSegment(String url) {
        if (url == null) return new ArrayList<>();
        String[] segs = url.split("/");
        List<String> segments = new ArrayList<>();

        for (String segment: segs)
            if ((segment != null) && !segment.equals(""))
                segments.add(segment);

        return segments;
    }

    /**
     * This method takes a URL pattern and a URL string and returns a boolean indicating whether
     * the URL matches the given pattern or not. The pattern can contain
     * placeholders (segments starting with ':') which match any segment of the URL.
     *
     * @param pattern The URL pattern to match against.
     * @param url The input URL string.
     * @return A boolean indicating whether the URL matches the pattern.
     */
    public static boolean isUrlPatternMatched(String pattern, String url) {
        List<String> urlSegments = allSegment(url);
        List<String> patternSegments = allSegment(pattern);

        if (urlSegments.size() != patternSegments.size()) return false;

        for (int i = 0; i < urlSegments.size(); i++) {
            if ((patternSegments.get(i).charAt(0) != ':') && !patternSegments.get(i).equals(urlSegments.get(i))) return false;
        }

        return true;
    }

    /**
     * This method takes a URL pattern and a URL string and returns a map of key-value pairs
     * containing the values of the placeholder segments in the URL. The placeholders are identified
     * by the ':' prefix.
     *
     * @param pattern The URL pattern to extract parameters from.
     * @param url The input URL string.
     * @return A map of key-value pairs containing the values of the placeholder segments in the URL.
     */
    public static Map<String, String> extractParams(String pattern, String url) {
        Map<String, String> paramsMap = new HashMap<>();

        List<String> patternSegments = allSegment(pattern);
        List<String> urlSegments = allSegment(url);

        for (int i = 0; i < urlSegments.size(); i++) {
            if (patternSegments.get(i).charAt(0) == ':') paramsMap.put(patternSegments.get(i).substring(1), urlSegments.get(i));
            else if (!patternSegments.get(i).equals(urlSegments.get(i))) return new HashMap<>();
        }

        return paramsMap;
    }

    /**
     * This method takes a string of queries and returns a map of key-value pairs containing
     * the queries in the form of key-value pairs.
     * @param queries The input string of queries.
     * @return A map of key-value pairs containing the queries.
     */
    public static Map<String, String> extractQueries(String queries) {
        Map<String, String> keyValueQueries = new HashMap<>();

        if ((queries != null) && !queries.isEmpty()) {
            String[] keyValueList = queries.split("&");
            for (String qu: keyValueList) {
                String[] kv = qu.split("=");
                if ((kv.length == 2) && !kv[0].isEmpty()) {
                    keyValueQueries.put(URLDecoder.decode(kv[0]), URLDecoder.decode(kv[1]));
                } else if(kv.length == 1) {
                    keyValueQueries.put(URLDecoder.decode(kv[0]), "");
                }
            }
        }

        return keyValueQueries;
    }
}
