package clientFX.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import teledon.services.ITeledonServices;

import java.io.IOException;

public class CreateNewAccountController {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
   private ITeledonServices server;
public CreateNewAccountController(){

}
    private void closeWindow() {
        Stage stage = (Stage) emailTextField.getScene().getWindow();
        stage.close();
    }
    /*public void registerUser(ActionEvent actionEvent) throws IOException {
        try {
            boolean result = server.addVolunteer(emailTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), passwordTextField.getText());
            if (result) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(clientFX.main.MainViewController.StartObjectClientFX.class.getResource("mainView.fxml"));

                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load(), 600, 500);
                clientFX.main.MainViewController ctrl = loader.getController();
                ctrl.setService(serviceTeledon);
                mainStage.setScene(scene);
                mainStage.setResizable(false);
                mainStage.show();
                closeWindow();
            } else {
                clientFX.main.AlertMessage.showErrorMessage("You already have an account with this email address!");
            }
        } catch (ValidationException validationException) {
            clientFX.main.AlertMessage.showErrorMessage(validationException.getMessage());
        }
    }*/
    public void backToLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartObjectClientFX.class.getResource("login.fxml"));

        Stage loginStage = new Stage();
        Scene scene = new Scene(loader.load(), 700, 400);
        LoginController ctr=loader.getController();
        ctr.setService(server);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        closeWindow();
        loginStage.show();
    }
    public void setService(ITeledonServices service) {

        this.server = service;
    }
}
