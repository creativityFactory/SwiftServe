package com.creativityfactory.swiftserver.response;

import java.io.IOException;
import java.io.PrintWriter;
/**
 * The Response interface represents the HTTP response to be sent back to the client.
 */

public interface Response {
    /**
     * Http status code constants.
     */
    int REST_INTERNAL_SERVER_ERROR = 500;
    int REST_BAD_REQUEST = 400;
    int REST_NOT_FOUND = 404;
    int REST_NOT_MODIFIED = 304;
    int REST_CREATED = 201;

    /**
     * Sets the status code for the response of an HTTP request.
     * @param n the HTTP code.
     * @return the current Response object for chaining.
     */
    Response status(int n);

    /**
     * Sets the max-age attribute of control-cache header.
     * @param maxAge the max age in milliseconds.
     * @return the current Response object for chaining.
     */
    Response cacheControl(int maxAge);

    /**
     * Sets the header last-modified in the HTTP response.
     * @param last the date in milliseconds.
     * @return the current Response object for chaining.
     */
    Response lastModified(long last);

    /**
     * Writes a string to the response output stream.
     * @param msg the string value to write.
     * @return the current Response object for chaining.
     */
    Response write(String msg);

    /**
     * Gets the PrintWriter object to write to the response output stream.
     * @return the PrintWriter object.
     * @throws IOException if an I/O error occurs.
     */
    PrintWriter out() throws IOException;
    /**
     * Writes an object in JSON format to the response output stream.
     * @param obj the object to write.
     */
    void json(Object obj);
    /**
     * Redirects the request to the specified URL.
     * @param url the URL to redirect to.
     */
    void redirect(String url);

    /**
     *
     * Sets a header for the HTTP response.
     * @param key the header name.
     * @param value the header value.
     * @return the current Response object for chaining.
     */
    Response setHeader(String key, String value);

    /**
     * Sets a date header for the HTTP response.
     * @param name the header name.
     * @param time the time in milliseconds.
     * @return the current Response object for chaining.
     */
    Response setDateHeader(String name, long time);

    /**
     * Renders a JSP page to the response output stream.
     * @param jspPage the JSP page to render.
     * @throws Exception if an error occurs while rendering the JSP page.
     * @return the current Response object for chaining.
     */
    void jsp(String jspPage) throws Exception;
    /**
     * Sends an error response to the client using the specified status code and message.
     * @param i the HTTP status code.
     * @param message the error message.
     */
    void sendError(int i, String message);
}
