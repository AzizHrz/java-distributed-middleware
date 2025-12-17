package model;


public class Epreuve {
    private String nom;
    private float note;
    private int coefficient;

    public Epreuve(String nom, float note, int coefficient) {
        this.nom = nom;
        this.note = note;
        this.coefficient = coefficient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return "Epreuve{" +
                "nom='" + nom + '\'' +
                ", note=" + note +
                ", coefficient=" + coefficient +
                '}';
    }
}