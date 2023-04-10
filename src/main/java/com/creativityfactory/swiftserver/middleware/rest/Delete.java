package com.creativityfactory.swiftserver.middleware.rest;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware for deleting an existed resource.
 */
public class Delete implements HttpRequestHandler {

    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Persistence<Object> dtSource = (Persistence<Object>) req.getAttribute("dtSource");

        Object id = req.getAttribute("id");
        try {
            Object obj = dtSource.getById(id);

            if (obj == null) {
                res.status(Response.REST_NOT_FOUND).json(null);
                return;
            }

            Object response = dtSource.delete(obj);
            if (response == null) throw new Exception("Deleting is failed");

            res.json(IdUtils.mapIdToObject(response, model));
            req.next(true);
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
        }
    }
}
