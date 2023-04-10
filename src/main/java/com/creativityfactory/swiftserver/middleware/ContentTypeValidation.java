package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware is for validation of incoming content-type, because the exchange data-type in
 * the generated REST-APIs by this framework is JSON.
 */
public class ContentTypeValidation implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        if (req.contentType() == null || !req.contentType().equals("application/json")) {
            res.sendError(Response.REST_BAD_REQUEST, "The mime-type of request is not application/json");
            return;
        }

        req.next(true);
    }
}
