package com.creativityfactory.swiftserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation used to indicate that a field represents a relationship of belonging to another class.
 * This annotation is typically used by our frameworks to define the relationship
 * between two classes for purpose of automating the creation of web services. The default value is
 * true, meaning that the field belongs to the other class.</p>
 * <p>Example usage:</p>
 * <pre>{@code
 * // The User class belongs to the Role class
 * public class User {
 *     ...
 *     @BelongTo
 *     private Role role;
 *     ...
 * }
 * // The Role class has many Users
 * public class Role {
 *     ...
 *     @HasMany
 *     private List<User> users;
 *     ...
 * }
 * }</pre>
 * @see HasMany
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BelongTo {
    /**
     * Indicates whether the annotated field belongs to the other class (true) or not (false).
     * The default value is true.
     */
    public boolean value() default true;
}