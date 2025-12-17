package server;

import EtudiantModule.*;
import model.EtudiantData;
import java.util.*;
import java.util.stream.Collectors;

public class EtudiantImpl extends EtudiantPOA {

    // Base de données en mémoire
    private Map<String, EtudiantData> baseDonneesEtudiants;

    public EtudiantImpl() {
        this.baseDonneesEtudiants = new HashMap<>();
        initialiserDonnees();
    }

    // Initialiser avec des données de test
    private void initialiserDonnees() {
        // Créer quelques étudiants avec des épreuves
        EtudiantData etud1 = new EtudiantData("Alice Martin");
        etud1.ajouterEpreuve(new model.Epreuve("Math", 16.5f, 3));
        etud1.ajouterEpreuve(new model.Epreuve("Physique", 14.0f, 2));
        etud1.ajouterEpreuve(new model.Epreuve("Informatique", 18.0f, 4));

        EtudiantData etud2 = new EtudiantData("Bob Dupont");
        etud2.ajouterEpreuve(new model.Epreuve("Math", 12.0f, 3));
        etud2.ajouterEpreuve(new model.Epreuve("Physique", 13.5f, 2));
        etud2.ajouterEpreuve(new model.Epreuve("Informatique", 15.0f, 4));

        EtudiantData etud3 = new EtudiantData("Claire Bernard");
        etud3.ajouterEpreuve(new model.Epreuve("Math", 17.0f, 3));
        etud3.ajouterEpreuve(new model.Epreuve("Physique", 16.5f, 2));
        etud3.ajouterEpreuve(new model.Epreuve("Informatique", 17.5f, 4));

        baseDonneesEtudiants.put("Alice Martin", etud1);
        baseDonneesEtudiants.put("Bob Dupont", etud2);
        baseDonneesEtudiants.put("Claire Bernard", etud3);

        System.out.println("✓ Données initiales chargées : " + baseDonneesEtudiants.size() + " étudiants");
    }

    @Override
    public boolean ajouterUneEpreuve(String nomEtudiant, Epreuve epreuve) {
        try {
            // Créer l'étudiant s'il n'existe pas
            if (!baseDonneesEtudiants.containsKey(nomEtudiant)) {
                baseDonneesEtudiants.put(nomEtudiant, new EtudiantData(nomEtudiant));
                System.out.println("→ Nouvel étudiant créé : " + nomEtudiant);
            }

            // Ajouter l'épreuve
            EtudiantData etudiant = baseDonneesEtudiants.get(nomEtudiant);
            model.Epreuve nouvelleEpreuve = new model.Epreuve(
                    epreuve.nom,
                    epreuve.note,
                    epreuve.coefficient
            );
            etudiant.ajouterEpreuve(nouvelleEpreuve);

            System.out.println("✓ Épreuve ajoutée : " + epreuve.nom + " (" + epreuve.note +
                    "/20, coef " + epreuve.coefficient + ") pour " + nomEtudiant);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Erreur lors de l'ajout de l'épreuve : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Epreuve[] listeDesEpreuves(String nomEtudiant) {
        EtudiantData etudiant = baseDonneesEtudiants.get(nomEtudiant);

        if (etudiant == null) {
            System.out.println("⚠ Étudiant non trouvé : " + nomEtudiant);
            return new Epreuve[0];
        }

        List<model.Epreuve> epreuves = etudiant.getEpreuves();
        Epreuve[] resultat = new Epreuve[epreuves.size()];

        for (int i = 0; i < epreuves.size(); i++) {
            model.Epreuve e = epreuves.get(i);
            resultat[i] = new Epreuve(e.getNom(), e.getNote(), e.getCoefficient());
        }

        System.out.println("→ " + resultat.length + " épreuve(s) trouvée(s) pour " + nomEtudiant);
        return resultat;
    }

    @Override
    public float calculerLaMoyenne(String nomEtudiant) {
        EtudiantData etudiant = baseDonneesEtudiants.get(nomEtudiant);

        if (etudiant == null) {
            System.out.println("⚠ Étudiant non trouvé : " + nomEtudiant);
            return 0.0f;
        }

        float moyenne = etudiant.calculerMoyenne();
        System.out.println("→ Moyenne de " + nomEtudiant + " : " +
                String.format("%.2f", moyenne) + "/20");
        return moyenne;
    }

    @Override
    public EtudiantInfo[] listerLes10Premiers() {
        // Trier les étudiants par moyenne décroissante
        List<EtudiantData> etudiantsTries = baseDonneesEtudiants.values().stream()
                .sorted((e1, e2) -> Float.compare(e2.calculerMoyenne(), e1.calculerMoyenne()))
                .limit(10)
                .collect(Collectors.toList());

        EtudiantInfo[] resultat = new EtudiantInfo[etudiantsTries.size()];

        for (int i = 0; i < etudiantsTries.size(); i++) {
            EtudiantData e = etudiantsTries.get(i);
            resultat[i] = new EtudiantInfo(e.getNom(), e.calculerMoyenne());
        }

        System.out.println("→ Top " + resultat.length + " étudiants listés");
        return resultat;
    }
}