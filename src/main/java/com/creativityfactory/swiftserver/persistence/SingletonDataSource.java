package com.creativityfactory.swiftserver.persistence;

import com.creativityfactory.swiftserver.annotation.FromDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for creating instance of datasource following the pattern Singleton.
 */
public class SingletonDataSource {
    private static Map<String, Class<? extends Persistence<?>>> dataSourceClassesMap = new HashMap<>();
    private static final Map<String, Persistence<Object>> instances = new HashMap<>();
    private static final Map<String, Persistence<Object>> dfDataSrcInstances = new HashMap<>();

    /**
     * This class initialize the creation of single instance for each identify, which it will be used later
     * to get instance by the given class, where it will identify by the value present in {@code @FromDataSource}.
     * This helps in the automation of creation of REST-APIs, exactly for user-defined data sources
     *
     * @param dtSrcMap Map of identify-class where will create a single instance for each identify
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void init(Map<String, Class<? extends Persistence<?>>> dtSrcMap) throws InstantiationException, IllegalAccessException {
        dataSourceClassesMap = dtSrcMap;

        // creating instance for data sources
        for (Map.Entry<String, Class<? extends Persistence<?>>> map: dataSourceClassesMap.entrySet()) {
            Persistence<?> instance = map.getValue().newInstance();
            instances.put(map.getKey(), (Persistence<Object>) instance);
        }
    }

    /**
     * Returns instance of persistence object for the given class. This class must be annotated with {@code @FromDataSource}
     *
     * @param model the given class which you want to get a persistence object of it.
     * @return instance of persistence object for the given class
     * @throws InstantiationException when attempt to get an instance of persistence for a class
     * does not annotated with {@code @FromDataSource}
     * @throws IllegalAccessException
     */
    public static Persistence<Object> getInstance(Class<?> model) throws InstantiationException, IllegalAccessException {
        if (!model.isAnnotationPresent(FromDataSource.class))
            throw new IllegalArgumentException("Can not get instance of datasource for a model does not annotated with FromDataSource");
        String fromDataSource = model.getAnnotation(FromDataSource.class).value();
        if (fromDataSource.isEmpty()) {
            dfDataSrcInstances.put(model.getName(), new DefaultPersistence(model, DefaultDataSource.getInstance()));

            return dfDataSrcInstances.get(model.getName());
        }

        if (instances.get(fromDataSource) == null) {
            Class<? extends Persistence<?>> dataSourceClass = dataSourceClassesMap.get(fromDataSource);
            instances.put(fromDataSource, (Persistence<Object>) dataSourceClass.newInstance());
        }

        return instances.get(fromDataSource);
    }
}
