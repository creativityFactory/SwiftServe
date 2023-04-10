package com.creativityfactory.swiftserver.persistence;

import java.util.List;

/**
 * This interface defines the methods for persistence operations on a given model class.
 * The creation of rest api using annotation {@code @REST} is based on this interface to deal with persistence
 *
 * @param <T> the type of the model class to persist
 */
public interface Persistence<T> {

    /**
     * Returns a list with all instances of the model class currently persisted.
     *
     * @return a list with all instances of the model class currently persisted
     */
    List<T> getAll();

    /**
     * Returns a limited list with instances of the model class currently persisted.
     *
     * @param limit the maximum number of instances to retrieve
     * @return a limited list with instances of the model class currently persisted
     */
    List<T> getLimit(Integer limit);

    /**
     * Returns the instance of the model class identified by the given ID.
     *
     * @param id the ID of the instance to retrieve
     * @return the instance of the model class identified by the given ID, or null if not found
     */
    T getById(Object id);

    /**
     * Saves a new instance of the model class to the persistence layer.
     *
     * @param t the instance of the model class to save
     * @return the saved instance of the model class
     */
    T save(T t);

    /**
     * Updates an existing instance of the model class in the persistence layer.
     *
     * @param t the instance of the model class to update
     * @return the updated instance of the model class
     */
    T update(T t);

    /**
     * Deletes an existing instance of the model class from the persistence layer.
     *
     * @param t the instance of the model class to delete
     * @return the deleted instance of the model class
     */
    T delete(T t);
}

