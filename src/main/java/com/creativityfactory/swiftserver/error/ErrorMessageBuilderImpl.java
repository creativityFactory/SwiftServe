package com.creativityfactory.swiftserver.error;

import java.util.Date;
/**
 * <p>
 *  This class implements the {@link ErrorMessageBuilder} interface and provides a concrete implementation
 *  of building the {@link ErrorResponse} object using the builder pattern.
 *  The builder methods allow setting the timestamp, status code, error message, message, and path fields
 *  of the ErrorResponse object. The reset method allows resetting the builder state to start building
 *  a new ErrorResponse object.
 * </p>
 */
public class ErrorMessageBuilderImpl implements ErrorMessageBuilder {
    private ErrorResponse errorResponse;
    public ErrorMessageBuilderImpl() {
        this.errorResponse = new ErrorResponse();
    }
    @Override
    public ErrorMessageBuilder buildTimestamp(Date timestamp) {
        errorResponse.setTimestamp(timestamp);
        return this;
    }

    @Override
    public ErrorMessageBuilder buildStatusCode(int code) {
        errorResponse.setStatus(code);
        return this;
    }

    @Override
    public ErrorMessageBuilder buildError(String error) {
        errorResponse.setError(error);
        return this;
    }

    @Override
    public ErrorMessageBuilder buildMessage(String message) {
        errorResponse.setMessage(message);
        return this;
    }

    @Override
    public ErrorMessageBuilder buildPath(String path) {
        errorResponse.setPath(path);
        return this;
    }

    @Override
    public ErrorMessageBuilder reset() {
        this.errorResponse = new ErrorResponse();
        return this;
    }

    @Override
    public ErrorResponse make() {
        return errorResponse;
    }
}
