package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button clients_btn;
    public Button logout_btn;
    public Button deposit_btn;
    public Button reports_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        create_client_btn.setOnAction(event -> onCreateClient());
        clients_btn.setOnAction(event -> onClients());
        deposit_btn.setOnAction(event -> onDeposit());
        reports_btn.setOnAction(event -> onReports());
        logout_btn.setOnAction(event -> onLogout());

    }

    private void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }

    private void onClients(){
        Model.getInstance().getViewFactory().getAdminMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    private void onDeposit(){
        Model.getInstance().getViewFactory().getAdminMenuItem().set(AdminMenuOptions.DEPOSIT);
    }

    private void onReports() {
        Model.getInstance().getViewFactory().getAdminMenuItem().set(AdminMenuOptions.REPORTS);
    }

    private void onLogout() {
        Stage stage = (Stage) create_client_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }


}


