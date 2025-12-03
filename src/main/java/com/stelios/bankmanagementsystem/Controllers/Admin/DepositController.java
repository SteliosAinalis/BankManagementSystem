package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.ClientCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<Client> result_listview;
    public Button deposit_btn;
    public TextField amount_fld;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(e-> onClientSearch());
        deposit_btn.setOnAction(e-> onDeposit());
    }


    private void onClientSearch() {
        ObservableList<Client> searchResults = Model.getInstance().searchClient(pAddress_fld.getText());
        result_listview.setItems(searchResults);
        result_listview.setCellFactory(e -> new ClientCellFactory());
        client = searchResults.get(0);

    }

    private void onDeposit() {
        double amount = Double.parseDouble(amount_fld.getText());
        double newBalance = amount + client.savingsAccountProperty().get().balanceProperty().get();
        if(amount_fld.getText() != null){
            Model.getInstance().getDatabaseDriver().depositSavings(client.payeeAddressProperty().get(), newBalance);
        }
        resetFields();
    }

    private void resetFields() {
        pAddress_fld.clear();
        amount_fld.clear();
    }
}
