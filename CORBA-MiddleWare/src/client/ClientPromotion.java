package client;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import PromotionModule.*;

public class ClientPromotion {

    public static void main(String[] args) {
        try {
            System.out.println("═══════════════════════════════════════════════");
            System.out.println("    Client CORBA - Service Promotion");
            System.out.println("═══════════════════════════════════════════════\n");

            // Initialiser l'ORB
            ORB orb = ORB.init(args, null);
            System.out.println("✓ Connexion à l'ORB établie");

            // Obtenir le service de nommage
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            System.out.println("✓ Service de nommage contacté");

            // Résoudre la référence du service Promotion
            String name = "PromotionService";
            Promotion promotionRef = PromotionHelper.narrow(ncRef.resolve_str(name));
            System.out.println("✓ Service Promotion trouvé\n");

            // ============================================
            // TEST 1 : Créer des étudiants
            // ============================================
            System.out.println("─────────────────────────────────────────────");
            System.out.println("TEST 1 : Créer de nouveaux étudiants");
            System.out.println("─────────────────────────────────────────────");

            boolean creation1 = promotionRef.creerEtudiant("François Petit");
            System.out.println("Création François : " + (creation1 ? "✓ Réussi" : "✗ Échec"));

            boolean creation2 = promotionRef.creerEtudiant("Gabrielle Rousseau");
            System.out.println("Création Gabrielle : " + (creation2 ? "✓ Réussi" : "✗ Échec"));

            boolean creation3 = promotionRef.creerEtudiant("Alice Martin");
            System.out.println("Création Alice (doublon) : " +
                    (creation3 ? "✓ Réussi" : "✗ Échec (attendu)"));

            // ============================================
            // TEST 2 : Rechercher des étudiants
            // ============================================
            System.out.println("\n─────────────────────────────────────────────");
            System.out.println("TEST 2 : Rechercher des étudiants");
            System.out.println("─────────────────────────────────────────────");

            boolean trouve1 = promotionRef.rechercherUnEtudiant("François Petit");
            System.out.println("François Petit : " + (trouve1 ? "✓ Trouvé" : "✗ Non trouvé"));

            boolean trouve2 = promotionRef.rechercherUnEtudiant("Alice Martin");
            System.out.println("Alice Martin : " + (trouve2 ? "✓ Trouvé" : "✗ Non trouvé"));

            boolean trouve3 = promotionRef.rechercherUnEtudiant("Étudiant Inexistant");
            System.out.println("Étudiant Inexistant : " +
                    (trouve3 ? "✓ Trouvé" : "✗ Non trouvé (attendu)"));

            // ============================================
            // TEST 3 : Calculer le ratio de réussite
            // ============================================
            System.out.println("\n─────────────────────────────────────────────");
            System.out.println("TEST 3 : Ratio de réussite");
            System.out.println("─────────────────────────────────────────────");

            float ratio = promotionRef.calculerRatioReussite();
            System.out.println("Ratio de réussite : " + String.format("%.2f", ratio) + "%");

            if (ratio >= 80) {
                System.out.println("→ Excellente promotion ! 🎉");
            } else if (ratio >= 60) {
                System.out.println("→ Bonne promotion ✓");
            } else {
                System.out.println("→ Promotion en difficulté ⚠");
            }

            // ============================================
            // TEST 4 : Moyenne générale
            // ============================================
            System.out.println("\n─────────────────────────────────────────────");
            System.out.println("TEST 4 : Moyenne générale de la promotion");
            System.out.println("─────────────────────────────────────────────");

            float moyenneGenerale = promotionRef.obtenirMoyenneGenerale();
            System.out.println("Moyenne générale : " +
                    String.format("%.2f", moyenneGenerale) + "/20");

            if (moyenneGenerale >= 14) {
                System.out.println("→ Promotion d'excellence ! 🌟");
            } else if (moyenneGenerale >= 12) {
                System.out.println("→ Très bonne promotion ✓");
            } else if (moyenneGenerale >= 10) {
                System.out.println("→ Promotion satisfaisante");
            } else {
                System.out.println("→ Promotion en difficulté ⚠");
            }

            System.out.println("\n═══════════════════════════════════════════════");
            System.out.println("  Tous les tests terminés avec succès !");
            System.out.println("═══════════════════════════════════════════════");

        } catch (Exception e) {
            System.err.println("✗ ERREUR CLIENT : " + e.getMessage());
            e.printStackTrace();
        }
    }
}