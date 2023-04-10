package com.creativityfactory.swiftserver.client.models;

import com.creativityfactory.swiftserver.annotation.Id;

import java.util.List;

public class Subject {
    private List<Note> notes;
    private String name;
    @Id
    private String id;

}
