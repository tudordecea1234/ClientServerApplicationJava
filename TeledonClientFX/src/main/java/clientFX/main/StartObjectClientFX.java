package clientFX.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import objectProtocol.TeledonServicesObjectProxy;
import protoBuffProtocol.ProtoTeledonProxy;
import teledon.services.ITeledonServices;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.Properties;

public class StartObjectClientFX extends Application {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartObjectClientFX.class.getResourceAsStream("/teledonclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        Stage stage = new Stage();
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        ITeledonServices server = new ProtoTeledonProxy(serverIP, serverPort);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(StartObjectClientFX.class.getResource("login.fxml"));
        var path= (StartObjectClientFX.class.getClassLoader().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        LoginController ctrl=fxmlLoader.getController();
        ctrl.setService(server);
        stage.setScene(scene);
        stage.show();
    }
}
