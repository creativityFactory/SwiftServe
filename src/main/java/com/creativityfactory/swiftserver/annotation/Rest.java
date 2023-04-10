package com.creativityfactory.swiftserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this class will be used to automatically generate a REST API for a model, using the data source
 * specified in the {@link FromDataSource} annotation. The generated API will include standard CRUD endpoints
 * for the model, as well as additional endpoints for querying and filtering the data. This annotation should be
 * used in conjunction with the {@link FromDataSource} annotation to specify the data source for the API.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rest {}