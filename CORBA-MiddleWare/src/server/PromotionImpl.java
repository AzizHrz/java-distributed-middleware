package server;

import PromotionModule.*;
import model.EtudiantData;
import java.util.*;

public class PromotionImpl extends PromotionPOA {

    private Map<String, EtudiantData> promotion;
    private static final float SEUIL_REUSSITE = 10.0f;

    public PromotionImpl() {
        this.promotion = new HashMap<>();
        initialiserPromotion();
    }

    private void initialiserPromotion() {
        // Créer une promotion avec plusieurs étudiants
        creerEtudiant("Alice Martin");
        creerEtudiant("Bob Dupont");
        creerEtudiant("Claire Bernard");
        creerEtudiant("David Leroy");
        creerEtudiant("Emma Dubois");

        System.out.println("✓ Promotion initialisée avec " + promotion.size() + " étudiants");
    }

    @Override
    public boolean creerEtudiant(String nomEtudiant) {
        try {
            if (promotion.containsKey(nomEtudiant)) {
                System.out.println("⚠ L'étudiant existe déjà : " + nomEtudiant);
                return false;
            }

            promotion.put(nomEtudiant, new EtudiantData(nomEtudiant));
            System.out.println("✓ Étudiant créé : " + nomEtudiant);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Erreur création étudiant : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rechercherUnEtudiant(String nomEtudiant) {
        boolean existe = promotion.containsKey(nomEtudiant);

        if (existe) {
            System.out.println("✓ Étudiant trouvé : " + nomEtudiant);
        } else {
            System.out.println("✗ Étudiant non trouvé : " + nomEtudiant);
        }

        return existe;
    }

    @Override
    public float calculerRatioReussite() {
        if (promotion.isEmpty()) {
            System.out.println("⚠ Aucun étudiant dans la promotion");
            return 0.0f;
        }

        long nbReussis = promotion.values().stream()
                .filter(e -> e.calculerMoyenne() >= SEUIL_REUSSITE)
                .count();

        float ratio = (float) nbReussis / promotion.size() * 100;

        System.out.println("→ Ratio de réussite : " + nbReussis + "/" +
                promotion.size() + " = " + String.format("%.2f", ratio) + "%");

        return ratio;
    }

    @Override
    public float obtenirMoyenneGenerale() {
        if (promotion.isEmpty()) {
            System.out.println("⚠ Aucun étudiant dans la promotion");
            return 0.0f;
        }

        float somme = 0.0f;
        int compteur = 0;

        for (EtudiantData etudiant : promotion.values()) {
            float moyenne = etudiant.calculerMoyenne();
            if (moyenne > 0) {  // Ignore les étudiants sans épreuves
                somme += moyenne;
                compteur++;
            }
        }

        float moyenneGenerale = compteur > 0 ? somme / compteur : 0.0f;

        System.out.println("→ Moyenne générale de la promotion : " +
                String.format("%.2f", moyenneGenerale) + "/20");

        return moyenneGenerale;
    }

    // Méthode utilitaire pour ajouter des épreuves (pour les tests)
    public void ajouterEpreuveEtudiant(String nomEtudiant, String nomEpreuve,
                                       float note, int coefficient) {
        EtudiantData etudiant = promotion.get(nomEtudiant);
        if (etudiant != null) {
            etudiant.ajouterEpreuve(new model.Epreuve(nomEpreuve, note, coefficient));
            System.out.println("✓ Épreuve ajoutée pour " + nomEtudiant);
        }
    }
}