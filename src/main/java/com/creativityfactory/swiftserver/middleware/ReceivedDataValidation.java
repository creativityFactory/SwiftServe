package com.creativityfactory.swiftserver.middleware;

import com.creativityfactory.swiftserver.annotation.HasMany;
import com.creativityfactory.swiftserver.annotation.UnRequired;
import com.creativityfactory.swiftserver.app.HttpRequestHandler;
import com.creativityfactory.swiftserver.response.Response;
import com.creativityfactory.swiftserver.utils.FieldUtils;
import com.creativityfactory.swiftserver.utils.IdUtils;
import com.google.gson.JsonSyntaxException;
import com.creativityfactory.swiftserver.request.Request;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This middleware is for the validation the incoming data.
 */
public class ReceivedDataValidation implements HttpRequestHandler {
    @Override
    public void method(Request req, Response res) {
        Class<?> model = (Class<?>) req.getAttribute("model");

        List<Field> fields = FieldUtils.getAllFields(model);

        try {
            Object receivedData = req.body(model);
            Field idField = IdUtils.getIdField(model);
            String method = req.method().toLowerCase();
            for (Field field : fields) {
                boolean accessRight = field.canAccess(receivedData);
                field.setAccessible(true);

                if ((method.equals("put") && field.equals(idField))
                        || (method.equals("patch") && field.get(receivedData) == null)
                ) continue;

                if (!field.isAnnotationPresent(UnRequired.class) && !field.isAnnotationPresent(HasMany.class)) {


                    if (field.get(receivedData) == null) {
                        String fieldName = field.getName();
                        if (field.isAnnotationPresent(SerializedName.class)) fieldName = field.getAnnotation(SerializedName.class).value();
                        throw new Exception("The field " + fieldName + " is required");
                    }
                }

                field.setAccessible(accessRight);
            }

        } catch (Exception exception) {
            exception.printStackTrace();

            String message = (exception.getClass() == JsonSyntaxException.class)? "unmatched type": exception.getMessage();
            res.sendError(Response.REST_BAD_REQUEST, message);
            return;
        }


        req.next(true);
    }
}
