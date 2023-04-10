package com.creativityfactory.swiftserver.middleware;

import com.google.gson.Gson;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

/**
 * This middleware is for set the model(Class) which we will create a REST-API for it
 */
public class SetUpModel implements HttpRequestHandler {
    private final Class<?> model;
    private final Gson gson;

    public SetUpModel(Class<?> model, Gson gson) {
        this.model = model;
        this.gson = gson;
    }

    @Override
    public void method(Request req, Response res) {
        req.setAttribute("model", model);
        req.setAttribute("gson", gson);
        req.next(true);
    }
}
