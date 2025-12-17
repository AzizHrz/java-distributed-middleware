package client;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import EtudiantModule.*;

public class ClientEtudiant {

    public static void main(String[] args) {
        try {
            System.out.println("═══════════════════════════════════════════════");
            System.out.println("    Client CORBA - Service Étudiant");
            System.out.println("═══════════════════════════════════════════════\n");

            // Initialiser l'ORB
            ORB orb = ORB.init(args, null);
            System.out.println("✓ Connexion à l'ORB établie");

            // Obtenir le service de nommage
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            System.out.println("✓ Service de nommage contacté");

            // Résoudre la référence du service Étudiant
            String name = "EtudiantService";
            Etudiant etudiantRef = EtudiantHelper.narrow(ncRef.resolve_str(name));
            System.out.println("✓ Service Étudiant trouvé\n");

            // ============================================
            // TEST 1 : Ajouter des épreuves
            // ============================================
            System.out.println("─────────────────────────────────────────────");
            System.out.println("TEST 1 : Ajouter des épreuves");
            System.out.println("─────────────────────────────────────────────");

            Epreuve epreuve1 = new Epreuve("Algorithmique", 17.5f, 4);
            boolean ajout1 = etudiantRef.ajouterUneEpreuve("Alice Martin", epreuve1);
            System.out.println("Ajout épreuve Alice : " + (ajout1 ? "✓ Réussi" : "✗ Échec"));

            Epreuve epreuve2 = new Epreuve("Base de données", 15.0f, 3);
            boolean ajout2 = etudiantRef.ajouterUneEpreuve("Bob Dupont", epreuve2);
            System.out.println("Ajout épreuve Bob : " + (ajout2 ? "✓ Réussi" : "✗ Échec"));

            // ============================================
            // TEST 2 : Lister les épreuves d'un étudiant
            // ============================================
            System.out.println("\n─────────────────────────────────────────────");
            System.out.println("TEST 2 : Lister les épreuves");
            System.out.println("─────────────────────────────────────────────");

            Epreuve[] epreuvesAlice = etudiantRef.listeDesEpreuves("Alice Martin");
            System.out.println("Épreuves d'Alice Martin (" + epreuvesAlice.length + ") :");
            for (Epreuve e : epreuvesAlice) {
                System.out.println("  - " + e.nom + " : " + e.note + "/20 (coef " +
                        e.coefficient + ")");
            }

            // ============================================
            // TEST 3 : Calculer la moyenne
            // ============================================
            System.out.println("\n─────────────────────────────────────────────");
            System.out.println("TEST 3 : Calculer les moyennes");
            System.out.println("─────────────────────────────────────────────");

            float moyenneAlice = etudiantRef.calculerLaMoyenne("Alice Martin");
            System.out.println("Moyenne d'Alice Martin : " +
                    String.format("%.2f", moyenneAlice) + "/20");

            float moyenneBob = etudiantRef.calculerLaMoyenne("Bob Dupont");
            System.out.println("Moyenne de Bob Dupont : " +
                    String.format("%.2f", moyenneBob) + "/20");

            float moyenneClaire = etudiantRef.calculerLaMoyenne("Claire Bernard");
            System.out.println("Moyenne de Claire Bernard : " +
                    String.format("%.2f", moyenneClaire) + "/20");

            // ============================================
            // TEST 4 : Lister les 10 meilleurs étudiants
            // ============================================
            System.out.println("\n─────────────────────────────────────────────");
            System.out.println("TEST 4 : Top 10 des étudiants");
            System.out.println("─────────────────────────────────────────────");

            EtudiantInfo[] top10 = etudiantRef.listerLes10Premiers();
            System.out.println("Classement (Top " + top10.length + ") :");
            for (int i = 0; i < top10.length; i++) {
                System.out.println((i + 1) + ". " + top10[i].nom + " : " +
                        String.format("%.2f", top10[i].moyenne) + "/20");
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