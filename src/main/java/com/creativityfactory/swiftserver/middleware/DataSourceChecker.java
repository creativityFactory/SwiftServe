package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;


/**
 * This middleware is for checking the existence of data source or not.
 */
public class DataSourceChecker implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Persistence<Object> dtSource = (Persistence<Object>) req.getAttribute("dtSource");
        if (dtSource == null) {
            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
            return;
        }

        req.next(true);
    }
}
