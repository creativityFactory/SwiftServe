package com.creativityfactory.swiftserver.utils;


import com.creativityfactory.swiftserver.client.models.Student;
import com.creativityfactory.swiftserver.client.models.Subject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdUtilsTest {

    // Suite of test for convertId function
    @Test
    void convertIdParamToInteger() {
        assertEquals(1, IdUtils.convertId(Student.class, "1"));
    }
    @Test
    void convertIdParamWithPaddingSpaceToInteger() {
        assertEquals(1, IdUtils.convertId(Student.class, "  1 "));
    }
    @Test
    void convertIdParamStringToInteger() {
        Throwable exception = assertThrows(NumberFormatException.class, () -> IdUtils.convertId(Student.class, "I am a string"));
        assertEquals("Cannot convert the string to an integer.", exception.getMessage());
    }
    @Test
    void convertIdParamStringToString() {
        assertEquals("C++", IdUtils.convertId(Subject.class, "C++"));
    }
    // Suite of test for getIdTypeFromModel function
    @Test
    void getIdTypeFromModelWithIntegerTypeIdWithConvention() {
        assertSame(Integer.class, IdUtils.getIdTypeFromModel(Student.class));
    }
    @Test
    void getIdTypeFromModelWithStringTypeIdWithAnnotation() {
        assertSame(String.class, IdUtils.getIdTypeFromModel(Subject.class));
    }
}