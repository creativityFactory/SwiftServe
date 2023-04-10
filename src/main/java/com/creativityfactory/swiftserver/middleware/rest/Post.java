package com.creativityfactory.swiftserver.middleware.rest;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;


/**
 * This middleware for creating new resource.
 */
public class Post implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Persistence<Object> dtSource = (Persistence<Object>) req.getAttribute("dtSource");
        try {
            Object obj = req.body(model);
            Object response = dtSource.save(obj);
            if (response == null) {
                res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "The resource has not registered, try later");
                return;
            }

            res.status(Response.REST_CREATED).json(IdUtils.mapIdToObject(response, model));
            req.next(true);
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
        }
    }
}
