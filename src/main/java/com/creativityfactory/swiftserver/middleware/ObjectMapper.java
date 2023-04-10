package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.annotation.BelongTo;
import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.persistence.SingletonDataSource;
import com.creativityfactory.swiftserver.utils.FieldUtils;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.request.Request;
import com.creativityfactory.swiftserver.response.Response;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * This middleware is for transforming incoming data from the id of other resources to theirs real objects.
 */
public class ObjectMapper implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");
        Gson gson = (Gson) req.getAttribute("gson");

        String jsonBody = req.jsonBody();
        // map object to id ....
        // deserialize thw incoming json
        Map<String, Object> deserializedJson;
        try {
            TypeToken<Map<String, Object>> typeToken = new TypeToken<>() {};
            deserializedJson = gson.fromJson(req.jsonBody(), typeToken);
        } catch (Exception exception) {
            exception.printStackTrace();

            res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "Malformed json format");
            return;
        }

        List<Field> fields = FieldUtils.getAllFields(model);
        for (Field field: fields) {
            String fieldName = (field.isAnnotationPresent(SerializedName.class)) ? field.getAnnotation(SerializedName.class).value() : field.getName();
            // check if this field or the id field of an object is Integer
            if ((
                    (field.getType() == Integer.class) || (IdUtils.getIdTypeFromModel(field.getType()) == Integer.class))
                    && deserializedJson.containsKey(fieldName) && deserializedJson.get(fieldName).getClass() == Double.class)
            {
                Double doubleValue = (Double) deserializedJson.get(fieldName);
                Integer integerValue = doubleValue.intValue();
                deserializedJson.put(fieldName, integerValue);
            }
            if(field.isAnnotationPresent(BelongTo.class)){
                try {
                    Persistence<Object> dataSrc = SingletonDataSource.getInstance(field.getType());

                    if (deserializedJson.containsKey(fieldName)) {
                        Object belongToObjectId = deserializedJson.get(fieldName);
                        // checking the type of id that for replacing it with an exist object or not
                        if (IdUtils.getIdTypeFromModel(field.getType()) == belongToObjectId.getClass()) {
                            deserializedJson.put(fieldName, dataSrc.getById(deserializedJson.get(fieldName)));
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();

                    res.sendError(Response.REST_INTERNAL_SERVER_ERROR, "try later");
                    return;
                }
            }
        }

        req.setBody(gson.toJson(deserializedJson));
        req.next(true);
    }
}
