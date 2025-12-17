import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:8080/RPC2"));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Addition");
            System.out.println("2. Multiplication");
            System.out.println("3. Soustraction");
            System.out.println("4. Division");
            System.out.println("5. Ajouter Note");
            System.out.println("6. Supprimer Note");
            System.out.println("7. Modifier Note");
            System.out.println("8. Lister Notes");
            System.out.println("9. Quitter");
            System.out.print("Choix : ");
            int ch = sc.nextInt();

            if (ch == 9) break;

            switch (ch) {
                case 1 -> {
                    System.out.print("a = "); double a = sc.nextDouble();
                    System.out.print("b = "); double b = sc.nextDouble();
                    Object result = client.execute("calc.addition", new Object[]{a, b});
                    System.out.println("Résultat = " + result);
                }

                case 2 -> {
                    System.out.print("a = "); double a = sc.nextDouble();
                    System.out.print("b = "); double b = sc.nextDouble();
                    Object result = client.execute("calc.multiplication", new Object[]{a, b});
                    System.out.println("Résultat = " + result);
                }

                case 3 -> {
                    System.out.print("a = "); double a = sc.nextDouble();
                    System.out.print("b = "); double b = sc.nextDouble();
                    Object result = client.execute("calc.soustraction", new Object[]{a, b});
                    System.out.println("Résultat = " + result);
                }

                case 4 -> {
                    System.out.print("a = "); double a = sc.nextDouble();
                    System.out.print("b = "); double b = sc.nextDouble();
                    Object result = client.execute("calc.division", new Object[]{a, b});
                    System.out.println("Résultat = " + result);
                }

                case 5 -> {
                    System.out.print("Nouvelle note: ");
                    double note = sc.nextInt();
                    client.execute("notes.ajouterNote", new Object[]{note});
                }

                case 6 -> {
                    System.out.print("Index à supprimer: ");
                    int index = sc.nextInt();
                    client.execute("notes.supprimerNote", new Object[]{index});
                }

                case 7 -> {
                    System.out.print("Index à modifier: ");
                    int index = sc.nextInt();
                    System.out.print("Nouvelle note: ");
                    double newNote = sc.nextDouble();
                    client.execute("notes.modifierNote", new Object[]{index, newNote});
                }

                case 8 -> {
                    Object result = client.execute("notes.listerNotes", new Object[]{});
                    System.out.println("Notes = " + Arrays.toString((Object[]) result));
                }
            }
        }

        System.out.println("Client terminé.");
    }
}
