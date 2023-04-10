package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

import java.util.Date;
import java.util.Map;

/**
 * This middleware has the rule of set upping http caching.
 */
public class CacheResource implements HttpRequestHandler {
    private Map<String, Date> cacheableResources;

    public CacheResource(Map<String, Date> cacheableResources) {
        this.cacheableResources = cacheableResources;
    }

    @Override
    public void method(Request req, Response res) {
        try {
            Class<?> model = (Class<?>) req.getAttribute("model");
            Date lastModifiedTime = cacheableResources.get(model.getName());

            long ifModifiedSince = req.getDateHeader("If-Modified-Since");
            if (ifModifiedSince > 0 && ifModifiedSince == lastModifiedTime.getTime()) {
                // If the modification time matches, send a 304 Not Modified response
                res.status(Response.REST_NOT_MODIFIED);
                return;
            }
            res.cacheControl(36000);

            lastModifiedTime = new Date();
            lastModifiedTime.setTime((lastModifiedTime.getTime() / 1000) * 1000);
            cacheableResources.put(model.getName(), lastModifiedTime);
            res.lastModified(lastModifiedTime.getTime());

            req.next(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "Try later");
        }
    }
}
