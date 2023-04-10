package com.creativityfactory.swiftserver.client.models;

import com.creativityfactory.swiftserver.annotation.HasMany;
import com.creativityfactory.swiftserver.annotation.Rest;
import com.creativityfactory.swiftserver.annotation.FromDataSource;

import java.util.List;
import java.util.Objects;

@FromDataSource("studentSrc")
@Rest
public class Student {
    private Integer id;
    private String name;
    @HasMany
    private List<Note> notes;

    public Student() {}

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student(Integer id, String name, List<Note> notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "id :" + id + " | name: " + name;
    }

    @Override
    public boolean equals(Object obj) {
        Student student = (Student) obj;
        return (Objects.equals(id, student.id)) && (name.equals(student.name));
    }
}
