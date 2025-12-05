package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;
    public Button delete_client_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initClientsList();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(e -> new ClientCellFactory());
        delete_client_btn.setOnAction(event -> onDeleteClient());
    }

    private void initClientsList() {
        if (Model.getInstance().getClients().isEmpty()) {
            Model.getInstance().setClients();
        }
    }


    private void onDeleteClient() {
        Client selectedClient = clients_listview.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Client Selected");
            alert.setContentText("Please select a client from the list to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Client");
        confirmationAlert.setHeaderText("Are you sure you want to delete this client?");
        confirmationAlert.setContentText("This will permanently delete the client and all of their associated accounts and transactions. This action cannot be undone.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String pAddress = selectedClient.payeeAddressProperty().get();
            Model.getInstance().getDatabaseDriver().deleteClient(pAddress);
            clients_listview.getItems().remove(selectedClient);
        }
    }
}