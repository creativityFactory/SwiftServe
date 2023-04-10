package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.persistence.SingletonDataSource;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware is for set up the data source which the REST API will base on it to do persistence stuffs.
 */
public class SetDataSource implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Persistence<Object> persistence;
        Class<?> model = (Class<?>) req.getAttribute("model");

        try {
            persistence = SingletonDataSource.getInstance(model);
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
            return;
        }

        req.setAttribute("dtSource", persistence);
        req.next(true);
    }
}
