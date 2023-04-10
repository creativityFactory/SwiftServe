package com.creativityfactory.swiftserver.persistence;

import com.creativityfactory.swiftserver.client.dao.StudentDaoMemory;
import com.creativityfactory.swiftserver.client.models.Student;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class DefaultDataSourceTest {
    DefaultDataSource defaultDataSource = DefaultDataSource.getInstance();

    DefaultDataSourceTest() throws IOException {

    }

    @Test
    void test() throws Exception {

        Map<String, Class<? extends Persistence<?>>> dtSrcMap = new HashMap<>();
        // map data sources instances with its name
        dtSrcMap.put("studentSrc", StudentDaoMemory.class);

        // initialize
        SingletonDataSource.init(dtSrcMap);
        //- -------------------
        Persistence<Object> persistence2 = SingletonDataSource.getInstance(Student.class);

        Object object = persistence2.getById(1);
        Object student = new Student(1, "john doe");

        assertEquals(student, object);
    }

}