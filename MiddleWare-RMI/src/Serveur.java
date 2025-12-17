import service.IMath;
import service.IStringTools;
import service.impl.MathImpl;
import service.impl.StringToolsImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Serveur {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            IMath mathService = new MathImpl();
            IStringTools stringService = new StringToolsImpl();

            registry.rebind("MATH", mathService);
            registry.rebind("STR", stringService);

            System.out.println("Serveur RMI démarré...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
