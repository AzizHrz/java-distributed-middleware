package server;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;
import EtudiantModule.*;
import PromotionModule.*;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("═══════════════════════════════════════════════");
            System.out.println("    Démarrage du Serveur CORBA");
            System.out.println("═══════════════════════════════════════════════\n");

            // Initialiser l'ORB
            ORB orb = ORB.init(args, null);
            System.out.println("✓ ORB initialisé");

            // Obtenir la référence au POA
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();
            System.out.println("✓ POA activé");

            // Créer les implémentations des servants
            EtudiantImpl etudiantImpl = new EtudiantImpl();
            PromotionImpl promotionImpl = new PromotionImpl();
            System.out.println("✓ Servants créés\n");

            // Activer les servants
            org.omg.CORBA.Object refEtudiant = rootPOA.servant_to_reference(etudiantImpl);
            Etudiant etudiantRef = EtudiantHelper.narrow(refEtudiant);

            org.omg.CORBA.Object refPromotion = rootPOA.servant_to_reference(promotionImpl);
            Promotion promotionRef = PromotionHelper.narrow(refPromotion);
            System.out.println("✓ Références des servants obtenues");

            // Obtenir le service de nommage
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            System.out.println("✓ Service de nommage contacté");

            // Enregistrer les objets dans le service de nommage
            String nameEtudiant = "EtudiantService";
            NameComponent[] pathEtudiant = ncRef.to_name(nameEtudiant);
            ncRef.rebind(pathEtudiant, etudiantRef);
            System.out.println("✓ Service Étudiant enregistré : " + nameEtudiant);

            String namePromotion = "PromotionService";
            NameComponent[] pathPromotion = ncRef.to_name(namePromotion);
            ncRef.rebind(pathPromotion, promotionRef);
            System.out.println("✓ Service Promotion enregistré : " + namePromotion);

            System.out.println("\n═══════════════════════════════════════════════");
            System.out.println("  Serveur CORBA prêt et en attente...");
            System.out.println("  Services disponibles :");
            System.out.println("    - EtudiantService");
            System.out.println("    - PromotionService");
            System.out.println("═══════════════════════════════════════════════\n");

            // Attendre les invocations des clients
            orb.run();

        } catch (Exception e) {
            System.err.println("✗ ERREUR SERVEUR : " + e.getMessage());
            e.printStackTrace();
        }
    }
}