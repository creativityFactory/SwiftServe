package com.creativityfactory.swiftserver.client.dao;

import com.creativityfactory.swiftserver.annotation.DataSource;
import com.creativityfactory.swiftserver.client.models.Student;
import com.creativityfactory.swiftserver.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

@DataSource("studentSrc")
public class StudentDaoMemory implements Persistence<Student> {
    private static final List<Student> students = new ArrayList<>();
    // add sample data
    public StudentDaoMemory() {
        students.add(new Student(1, "john doe"));
        students.add(new Student(2, "maria Kartner"));
        students.add(new Student(3, "marouane Benabdelkader"));
    }
    @Override
    public List<Student> getAll() {
        return students;
    }

    @Override
    public List<Student> getLimit(Integer limit) {
        return null;
    }

    @Override
    public Student getById(Object o) {
        Integer id = (Integer) o;
        for (Student student: students) {
            if (id.equals(student.getId())) return student;
        }
        return null;
    }

    @Override
    public Student save(Student student) {
        if (getById(student.getId()) != null) {
            students.add(student);

            return student;
        }

        return null;
    }

    @Override
    public Student update(Student student) {
        Student existStudent = getById(student.getId());
        if (student.getId() == null) return null;

        existStudent.setName(student.getName());
        existStudent.setNotes(student.getNotes());

        return existStudent;
    }

    @Override
    public Student delete(Student student) {
        for (Student student1: students) {
            if (student1.getId().equals(student.getId())) {
                students.remove(student1);

                return student1;
            }
        }

        return null;
    }
}
