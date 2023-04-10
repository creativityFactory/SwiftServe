package com.creativityfactory.swiftserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a model should use a specific data source for its storage. If the value is not specified,
 * the default data source, which is the file database, will be used. The value should match the id of a data
 * source defined using the {@link DataSource} annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromDataSource {
    /**
     * The id of the data source to be used by the model. If not specified, the default data source will be used.
     * The value should match the id of a data source defined using the {@link DataSource} annotation.
     */
    public String value() default "";
}