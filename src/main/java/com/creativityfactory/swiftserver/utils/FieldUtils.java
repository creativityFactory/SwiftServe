package com.creativityfactory.swiftserver.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility methods for working with fields using reflection.
 */
public class FieldUtils {

    /**
     * Returns all the fields of a class including the inherited ones using reflection.
     *
     * @param clazz The class to retrieve fields from.
     * @return A List of Field objects representing all the fields of the given class.
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        if (clazz.getSuperclass() == Object.class) {
        // If the superclass is Object, then we only need to get the declared fields of the given class.
            return new ArrayList<>(List.of(clazz.getDeclaredFields()));
        }

        // Recursively get all the fields of the superclass and add them to the list.
        List<Field> fields = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        // Add the declared fields of the given class to the list.
        fields.addAll(new ArrayList<>(List.of(clazz.getDeclaredFields())));
        return fields;
    }
    /**
     * Updates the fields of an old object with the fields of a new object of the same class.
     *
     * @param oldObj the old object to update
     * @param newObj the new object containing the updated field values
     * @return the updated old object or null if the classes of the old and new objects do not match
     */
    public static Object updateFields(Object oldObj, Object newObj) {
        if (oldObj.getClass() != newObj.getClass()) return null;

        Class<?> model = oldObj.getClass();
        Field[] fields = model.getDeclaredFields();
        Field idField = IdUtils.getIdField(model);
        for (Field field: fields) {
            if (!field.equals(idField)) {
                try {
                    field.setAccessible(true);
                    if (field.get(newObj) != null) {
                        field.set(oldObj, field.get(newObj));
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return null;
                }
            }
        }

        return oldObj;
    }
}