package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminMenuItem().addListener((observableValue, oldVal, newVal)  ->{
            switch (newVal){
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
                case DEPOSIT ->  admin_parent.setCenter(Model.getInstance().getViewFactory().getDepositView());
                case REPORTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getReportsView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());
            }
        });
    }
}
