package com.creativityfactory.swiftserver.utils;

import java.util.Map;

/**
 *  The BodyAdapter interface defines methods for adapting the body of an HTTP request to various
 *  formats. This interface is designed to implement the Adapter pattern.
 */
public interface BodyAdapter {
    /**
     * Returns the body of the HTTP request as a string.
     * @return the body of the HTTP request as a string
     */
    String body();
    /**
     * Returns the body of the HTTP request as a map of form data.
     * @return the body of the HTTP request as a map of form data
     */
    Map<String, String> formData();
    /**
     * Returns the body of the HTTP request in JSON format.
     * @return the body of the HTTP request in JSON format
     */
    String jsonFormat();
    /**
     * Returns the body of the HTTP request in URL-encoded format.
     * @return the body of the HTTP request in URL-encoded format
     */
    String urlEncodedFormat();
    /**
     * Returns the body of the HTTP request in a format that can be used to create an object of the given model class.
     * @param model the class of the model that the body will be adapted to
     * @return the body of the HTTP request in a format that can be used to create an object of the given model class
     */
    Object modelFormat(Class<?> model);
}