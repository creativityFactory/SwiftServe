package com.creativityfactory.swiftserver.client.models;

import com.creativityfactory.swiftserver.annotation.BelongTo;
import com.creativityfactory.swiftserver.annotation.FromDataSource;

@FromDataSource
public class Note {
    private Integer id;
    private Float mark;
    @BelongTo
    private Student student;
    private Boolean isActive;

    public Note() {}

    public Note(Integer id, Float mark, Boolean isActive) {
        this.id = id;
        this.mark = mark;
        this.isActive = isActive;
    }

    public Note(Integer id, Float mark, Boolean isActive, Student student) {
        this.id = id;
        this.mark = mark;
        this.isActive = true;
        this.student = student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMark() {
        return mark;
    }

    public void setMark(Float mark) {
        this.mark = mark;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "id :" + id + " | mark: " + mark;
    }
}
