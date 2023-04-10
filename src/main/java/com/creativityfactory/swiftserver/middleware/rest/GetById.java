package com.creativityfactory.swiftserver.middleware.rest;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware is for a specific resource by id.
 */
public class GetById implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Persistence<Object> dtSource = (Persistence<Object>) req.getAttribute("dtSource");

        Object id = req.getAttribute("id");
        try {
            Object response = dtSource.getById(id);

            if (response == null) {
                res.status(404);
            }
            // TODO: more checks
            res.json(IdUtils.mapIdToObject(response, model));
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
        }
    }
}
