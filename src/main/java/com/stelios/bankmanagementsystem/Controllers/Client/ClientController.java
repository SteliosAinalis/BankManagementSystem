package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Model.getInstance().getViewFactory().getClientMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case TRANSACTIONS -> client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView());
                case ACCOUNTS -> client_parent.setCenter(Model.getInstance().getViewFactory().getAccountsView());
                case PROFILE -> client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            }
        });

        Model.getInstance().getViewFactory().getClientMenuItem().set(ClientMenuOptions.DASHBOARD);

    }
}
