package impl;

import service.NotesServiceInterface;
import java.util.ArrayList;
import java.util.List;

public class NotesServiceImpl implements NotesServiceInterface {

    private final List<Double> notes = new ArrayList<>();

    @Override
    public void ajouterNote(int note) {
        notes.add((double) note);
    }

    @Override
    public boolean supprimerNote(int index) {
        if (index < 0 || index >= notes.size()) return false;
        notes.remove(index);
        return true;
    }

    @Override
    public boolean modifierNote(int index, double newNote) {
        if (index < 0 || index >= notes.size()) return false;
        notes.set(index, newNote);
        return true;
    }

    @Override
    public List<Double> listerNotes() {
        return notes;
    }
}
