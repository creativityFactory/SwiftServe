package com.creativityfactory.swiftserver.request;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 * The Request interface represents an HTTP request received by the server.
 *
 * It provides methods to retrieve information about the request, such as the
 * request body, headers, parameters, URL, and more.
 *
 * This interface also includes methods to control the flow of request handling,
 * such as indicating whether the request should continue to be handled by the
 * next middleware or route.
 *
 * <p>Implementations of this interface should be able to handle both HTTP/1.1 and HTTP/2 requests, and should be able to handle
 * requests with or without a body.</p>
 *
 * <p>Users of this interface should be able to obtain information about the request by calling the various accessor methods.
 * Implementations of this interface should provide default implementations for all of these methods, so that users only need
 * to override the methods that they need.</p>
 *
 * <p>Example usage:</p>
 *
 * <pre>{@code
 * Request request = ...; // create an instance of the request object
 *
 * String method = request.method(); // get the HTTP method (GET, POST, etc.)
 * String path = request.path(); // get the path component of the URL
 * String header = request.header(name); // get a request header
 * String contentType = request.contentType(); // get the value of the Content-Type header
 * String name = request.query("name"); // get the value of the "name" query parameter
 * String body = request.body(ClassNAME.class); // get instance of class <code>ClassNAME</code> from the exist body.
 * ...
 * }</pre>
 */
public interface Request {
    /**
     * Gets the body of the request as a string.
     * @return the body of the request as a string.
     */
    String body();
    /**
     * Parses the body of the request as form data and returns it as a map.
     * @return a map containing the form data in the request body.
     */
    Map<String, String> formDataBody();
    /**
     * Gets the body of the request as a JSON string.
     * @return the body of the request as a JSON string.
     */
    String jsonBody();
    /**
     * Gets the body of the request in the URL-encoded format.
     * @return the body of the request in the URL-encoded format.
     */
    String urlEncodedFormatBody();
    /**
     * Parses the body of the request and maps it to an instance of the specified class.
     * @param clazz the class to map the request body to.
     * @return an instance of the specified class containing the request body data.
     * @throws IllegalArgumentException if the request body cannot be mapped to the specified class.
     */
    Object body(Class<?> clazz);
    //-----------------------------------
    /**
     * Parses the body of the request and maps it to an instance of the specified class.
     * @param clazz the class to map the request body to.
     * @return an instance of the specified class containing the request body data.
     * @throws IllegalArgumentException if the request body cannot be mapped to the specified class.
     */
    /**
     * Returns the HTTP request method (e.g. "GET", "POST", "PUT", etc.).
     *
     * @return the HTTP request method
     */
    String method();

    /**
     * Returns the HTTP protocol used for the request (e.g. "HTTP/1.1").
     *
     * @return the HTTP protocol
     */
    String protocol();

    /**
     * Returns the value of the specified request parameter as a String, or null if the parameter does not exist.
     *
     * @param name the name of the parameter to retrieve
     * @return the value of the parameter, or null if the parameter does not exist
     */
    String params(String name);

    /**
     * Returns the value of the specified query parameter as a String, or null if the parameter does not exist.
     *
     * @param name the name of the query parameter to retrieve
     * @return the value of the query parameter, or null if the parameter does not exist
     */
    String query(String name);
    /**
     *
     */
    /**
     * Returns the value of the specified request header as a string. If the request did not include a header with the
     * specified name, this method returns null.
     *
     * @param headerName the name of the header
     * @return a string containing the value of the specified request header, or null if the request does not have a header
     * with that name
     */
    String header(String headerName);

    /**
     * Returns the value of the specified request header as a long value that represents a Date object. Use this method
     * with headers that contain dates, such as If-Modified-Since.
     *
     * @param name the name of the header
     * @return a long value representing the date specified in the header expressed as the number of milliseconds since
     * January 1, 1970 GMT, or -1 if the named header was not included with the request
     */
    long getDateHeader(String name);

    /**
     * Returns the MIME type of the body of the request, or null if the type is not known. This method returns the value of
     * the Content-Type header field if it exists and is accessible; otherwise, it returns null.
     *
     * @return a string containing the name of the MIME type of the request, or null if the type is not known
     */
    String contentType();

    /**
     * Returns the name of the character encoding used in the body of this request. This method returns null if the request
     * does not specify a character encoding.
     *
     * @return a string containing the name of the character encoding, or null if the request does not specify a character
     * encoding
     */
    String bodyEncoding();

    /**
     * Returns the context path of the request. The context path is the portion of the request URI that indicates the context
     * of the request. The context path always comes first in a request URI. The path starts with a "/" character but does not
     * end with a "/" character. If this method is called on a request that was dispatched to a servlet using a RequestDispatcher,
     * the context path returned is the context path of the original request, not the context path of the dispatch target.
     *
     * @return a string containing the context path of the request
     */
    String contextPath();
    /**
     * Returns the path of the current request.
     * The path is the part of the URL after the domain name and context path.
     * For example, if the full URL is "http://example.com/context/servlet-path/path/stuffs",
     * then the path returned by this method would be "/path/stuffs".
     *
     * @return the path of the current request
     */
    String path();

    /**
     * Returns the hostname of the server that received the current request.
     *
     * @return the hostname of the server that received the current request
     */
    String hostname();

    /**
     * Returns the port number on which the server is listening for requests.
     *
     * @return the port number on which the server is listening for requests
     */
    int port();

    /**
     * Returns the full URL of the current request, including the protocol, hostname, port, context path, servlet path, and path.
     *
     * @return the full URL of the current request
     */
    String url();
    /**
     * Returns the path pattern of the current request.
     *
     * @return the path pattern of the request, for example "/path/:id/src".
     */
    String pattern();

    /**
     * Sets the value indicating whether the request should continue to be
     * handled by the next middleware or route.
     *
     * @param isContinue true if the request should continue, false otherwise.
     */
    boolean next(boolean isContinue);

    /**
     * Returns the value indicating whether the request should continue to be
     * handled by the next middleware or route. By default, this value is false.
     *
     * @return true if the request should continue, false otherwise.
     */
    boolean shouldContinue();
    /**
     * Sets the body of this Request. If the given body is null, the effect is the same as calling setBody("") to set an empty body.
     *
     * @param body a String representing the body of this Request.
     */
    void setBody(String body);

    /**
     * Stores an attribute in this request. Attribute names should follow the same conventions as package names.
     * Names beginning with java.*, javax.*, and com.sun.*, are reserved for use by the Servlet specification.
     * If the object passed in is null, the effect is the same as calling removeAttribute(String).
     *
     * @param name the name of the attribute to be stored.
     * @param obj  the object to be stored as the attribute value.
     */
    void setAttribute(String name, Object obj);

    /**
     * Returns the value of the named attribute as an Object, or null if no attribute of the given name exists.
     * Attribute names should follow the same conventions as package names. This specification reserves names matching java.*, javax.*, and sun.*.
     *
     * @param name the name of the attribute to retrieve.
     * @return the value of the named attribute, or null if no attribute of the given name exists.
     */
    Object getAttribute(String name);

    /**
     * Removes the attribute with the given name from this request. If the request does not have an attribute with the given name, this method does nothing.
     *
     * @param name the name of the attribute to remove.
     * @return the value of the removed attribute, or null if no attribute of the given name existed.
     */
    Object removeAttribute(String name);
    /**
     * Returns the HttpSession object associated with this request.
     * If the request does not have a session, this method creates one.
     * This method never returns null and should not create a new session if the
     * request does not have a valid session ID.
     * @return the HttpSession associated with this request
     */
    HttpSession session();
}
