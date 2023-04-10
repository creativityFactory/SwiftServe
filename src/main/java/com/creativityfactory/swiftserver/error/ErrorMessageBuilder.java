package com.creativityfactory.swiftserver.error;

import java.util.Date;

/**
 *  <p>An interface used to implement the builder pattern for building an {@link ErrorResponse}.
 *
 *  The {@code ErrorMessageBuilder} provides methods for building the various fields of an {@code ErrorResponse} object,
 *
 *  allowing for a more flexible and readable way to create instances of this class. It also provides a method to reset
 *
 *  the builder's internal state and start over. </p>
 *
 *  <p>Example usage:</p>
 *
 *  <pre>{@code
 *  ErrorMessageBuilder builder = new MyErrorMessageBuilder();
 *
 *  ErrorResponse errorResponse = builder
 *  .buildTimestamp(new Date())
 *  .buildStatusCode(404)
 *  .buildError("Not Found")
 *  .buildMessage("The requested resource was not found.")
 *  .buildPath("/api/users")
 *  .make();
 *  }</pre>
 *  Note that the order in which the methods are called does not matter, as long as all necessary fields are set before calling
 *  the {@link #make()} method to create the {@code ErrorResponse} object.
 */
public interface ErrorMessageBuilder {

    /**
     * Sets the timestamp field of the {@link ErrorResponse} to the specified date.
     * @param timestamp the date to set as the timestamp
     * @return the builder instance for method chaining
     */
    ErrorMessageBuilder buildTimestamp(Date timestamp);
    /**
     * Sets the status code field of the {@link ErrorResponse} to the specified code.
     * @param code the HTTP status code to set
     * @return the builder instance for method chaining
     */
    ErrorMessageBuilder buildStatusCode(int code);
    /**
     * Sets the error field of the {@link ErrorResponse} to the specified error message.
     * @param error the error message to set
     * @return the builder instance for method chaining
     */
    ErrorMessageBuilder buildError(String error);
    /**
     * Sets the message field of the {@link ErrorResponse} to the specified message.
     * @param message the message to set
     * @return the builder instance for method chaining
     */
    ErrorMessageBuilder buildMessage(String message);
    /**
     * Sets the path field of the {@link ErrorResponse} to the specified path.
     * @param path the path to set
     * @return the builder instance for method chaining
     */
    ErrorMessageBuilder buildPath(String path);
    /**
     * Resets the builder's internal state to its initial values.
     * @return the builder instance for method chaining
     */
    ErrorMessageBuilder reset();
    /**
     * Builds and returns a new instance of {@link ErrorResponse} using the fields set on this builder.
     * @return a new instance of {@link ErrorResponse}
     */
    ErrorResponse make();
}