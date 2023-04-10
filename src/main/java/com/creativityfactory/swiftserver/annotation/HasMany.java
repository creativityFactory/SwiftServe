package com.creativityfactory.swiftserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Indicates that the annotated field represents a "has many" relationship with another class.
 * This annotation is typically used by our frameworks to define the relationship between
 * two classes for purpose of automating the creation of web services.</p>
 *
 * <p>Example usage:</p>
 *
 * <pre>{@code
 * public class Customer {
 *   // ...
 *   @HasMany
 *   private List<Order> orders;
 *   // ...
 * }
 * }</pre>
 *
 * @see BelongTo
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasMany {}