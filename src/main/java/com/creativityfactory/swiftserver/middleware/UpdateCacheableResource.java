package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

import java.util.Date;
import java.util.Map;

/**
 * This middleware is for indicating that there is a change in this resource and update the http caching.
 */
public class UpdateCacheableResource implements HttpRequestHandler {
    private Map<String, Date> cacheableResources;

    public UpdateCacheableResource(Map<String, Date> cacheableResources) {
        this.cacheableResources = cacheableResources;
    }

    @Override
    public void method(Request req, Response res) throws IllegalAccessException {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Date date = new Date();
        date.setTime((date.getTime() / 1000) * 1000);
        cacheableResources.put(model.getName(), date);
    }
}
