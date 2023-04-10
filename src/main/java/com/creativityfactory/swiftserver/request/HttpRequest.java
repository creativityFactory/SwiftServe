package com.creativityfactory.swiftserver.request;

import com.creativityfactory.swiftserver.response.Response;
import com.creativityfactory.swiftserver.utils.BodyAdapter;
import com.creativityfactory.swiftserver.utils.BodyAdapterImpl;
import com.creativityfactory.swiftserver.utils.IOUtils;
import com.creativityfactory.swiftserver.utils.RouteUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

/**
 * An implementation of the {@link Request} interface that provides methods for getting HTTP request
 * attributes and related stuffs.
 *
 * This class is intended for use in Java EE web applications, where it can be used to get the incoming
 * HTTP request...
 *
 * Example usage:
 *
 * <pre>{@code
 * Request request = new HttpRequest(req);
 * String method = request.method();
 * HttpSession session = request.session();
 * // ...
 * }</pre>
 */
public class HttpRequest implements Request {
    private boolean isContinue;
    private final Gson gson;
    private final HttpServletRequest request;
    private final Map<String, String> queries;
    private final Map<String, String> routeParams;
    // only for data/form and json
    private BodyAdapter bodyAdapter;
    public HttpRequest(HttpServletRequest request) throws IOException {
        isContinue = false;
        gson = new Gson();
        this.request = request;
        // extract queries
        this.queries = RouteUtils.extractQueries(request.getQueryString());

        // extract params
        String pattern = (String) request.getAttribute("pattern");
        routeParams = RouteUtils.extractParams(pattern, path());

        String bodyString = null;
        // parse the body to string: warning does not support files
        if (contentType() != null) {
            bodyString = IOUtils.fromBufferToString(request.getReader());
        }

        this.bodyAdapter = new BodyAdapterImpl(gson, bodyString, contentType(), bodyEncoding());
    }

    @Override
    public String body() {
        return bodyAdapter.body();
    }
    @Override
    public Map<String, String> formDataBody() {
        return bodyAdapter.formData();
    }
    @Override
    public String jsonBody() {
        return bodyAdapter.jsonFormat();
    }

    @Override
    public String urlEncodedFormatBody() {
        return bodyAdapter.urlEncodedFormat();
    }
    @Override
    public Object body(Class<?> clazz) {
        return bodyAdapter.modelFormat(clazz);
    }
    @Override
    public void setBody(String body) {
        bodyAdapter = new BodyAdapterImpl(gson, body, contentType(), bodyEncoding());
    }

    @Override
    public void setAttribute(String name, Object obj) {
        request.setAttribute(name, obj);
    }
    @Override
    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    @Override
    public Object removeAttribute(String name) {
        Object object = getAttribute(name);
        request.removeAttribute(name);
        return object;
    }

    @Override
    public String method() {
        return request.getMethod();
    }

    @Override
    public String protocol() {
        return request.getProtocol();
    }

    @Override
    public String params(String name) {
        return routeParams.get(name);
    }

    @Override
    public String query(String name) {
        return queries.get(name);
    }

    @Override
    public String header(String headerName) {
        return request.getHeader(headerName);
    }

    @Override
    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }

    @Override
    public String contentType() {
        return request.getContentType();
    }

    @Override
    public String bodyEncoding() {
        return request.getCharacterEncoding();
    }

    @Override
    public String contextPath() {
        return request.getContextPath();
    }
    @Override
    public String path() {
        return (request.getPathInfo() == null)? "/":request.getPathInfo();
    }

    @Override
    public String hostname() {
        return request.getServerName();
    }

    @Override
    public int port() {
        return request.getServerPort();
    }

    @Override
    public String url() {
        return request.getRequestURL().toString();
    }

    @Override
    public String pattern() {
        return (String) request.getAttribute("pattern");
    }

    @Override
    public boolean next(boolean isContinue) {
        this.isContinue = isContinue;

        return isContinue;
    }
    @Override
    public boolean shouldContinue() {
        return isContinue;
    }
    @Override
    public HttpSession session() {
        return request.getSession();
    }
}