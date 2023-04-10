package com.creativityfactory.swiftserver.error;

import com.creativityfactory.swiftserver.response.Response;

import java.util.Calendar;

/**
 * This class is responsible for constructing an {@link ErrorResponse} object using the builder pattern
 * and setting default values for the error field based on the given status code.
 */
public class DirectorErrorResponseBuilder {

    private ErrorMessageBuilder messageBuilder;

    /**
     * Creates a new instance of DirectorErrorResponseBuilder with a given ErrorMessageBuilder.
     *
     * @param messageBuilder the implementation of ErrorMessageBuilder to use
     */
    public DirectorErrorResponseBuilder(ErrorMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    /**
     * Builds an ErrorResponse object with the given parameters and default values for the error field
     * based on the given status code.
     *
     * @param code the HTTP status code
     * @param path the request path
     * @param message the error message
     */
    public void make(int code, String path, String message) {
        messageBuilder.reset()
                .buildTimestamp(Calendar.getInstance().getTime())
                .buildStatusCode(code)
                .buildMessage(message)
                .buildPath(path);

        switch (code) {
            case Response.REST_BAD_REQUEST:
                messageBuilder.buildError("Bad request");
                break;
            case Response.REST_NOT_FOUND:
                messageBuilder.buildError("Not found");
                break;
            case Response.REST_INTERNAL_SERVER_ERROR:
                messageBuilder.buildError("Internal server error");
                break;
        }
    }
}
