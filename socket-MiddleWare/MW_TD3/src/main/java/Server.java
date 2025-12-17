import org.apache.xmlrpc.webserver.WebServer;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;

import impl.CalculatriceServiceImpl;
import impl.NotesServiceImpl;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("Starting XML-RPC Server on port 8080...");

            WebServer webServer = new WebServer(8080);

            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            PropertyHandlerMapping phm = new PropertyHandlerMapping();

            // Register services
            phm.addHandler("calc", CalculatriceServiceImpl.class);
            phm.addHandler("notes", NotesServiceImpl.class);

            xmlRpcServer.setHandlerMapping(phm);

            XmlRpcServerConfigImpl config = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
            config.setEnabledForExtensions(true);
            config.setContentLengthOptional(false);

            webServer.start();
            System.out.println("XML-RPC Server started!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
