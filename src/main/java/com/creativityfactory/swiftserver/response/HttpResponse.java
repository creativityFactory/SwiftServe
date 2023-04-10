package com.creativityfactory.swiftserver.response;

import com.creativityfactory.swiftserver.error.DirectorErrorResponseBuilder;
import com.creativityfactory.swiftserver.error.ErrorMessageBuilder;
import com.creativityfactory.swiftserver.error.ErrorMessageBuilderImpl;
import com.creativityfactory.swiftserver.error.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

/**
 * An implementation of the {@link Response} interface that provides methods for setting HTTP response
 * attributes and writing response data.
 *
 * This class is intended for use in Java EE web applications, where it can be used to generate
 * HTTP responses for client requests.
 *
 * Example usage:
 *
 * <pre>{@code
 * Response response = new HttpResponse(req, res);
 * response.status(Response.REST_OK)
 *         .cacheControl(60 * 60 * 24 * 30 * 12) // cache for one year
 *         .setHeader("Content-Type", "text/html")
 *         .write("<html><body><h1>Hello, world!</h1></body></html>");
 * }</pre>
 */
public class HttpResponse implements Response {
    private final HttpServletRequest request;
    private final  HttpServletResponse response;
    private final PrintWriter out ;
    private final Gson gson;


    public HttpResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        this.out = this.response.getWriter();
        this.gson = new Gson();
    }
    @Override
    public Response status(int n) {
        response.setStatus(n);
        return this;
    }

    @Override
    public Response cacheControl(int maxAge) {
        response.setHeader("Cache-Control", "public, max-age=" + maxAge);
        return this;
    }

    @Override
    public Response lastModified(long last) {
        response.setDateHeader("Last-Modified", last);

        return this;
    }

    @Override
    public Response write(String msg) {
        out.print(msg);
        return this;
    }

    @Override
    public PrintWriter out() throws IOException {
        return response.getWriter();
    }

    @Override
    public void json(Object obj) {
        setHeader("Content-Type", "application/json");
        if (obj == null) write("{}");
        else write(gson.toJson(obj));
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    // TODO: review this function later
    @Override
    public void redirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (Exception exception) {
            System.out.println("[Error at redirect]: " + exception.getMessage());
        }
    }

    @Override
    public Response setHeader(String key, String value) {
        response.setHeader(key, value);
        return this;
    }

    @Override
    public Response setDateHeader(String name, long time) {
        response.setDateHeader(name, time);

        return this;
    }

    @Override
    public void jsp(String jspPage) throws Exception {
        String relativePath = "/WEB-INF/views/" + jspPage + ".jsp";
        String absolutePath = request.getServletContext().getRealPath("/") + relativePath;

        File f = new File(absolutePath);

        if(f.exists() && !f.isDirectory()) {
            request.getRequestDispatcher(relativePath).forward(request, response);
            return;
        }

        throw new NoSuchElementException("The jsp \"" + relativePath + "\"page does not exist :");
    }

    @Override
    public void sendError(int i, String message) {
        String path = (request.getPathInfo() == null)? "/":request.getPathInfo();
        ErrorMessageBuilder messageBuilder = new ErrorMessageBuilderImpl();
        DirectorErrorResponseBuilder director = new DirectorErrorResponseBuilder(messageBuilder);

        director.make(i, path, message);
        ErrorResponse errorResponse = messageBuilder.make();

        status(i);
        json(errorResponse);
    }
}
