package model;

import java.util.ArrayList;
import java.util.List;

public class EtudiantData {
    private String nom;
    private List<Epreuve> epreuves;

    public EtudiantData(String nom) {
        this.nom = nom;
        this.epreuves = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Epreuve> getEpreuves() {
        return epreuves;
    }

    public void ajouterEpreuve(Epreuve epreuve) {
        this.epreuves.add(epreuve);
    }

    // Calcul de la moyenne pondérée
    public float calculerMoyenne() {
        if (epreuves.isEmpty()) {
            return 0.0f;
        }

        float sommeNotesPonderees = 0.0f;
        int sommeCoefficients = 0;

        for (Epreuve e : epreuves) {
            sommeNotesPonderees += e.getNote() * e.getCoefficient();
            sommeCoefficients += e.getCoefficient();
        }

        return sommeCoefficients > 0 ? sommeNotesPonderees / sommeCoefficients : 0.0f;
    }

    @Override
    public String toString() {
        return "EtudiantData{" +
                "nom='" + nom + '\'' +
                ", nombreEpreuves=" + epreuves.size() +
                ", moyenne=" + calculerMoyenne() +
                '}';
    }
}