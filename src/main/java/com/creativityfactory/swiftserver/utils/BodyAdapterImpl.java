package com.creativityfactory.swiftserver.utils;

import com.google.gson.Gson;
import java.util.Map;

/**
 * Implementation of {@link BodyAdapter} interface, which adapts the body of an HTTP request into various formats.
 */
public class BodyAdapterImpl implements BodyAdapter {
    private Gson gson;
    private String contentType;
    private String encoding;
    private String body;
    private String urlEncodedString;
    private String jsonString;
    private Map<String, String> formData;

    /**
     *
     Constructs a new instance of {@link BodyAdapterImpl} with the given parameters.

     @param gson The {@link Gson} instance to use for serialization/deserialization.
     @param body The body of the HTTP request to be adapted.
     @param contentType The content type of the HTTP request.
     @param encoding The encoding of the HTTP request.
     */
    public BodyAdapterImpl(Gson gson, String body, String contentType, String encoding) {
        this.gson = gson;
        this.contentType = contentType;
        this.encoding = encoding;
        this.body = body;

        if (contentType == null) {
            urlEncodedString = jsonString = "";
        } else if (contentType.equals("application/x-www-form-urlencoded")) {
            urlEncodedString = body;
            this.formData = BodyParser.urlEncoded(urlEncodedString, encoding);
            jsonString = gson.toJson(formData);
        } else if (contentType.equals("application/json")) {
            jsonString = body;
        }
    }

    /**
     * Returns the body of the HTTP request as a {@link String}.
     * @return The body of the HTTP request as a {@link String}.
     */
    @Override
    public String body() {
        return this.body;
    }

    /**
     * Returns the form data of the HTTP request as a {@link Map} of key-value pairs.
     * @return The form data of the HTTP request as a {@link Map} of key-value pairs.
     */
    @Override
    public Map<String, String> formData() {
        return formData;
    }

    /**
     * Returns the JSON representation of the body of the HTTP request as a {@link String}.
     * @return The JSON representation of the body of the HTTP request as a {@link String}.
     */
    @Override
    public String jsonFormat() {
        return jsonString;
    }

    /**
     * Returns the URL-encoded representation of the body of the HTTP request as a {@link String}.
     * @return The URL-encoded representation of the body of the HTTP request as a {@link String}.
     */
    @Override
    public String urlEncodedFormat() {
        if (contentType.equals("application/x-www-form-urlencoded")) return urlEncodedString;
        return null;
    }

    /**
     * Returns the deserialized model object of the body of the HTTP request.
     *
     * @param model The class of the model object.
     * @return The deserialized model object of the body of the HTTP request.
     */
    @Override
    public Object modelFormat(Class<?> model) {
        if (contentType == null) return null;
        try {
            return gson.fromJson(jsonString, model);
        } catch (Exception exception) {
            System.out.println("[modelFormat ]: " + exception.getMessage());
        }

        return null;
    }
}
