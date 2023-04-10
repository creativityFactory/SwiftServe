package com.creativityfactory.swiftserver.utils;

import com.creativityfactory.swiftserver.client.models.Wife;
import com.creativityfactory.swiftserver.client.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FieldUtilsTest {
    @Test
    void getAllFieldsFromParentToChildClassesOfWifeClassThatInheritFromPerson() {
        List<Field> fields = new ArrayList<>(List.of(Person.class.getDeclaredFields()));
        fields.addAll(new ArrayList<>(List.of(Wife.class.getDeclaredFields())));

        for (Field field:FieldUtils.getAllFields(Wife.class)) {
            System.out.println(field.getName());
        }

        assertArrayEquals(fields.toArray(), FieldUtils.getAllFields(Wife.class).toArray());
    }
    // update object
    private class Man {
        private int id;
        private String name;
        private Date birthDate;

        public Man(int id, String name, Date birthDate) {
            this.id = id;
            this.name = name;
            this.birthDate = birthDate;
        }
    }

    private class Employee {
        private int id;
        private String name;
        private Date birthDate;
        private double salary;

        public Employee(int id, String name, Date birthDate, double salary) {
            this.id = id;
            this.name = name;
            this.birthDate = birthDate;
            this.salary = salary;
        }
    }

    private Man oldMan;
    private Man newMan;
    private Employee oldEmployee;
    private Employee newEmployee;
    private Date birthDate;
    private final double SALARY = 50000.0;

    @BeforeEach
    public void setUp() throws Exception {
        birthDate = new Date();
        oldMan = new Man(1, "Alice", birthDate);
        newMan = new Man(1, "Alice Smith", birthDate);
        oldEmployee = new Employee(1, "Bob", birthDate, SALARY);
        newEmployee = new Employee(1, "Bob Smith", birthDate, SALARY);
    }

    @Test
    public void testUpdateObjectReturnsNullWhenClassesAreDifferent() {
        Object result = FieldUtils.updateFields(oldMan, oldEmployee);
        assertNull(result);
    }

    @Test
    public void testUpdateObjectUpdatesFieldsOfSameClass() {
        Object result = FieldUtils.updateFields(oldMan, newMan);
        assertNotNull(result);
        assertTrue(result instanceof Man);
        assertEquals(newMan.name, ((Man) result).name);

        result = FieldUtils.updateFields(oldEmployee, newEmployee);
        assertNotNull(result);
        assertTrue(result instanceof Employee);
        assertEquals(newEmployee.name, ((Employee) result).name);
    }

    @Test
    public void testUpdateObjectDoesNotUpdateIdField() {
        Object result = FieldUtils.updateFields(oldMan, newMan);
        assertNotNull(result);
        assertTrue(result instanceof Man);
        assertEquals(oldMan.id, ((Man) result).id);

        result = FieldUtils.updateFields(oldEmployee, newEmployee);
        assertNotNull(result);
        assertTrue(result instanceof Employee);
        assertEquals(oldEmployee.id, ((Employee) result).id);
    }

    @Test
    public void testUpdateObjectDoesNotUpdateFieldWithNullValue() {
        newMan.name = null;
        Object result = FieldUtils.updateFields(oldMan, newMan);
        assertNotNull(result);
        assertTrue(result instanceof Man);
        assertEquals(oldMan.name, ((Man) result).name);

        newEmployee.name = null;
        result = FieldUtils.updateFields(oldEmployee, newEmployee);
        assertNotNull(result);
        assertTrue(result instanceof Employee);
        assertEquals(oldEmployee.name, ((Employee) result).name);
    }
}