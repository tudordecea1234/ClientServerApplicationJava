package clientFX.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CharityCase;
import model.Donation;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;

import java.io.IOException;
import java.util.Collection;

public class MainViewController implements ITeledonObserver {
    ObservableList<CharityCase> modelCase = FXCollections.observableArrayList();
    private ITeledonServices service;
    private Long caseId;
    @FXML
    TableColumn<CharityCase, Long> idCase;
    @FXML
    TableColumn<CharityCase, String> nameCase;
    @FXML
    TableColumn<CharityCase, Float> totalAmount;
    @FXML
    TableView<CharityCase> tableViewCases;

    public void initialize() {
        idCase.setCellValueFactory(new PropertyValueFactory<CharityCase, Long>("id"));
        nameCase.setCellValueFactory(new PropertyValueFactory<CharityCase, String>("caseName"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<CharityCase, Float>("totalAmount"));
        tableViewCases.setItems(modelCase);
        tableViewCases.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {if(newValue!=null) caseId = newValue.getId();
                                                        else caseId=3L;});

    }

    public void setService(ITeledonServices service) throws TeledonException {

        this.service = service;
        modelCase.setAll(service.getAllCases());

    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) tableViewCases.getScene().getWindow();
        stage.close();
    }

    public void backToLogIn(ActionEvent actionEvent) throws IOException, TeledonException {


        try {
            service.logout();
        } catch (TeledonException e) {
            System.out.println("Logout error " + e);
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartObjectClientFX.class.getResource("login.fxml"));
        Stage loginStage = new Stage();
        Scene scene = new Scene(loader.load(), 700, 400);
        LoginController ctr = loader.getController();
        ctr.setService(service);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        closeWindow(actionEvent);
        loginStage.show();



    }



    public void addNewDonation(ActionEvent actionEvent) throws IOException, TeledonException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartObjectClientFX.class.getResource("addDonation.fxml"));

        Stage loginStage = new Stage();
        Scene scene = new Scene(loader.load(), 700, 400);
        AddDonationController ctr = loader.getController();
        ctr.setService(service);
        ctr.setIdCase(caseId);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        //closeWindow(actionEvent);
        loginStage.show();

    }

    @Override
    public void donationReceived(Donation donation) throws TeledonException {

    }

    @Override
    public void casesAmountUpdate(Iterable<CharityCase> case1) throws TeledonException {
        Platform.runLater(()-> {
            try {
                modelCase.setAll(service.getAllCases());
            } catch (TeledonException e) {
                e.printStackTrace();
            }
            tableViewCases.setItems(modelCase);

        });
    }

    @Override
    public void donationsUpdate(Iterable<Donation> donations) throws TeledonException {

    }
}
