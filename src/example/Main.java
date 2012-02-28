package example;

import kerberos.protocol.as.ASConfiguration;
import kerberos.protocol.as.AuthenticationServer;
import kerberos.protocol.service.ServiceConfiguration;
import kerberos.protocol.tgs.TGSConfiguration;
import kerberos.protocol.tgs.TicketGrantingServer;
import kerberos.serialize.SerializeUtilities;
import example.client.Client;
import example.service.TestService;

public class Main {

    /**
     * Demonstrates the usage of the Kerberos-Protocol.
     * Starts an AuthenticationServer, a TicketGrantingServer and a Service,
     * to which the GUI-Client will connect.
     */
    public Main() {
        try{
            String configDir = this.getConfigDirectory();

            ASConfiguration asc = SerializeUtilities.deserializeFile(
                  configDir + "as-config.xml", ASConfiguration.class);
            AuthenticationServer as = new AuthenticationServer(asc);
            System.out.println("AS started");

            TGSConfiguration tgsc = SerializeUtilities.deserializeFile(
                  configDir + "tgs-config.xml", TGSConfiguration.class);
            TicketGrantingServer tgs = new TicketGrantingServer(tgsc);
            System.out.println("TGS started");

            ServiceConfiguration sc = SerializeUtilities.deserializeFile(
                  configDir + "service-config.xml", ServiceConfiguration.class);
            TestService service = new TestService(sc);

            new Client();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Tries to find the config-directory relative to this Main.class.
     * /path/to/example/Main.class ../../config
     * @return the absolute path to the config directory.
     */
    private String getConfigDirectory(){
        String resource = "/example/Main.class";
        String path = this.getClass().getResource(resource).getPath();

        int endIndex = path.length() - resource.length() - "bin".length();
        String configPath = path.substring(0, endIndex) + "config/";

        return configPath;
    }

    /**
     * Creates an Instance of this class.
     * @param args none expected
     */
    public static void main(String[] args) {
        new Main();
    }

}
