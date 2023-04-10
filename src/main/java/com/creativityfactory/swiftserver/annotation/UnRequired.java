package com.creativityfactory.swiftserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Indicates that a field is unrequired for a model.This annotation is used to mark that a
 * particular field in the model class is not required for the successful creation of an instance.
 * If a field has this annotation, the client can omit it from the request without resulting in an error.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * @REST
 * public class Book {
 *      private String id;
 *      //...
 *      @UnRequired
 *      private String publisher;
 * }
 * }</pre>
 * <p>In this example, the "publisher" field is marked as unrequired. If a client makes a request
 * to create a new book, they can choose to omit the publisher field from the request
 * without causing an error.</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnRequired{}