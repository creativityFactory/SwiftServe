package com.creativityfactory.swiftserver.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.creativityfactory.swiftserver.utils.IOUtils;
import com.creativityfactory.swiftserver.utils.IdUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultDataSource class provides an API for persistence list objects of a class in JSON format, and also for reading and writing data.
 * It is a singleton class, and an instance can be obtained using the getInstance() method.
 */
public class DefaultDataSource {
    private static DefaultDataSource instance;

    /**
     * Returns an instance of the DefaultDataSource class.
     * @return an instance of the DefaultDataSource class
     */
    public static DefaultDataSource getInstance() {
        if (instance == null) instance = new DefaultDataSource(new Gson());

        return instance;
    }

    private final Gson gson;

    /**
     * Constructs an instance of the DefaultDataSource class with the specified Gson object.
     * @param gson the Gson object to use for serialization and deserialization
     */
    private DefaultDataSource(Gson gson) {
        this.gson = gson;
    }

    /**
     * Loads the data from the JSON file corresponding to the specified model class.
     *
     * @param model the model class for which data is to be loaded
     * @return a list of objects of the specified model class, loaded from the corresponding JSON file
     * @throws IOException if an I/O error occurs while reading the JSON file
     */
    public List<Object> loadDB(Class<?> model) throws IOException {
        try {
            String db = IOUtils.readFile(model.getName() + ".json");


            TypeToken<List<String>> mapType = new TypeToken<>(){};
            List<String> list = gson.fromJson(db, mapType);

            List<Object> objectList = new ArrayList<>();
            for (String str: list) {
                objectList.add(gson.fromJson(IdUtils.mapObjectToId(str, model), model));
            }

            return objectList;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Saves the specified list of objects to the JSON file corresponding to the specified model class.
     *
     * @param list the list of objects to be saved
     * @param model the model class for which data is to be saved
     * @return true if the data was successfully saved to the corresponding JSON file, false otherwise
     */
    public boolean saveDB(List<Object> list, Class<?> model) {
        try {
            List<String> db = new ArrayList<>();

            for (Object obj: list) {
                db.add(gson.toJson(IdUtils.mapIdToObject(obj, model)));
            }


            IOUtils.saveDataToFile(gson.toJson(db), model.getName() + ".json");
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }
}
