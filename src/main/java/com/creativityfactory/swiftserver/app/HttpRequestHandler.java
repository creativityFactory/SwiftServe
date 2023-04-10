package com.creativityfactory.swiftserver.app;

import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * <p>Functional interface for handling HTTP requests. Implementations of this interface should
 * define how to handle a specific HTTP request method,such as GET, POST, PUT, etc.</p>
 *
 * <p>Implementations of this interface should take two parameters: a Request object representing
 * the incoming request, and a Response object representing the response to be sent back to the client.</p>
 */
@FunctionalInterface
public interface HttpRequestHandler {
    void method(Request req, Response res) throws Exception;
}