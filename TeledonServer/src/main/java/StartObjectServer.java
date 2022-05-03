import repository.*;
import server.TeledonServicesImplementation;
import teledon.services.ITeledonServices;
import utils.AbstractServer;
import utils.TeledonObjectConcurrentServer;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class StartObjectServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/teledonserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }
        VolunteerRepository volunteerRepository=new VolunteerDbRepository(serverProps);
        DonationRepository donationRepository=new DonationDbRepository(serverProps);
        CharityCaseRepository charityCaseRepository=new CharityCaseDbRepository(serverProps);
        CharityCaseHibernateRepository caseHibernateRepository=new CharityCaseHibernateRepository();
        ITeledonServices teledonServices=new TeledonServicesImplementation(volunteerRepository,donationRepository,charityCaseRepository,caseHibernateRepository);
        int teledonServerPort=defaultPort;
        try {
            teledonServerPort = Integer.parseInt(serverProps.getProperty("teledon.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+teledonServerPort);
        AbstractServer server = new TeledonObjectConcurrentServer(teledonServerPort, teledonServices);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
