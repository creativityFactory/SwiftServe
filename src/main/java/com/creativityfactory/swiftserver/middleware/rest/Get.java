package com.creativityfactory.swiftserver.middleware.rest;

import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This middleware is for sending all the existences of this resource.
 */
public class Get implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Persistence<Object> dtSource = (Persistence<Object>) req.getAttribute("dtSource");

        try {
            List<Object> list = null;
            List<Map<String, Object>> responseList = new ArrayList<>();
            String limitQuery = req.query("limit");

            if (limitQuery != null) {
                int limit;

                try {
                    limit = Integer.parseInt(limitQuery);
                    list = dtSource.getLimit(limit);
                } catch (Exception exception) {
                    limitQuery = null;
                    if (exception.getClass() != NumberFormatException.class) throw new Exception(exception);
                }

            }

            // this is not idiot but in the case of the user send malformed limit value
            if (limitQuery == null) list = dtSource.getAll();

            for (Object obj: list) {
                responseList.add(IdUtils.mapIdToObject(obj, model));
            }

            res.json(responseList);
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
        }
    }
}
