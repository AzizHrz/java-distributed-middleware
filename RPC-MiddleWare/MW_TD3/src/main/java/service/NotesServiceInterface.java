package service;

import java.util.List;

public interface NotesServiceInterface {
    void ajouterNote(int note);
    boolean supprimerNote(int index);
    boolean modifierNote(int index, double newNote);
    List<Double> listerNotes();
}
