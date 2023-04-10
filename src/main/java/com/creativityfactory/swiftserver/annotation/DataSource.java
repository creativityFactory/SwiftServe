package com.creativityfactory.swiftserver.annotation;

import com.creativityfactory.swiftserver.persistence.Persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Indicates that a class will be used as a data source, and the value specifies the ID for this
 * data source. Classes annotated with this annotation can be used to specify the source of data
 * for a component, service, or application. </p>
 *
 * <p>Every class use this annotation must implement the
 * {@link Persistence} interface.</p>
 *
 * <p>Example usage:</p>
 *
 * <pre>{@code
 *  @DataSource("dtSrcYourType")
 *  public class MyDataSource implements Persistence<YourType> {
 *      // ...
 *  }
 * }</pre>
 * @see Persistence
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    /**
     * The ID of the data source.
     * @return the ID of the data source
     */
    String value() default "";
}




