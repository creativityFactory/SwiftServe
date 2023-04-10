package com.creativityfactory.swiftserver.utils;

import com.creativityfactory.swiftserver.annotation.BelongTo;
import com.creativityfactory.swiftserver.annotation.HasMany;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.creativityfactory.swiftserver.annotation.Id;
import com.creativityfactory.swiftserver.persistence.Persistence;
import com.creativityfactory.swiftserver.persistence.SingletonDataSource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for all the operation related to the id of models, such extracting the value of an
 * id from an object of specific model, type of id...
 */
public class IdUtils {
    /**
     * Extracts the ID value from an object by accessing the first field annotated with the @Id annotation.
     *
     * @param obj the object from which to extract the ID value
     * @return the ID value extracted from the object
     * @throws Exception if the field cannot be accessed or if the class has no field
     */
    public static Object extractIdValue(Object obj) throws Exception {
        List<Field> fields = FieldUtils.getAllFields(obj.getClass());
        if(fields.size() == 0) return new Exception("Cannot extract id from an empty class");
        fields.get(0).setAccessible(true);
        Object id = fields.get(0).get(obj);
        fields.get(0).setAccessible(false);
        boolean flag = false;
        for (Field field:fields) {
            if(!field.canAccess(obj)){
                field.setAccessible(true);
                flag = true;
            }

            if(field.isAnnotationPresent(Id.class)){
                id = field.get(obj);
            }
            if(flag)
                field.setAccessible(false);
        }
        return id;
    }

    /**
     * Given a model class, this method returns the type of its ID field.
     *
     * @param clazz the class of the model
     * @return the type of the ID field, or null if the class doesn't have an ID field
     *
     */
    public static Class<?> getIdTypeFromModel(Class<?> clazz) {
        Field idField = getIdField(clazz);

        if (idField == null) return null;

        return idField.getType();
    }
    /**
     * <p>Returns the ID field of a given class annotated with the {@link Id} annotation.</p>
     * <p>If no field is annotated with {@link Id}, returns the first field in the class.</p>
     *
     * @param clazz the class to retrieve the ID field from
     * @return the ID field of the given class, or null if the class has no fields
     */
    public static Field getIdField(Class<?> clazz) {
        Field[] fields = FieldUtils.getAllFields(clazz).toArray(new Field[0]);
        if(fields.length == 0) return null;

        for (Field field: fields) {
            if (field.isAnnotationPresent(Id.class)) return field;
        }

        return fields[0];
    }

    /**
     * Convert the id from String to its compatible type.
     *
     * @param model   The Class object representing the model class that contains the id field.
     * @param paramId The id value as a string.
     * @return The id value converted to its compatible type. Returns null if either the model or paramId is null.
     * @throws NumberFormatException If the id value cannot be converted to an integer (if the id type is an integer).
     */
    public static Object convertId(Class<?> model, String paramId) {
        if (model == null || paramId == null) return null;

        Object id = null;
        try {
            Class<?> idType = getIdTypeFromModel(model);
            if (idType == Integer.class) {
                id = Integer.parseInt(paramId.trim());
            } else {
                id = paramId;
            }
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("Cannot convert the string to an integer.");
        }

        return id;
    }

    /**
     * Maps an object's ID field to its corresponding value in a JSON string, and converts Integer fields from Double to Integer.
     *
     * @param json a JSON string representing an object, which must contain the ID field value.
     * @param clazz the class of the object that the JSON string represents.
     * @return a JSON string representing the same object, but with Integer fields properly converted and BelongTo fields replaced with their referenced objects.
     */
    public static String mapObjectToId(String json, Class<?> clazz) {
        Gson gson = new Gson();
        List<Field> fields = FieldUtils.getAllFields(clazz);
        // deserialize thw incoming json
        TypeToken<Map<String, Object>> typeToken = new TypeToken<>() {};
        Map<String, Object> deserializedJson = gson.fromJson(json, typeToken);

        for (Field field: fields) {
            String fieldName = (field.isAnnotationPresent(SerializedName.class)) ? field.getAnnotation(SerializedName.class).value() : field.getName();
            // check if this field or the id field of an object is Integer
            if ((
                    (field.getType() == Integer.class) || (getIdTypeFromModel(field.getType()) == Integer.class))
                    && deserializedJson.containsKey(fieldName))
            {
                Double doubleValue = (Double) deserializedJson.get(fieldName);
                Integer integerValue = doubleValue.intValue();
                deserializedJson.put(fieldName, integerValue);
            }
            if(field.isAnnotationPresent(BelongTo.class)){
                try {
                    Persistence<Object> dataSrc = SingletonDataSource.getInstance(field.getType());

                    if (deserializedJson.containsKey(fieldName)) {
                        deserializedJson.put(fieldName, dataSrc.getById(deserializedJson.get(fieldName)));
                    }
                } catch (Exception exception) {
                    System.out.println("[mapObjectToId ]: ");
                    exception.printStackTrace();
                }
            }
        }
        return gson.toJson(deserializedJson);
    }

    /**
     * <p>Converts an object to a map, where keys are the names of the object's fields and the values are the corresponding field values.</p>
     * <p>It only maps the fields that do not have the annotations BelongTo or HasMany.</p>
     * <p>If a field has the BelongTo annotation, the corresponding value in the map will be the ID of the related object.</p>
     *
     * @param obj The object to be mapped
     * @param clazz The class of the object to be mapped
     * @return A map representing the object with its fields and values
     */
    public static Map<String, Object> mapIdToObject(Object obj , Class<?> clazz){
        if (obj == null) return null;
        Gson gson = new Gson();
        List<Field> fields = FieldUtils.getAllFields(clazz);
        Map<String, Object> serializedObject = new HashMap<>();
        boolean flag = false;
        String fieldName;
        for (Field field : fields) {

            if (!field.canAccess(obj)){
                field.setAccessible(true);
                flag = true;
            }

            fieldName = (field.isAnnotationPresent(SerializedName.class)) ? field.getAnnotation(SerializedName.class).value() : field.getName();
            try {
                if (!field.isAnnotationPresent(BelongTo.class) && !field.isAnnotationPresent(HasMany.class)) {
                    serializedObject.put(fieldName, field.get(obj));
                }
                else if (field.isAnnotationPresent(BelongTo.class)){
                    serializedObject.put(fieldName, extractIdValue(field.get(obj)));
                }
            } catch (Exception exception){
                System.out.println("[mapIdToObject ]: " + exception.getMessage());
            }
            if (flag) field.setAccessible(false);
        }

        return serializedObject;
    }
}
