package com.creativityfactory.swiftserver.error;

import java.util.Date;

/**
 * A class representing an HTTP error message to be sent to the client.
 */
public class ErrorResponse {

    /**
     * The timestamp of the error.
     */
    private Date timestamp;
    /**
     * The status code of the error.
     */
    private int status;
    /**
     * The type of the error.
     */
    private String error;
    /**
     * The error message.
     */
    private String message;
    /**
     * The path that caused the error.
     */
    private String path;
    public ErrorResponse() {}

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
