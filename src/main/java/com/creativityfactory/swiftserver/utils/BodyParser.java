package com.creativityfactory.swiftserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that parses the body of an HTTP request.
 */
public class BodyParser {
    /**
     * Parses the URL-encoded body of an HTTP request and returns a map of key-value pairs.
     *
     * @param body the URL-encoded body string to be parsed
     * @param encoding the character encoding to be used for decoding the URL-encoded string
     * @return a map of key-value pairs parsed from the URL-encoded body string
     * @throws NullPointerException if the body or encoding is null
     * @throws IllegalArgumentException if the encoding is not supported
     */
    public static Map<String, String> urlEncoded(String body, String encoding) {

        try {
            Map<String, String> parsedBody = new HashMap<>();

            String[] encodedKeysValuesList = body.split("&");

            for (String keyValue: encodedKeysValuesList) {
                String[] encodedKeyValue = keyValue.split("=");

                if (encodedKeyValue.length == 2) {
                    parsedBody.put(URLDecoder.decode(encodedKeyValue[0], encoding), URLDecoder.decode(encodedKeyValue[1], encoding));
                } else {
                    parsedBody.put(URLDecoder.decode(encodedKeyValue[0], encoding), "");
                }
            }

            return parsedBody;
        } catch (UnsupportedEncodingException exception) {
            System.out.println("[BodyParser.urlEncoded ]: " + exception.getMessage());
        }

        return null;
    }
}
