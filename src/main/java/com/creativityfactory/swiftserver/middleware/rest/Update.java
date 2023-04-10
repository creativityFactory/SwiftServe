package com.creativityfactory.swiftserver.middleware.rest;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.utils.FieldUtils;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware for updating an existed resource.
 */
public class Update implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Persistence<Object> dtSource = (Persistence<Object>) req.getAttribute("dtSource");

        Object id = req.getAttribute("id");
        try {
            // get the old object
            Object oldObj = dtSource.getById(id);
            if (oldObj == null) {
                res.status(Response.REST_NOT_FOUND).json(null);
                return;
            }
            // get the new object
            Object receivedObj = req.body(model);

            // update object
            Object response = dtSource.update(FieldUtils.updateFields(oldObj, receivedObj));
            res.json(IdUtils.mapIdToObject(response, model));
            req.next(true);
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
        }
    }
}
