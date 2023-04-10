package com.creativityfactory.swiftserver.persistence;

import com.creativityfactory.swiftserver.annotation.DataSource;
import com.creativityfactory.swiftserver.annotation.Id;
import com.creativityfactory.swiftserver.utils.IdUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link Persistence} interface that provides
 * default implementation for the CRUD operations using a DefaultDataSource instance.
 * This implementation is based on the default data source specified by the
 * {@link DataSource} annotation on the model class.
 *
 * <p>The class uses a file database to store and retrieve data. It assumes that
 * each entity has an ID field annotated with the {@link Id} annotation. If the
 * ID field is not present, the first field in the entity will be used as the ID.</p>
 *
 */
public class DefaultPersistence implements Persistence<Object> {
    private final Class<?> model;
    private final DefaultDataSource api;
    /**
     * Constructs a new DefaultPersistence instance with the given model class and API.
     * @param model the class of the model to be persisted.
     * @param api the API to be used for persistence operations.
     */
    public DefaultPersistence(Class<?> model, DefaultDataSource api) {
        this.model = model;
        this.api = api;
    }
    /**
     * Returns a list of all objects of the specified model type.
     * @return a list of all objects of the specified model type, or null if an exception occurs.
     */
    @Override
    public List<Object> getAll() {
        try {
            return api.loadDB(model);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a list of objects of the specified model type, up to the given limit.
     *
     * @param limit the maximum number of objects to return.
     * @return a list of up to 'limit' objects of the specified model type, or null if an exception occurs.
     */
    @Override
    public List<Object> getLimit(Integer limit) {
        try {
            List<Object> objectList =  api.loadDB(model);
            List<Object> list = new ArrayList<>();
            int index = 0;
            while (index < limit && index < objectList.size())
                list.add(objectList.get(index++));

            return list;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
    /**
     * Returns the object with the specified ID of the specified model type, or null if it does not exist.
     *
     * @param id the ID of the object to be returned.
     * @return the object with the specified ID of the specified model type, or null if it does not exist.
     */
    @Override
    public Object getById(Object id) {
        try {
            List<Object> list = api.loadDB(model);
            for (Object object: list) {
                Object _id = IdUtils.extractIdValue(object);
                if ((_id != null) && _id.equals(id)) return object;
            };

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Saves the specified object in the data store if it does not already exist.
     *
     * @param o the object to be saved.
     * @return the saved object if the operation is successful, or null if it already exists or an exception occurs.
     */
    @Override
    public Object save(Object o) {
        try {
            Object id = IdUtils.extractIdValue(o);
            List<Object> list = api.loadDB(model);
            for (Object o1: list) {
                Object _id = IdUtils.extractIdValue(o1);
                if ((_id != null) && (_id.equals(id))) return null;
            }

            list.add(o);
            if (api.saveDB(list, model)) return o;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the specified object in the data store if it already exists.
     * @param o the object to be updated.
     * @return the updated object if the operation is successful, or null if it does not exist or an exception occurs.
     */
    @Override
    public Object update(Object o) {
        try {
            Object id = IdUtils.extractIdValue(o);
            List<Object> list = api.loadDB(model);
            for (Object o1: list) {
                Object _id = IdUtils.extractIdValue(o1);
                if ((_id != null) && (_id.equals(id))) {
                    list.remove(o1);
                    list.add(o);

                    return api.saveDB(list, model)? o: null;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Delete the specified object in the data store if it already exists.
     * @param o the object to be deleted.
     * @return the deleted object if the operation is successful, or null if it does not exist or an exception occurs.
     */
    @Override
    public Object delete(Object o) {
        try {
            Object id = IdUtils.extractIdValue(o);
            List<Object> list = api.loadDB(model);
            for (Object o1: list) {
                Object _id = IdUtils.extractIdValue(o1);
                if ((_id != null) && (_id.equals(id))) {
                    list.remove(o1);
                    api.saveDB(list, model);

                    return o1;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
