package clientFX.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CharityCase;
import model.Donation;
import teledon.services.ITeledonObserver;
import teledon.services.ITeledonServices;
import teledon.services.TeledonException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class AddDonationController {
    ObservableList<Donation> donations = FXCollections.observableArrayList();
    ITeledonServices server;
    private Long idCase;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField amountDonated;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField idCaseTextField;
    @FXML
    private TableColumn<Donation, String> donorFirstName;
    @FXML
    private TableColumn<Donation, String> donorLastName;
    @FXML
    private TableView<Donation> donationView;

    public void initialize() {
        donorFirstName.setCellValueFactory(new PropertyValueFactory<Donation, String>("donorFirstName"));
        donorLastName.setCellValueFactory(new PropertyValueFactory<Donation, String>("donorLastName"));
        donationView.setItems(donations);
        donationView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    address.setText(newValue.getAddress());
                    phoneNumber.setText(newValue.getPhoneNumber());
                    firstName.setText(newValue.getDonorFirstName());
                    lastName.setText(newValue.getDonorLastName());
                });

    }

    public void setService(ITeledonServices server) throws TeledonException {
        this.server = server;

        nameTextField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                findDonorByName();
            } catch (TeledonException e) {
                e.printStackTrace();
            }
        });
        donationView.setItems(donations);
        updateTableWithUsersAtSearch("");
    }

    public void setIdCase(Long id) {
        this.idCase = id;
        idCaseTextField.setText(idCase.toString());
        idCaseTextField.disabledProperty();
    }

    private List<Donation> getUserRecordList(String searchName) throws TeledonException {
        Collection<Donation> usersList = server.getAllDonations(searchName);

        return usersList.stream().distinct().toList();
    }

    public void findDonorByName() throws TeledonException {
        updateTableWithUsersAtSearch(nameTextField.getText());
    }

    private void updateTableWithUsersAtSearch(String searchName) throws TeledonException {
        donations.setAll(getUserRecordList(searchName));
    }

    public void closeLoginWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
    public void backToMain(ActionEvent actionEvent) throws IOException, TeledonException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartObjectClientFX.class.getResource("mainView.fxml"));
        Stage mainStage = new Stage();
        Scene scene = new Scene(loader.load(), 600, 500);
        MainViewController ctrl = loader.getController();
        ctrl.setService(server);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
        closeLoginWindow(null);
    }
    public void addNewDonation(ActionEvent actionEvent) throws IOException, TeledonException {
        Donation donation=new Donation(idCase, firstName.getText(), lastName.getText(), address.getText(), phoneNumber.getText(), Float.parseFloat(amountDonated.getText())) ;
        server.addDonation(donation);
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StartObjectClientFX.class.getResource("mainView.fxml"));
        Stage mainStage = new Stage();
        Scene scene = new Scene(loader.load(), 600, 500);
        MainViewController ctrl = loader.getController();
        ctrl.setService(server);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();*/
        closeLoginWindow(null);
    }

}
