package com.creativityfactory.swiftserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to mark the field in a model class that represents the unique identifier
 *  * for the object. If the {@code @Id} annotation is not used on any field in the model class, then
 *  * the first field of the class will be used as the ID. This annotation can be used in conjunction
 *  * with the {@code @Rest} annotation to create REST APIs from the model.</p>
 * <p>Example usage:</p>
 * <pre>{@code
 * @Rest
 * public User {
 *     @Id
 *     private String id;
 *     private String name;
 *     // ...
 * }
 * }</pre>
 * @see Rest
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}
