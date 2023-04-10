package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware is for converting the incoming id (from route parameter) from string to its
 * appropriate type.
 */
public class IdConverter implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");

        String idParam = req.params("id");
        Object id = IdUtils.convertId(model, idParam);

        // check the compatibility of the id
        if (id == null) {
            res.sendError(Response.REST_BAD_REQUEST, "the id type does not match the id type of the resource");
            return;
        }

        req.setAttribute("id", id);
        req.next(true);
    }
}
