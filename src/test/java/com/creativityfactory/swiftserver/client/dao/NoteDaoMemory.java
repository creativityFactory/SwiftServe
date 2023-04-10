package com.creativityfactory.swiftserver.client.dao;

import com.creativityfactory.swiftserver.client.models.Note;
import com.creativityfactory.swiftserver.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

public class NoteDaoMemory implements Persistence<Note> {
    // TODO refactor Persistence interface
    private static final List<Note> notes = new ArrayList<>();
    @Override
    public List<Note> getAll() {
        return notes;
    }

    @Override
    public List<Note> getLimit(Integer limit) {
        return null;
    }

    @Override
    public Note getById(Object o) {
        Integer id = (Integer) o;
        for (Note note: notes) {
            if (id.equals(note.getId())) return note;
        }
        return null;
    }


    @Override
    public Note save(Note note) {
        if (getById(note.getId()) == null) {
            notes.add(note);
            return note;
        }

        return null;
    }

    @Override
    public Note update(Note note) {
        Note existNote = getById(note.getId());
        if (existNote == null) return null;

        existNote.setMark(note.getMark());
        existNote.setStudent(note.getStudent());

        return existNote;
    }

    @Override
    public Note delete(Note note) {
        for (Note note1: notes) {
            if (note1.getId().equals(note.getId())) {
                notes.remove(note1);

                return note1;
            }
        }

        return null;
    }
}
