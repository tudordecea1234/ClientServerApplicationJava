package clientFX.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;


import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button newAccountButton;
    @FXML
    private Button closeLoginButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    private ITeledonServices server;


    public LoginController() {

    }
    public void initialize() {
        emailTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    logInUser(new ActionEvent());
                } catch (IOException | TeledonException e) {
                    e.printStackTrace();
                }
            }
        });
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    logInUser(new ActionEvent());
                } catch (IOException | TeledonException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void logInUser(ActionEvent actionEvent) throws IOException, TeledonException {
        if(!Objects.equals(emailTextField.getText(), "") && !Objects.equals(passwordTextField.getText(), ""))
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartObjectClientFX.class.getResource("mainView.fxml"));
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 600, 500);
            MainViewController ctrl=loader.getController();

            try{
                server.loginUser(emailTextField.getText(), passwordTextField.getText(),ctrl);
                ctrl.setService(server);
                mainStage.setScene(scene);
                mainStage.setResizable(false);
                mainStage.show();
                closeLoginWindow(null);
            }
            catch(TeledonException e)
            {
                AlertMessage.showErrorMessage(e.getMessage());
            }


        }
        else AlertMessage.showErrorMessage("Invalid email or password!");

    }

    public void closeLoginWindow(ActionEvent actionEvent) throws TeledonException {
        Stage stage = (Stage) closeLoginButton.getScene().getWindow();
        stage.close();
    }
    public void closeLoginWindow2(ActionEvent actionEvent) throws TeledonException {
        Stage stage = (Stage) closeLoginButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void setService(ITeledonServices service) {

        this.server = service;
    }
    public void createNewAccount(ActionEvent actionEvent) throws IOException, TeledonException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartObjectClientFX.class.getResource("createAccount.fxml"));


        Stage newAccountStage = new Stage();
        Scene scene = new Scene(loader.load(), 600, 570);
        CreateNewAccountController ctrl=loader.getController();
        ctrl.setService(server);
        newAccountStage.setScene(scene);
        newAccountStage.setResizable(false);
        newAccountStage.show();
        closeLoginWindow(null);
    }
}


