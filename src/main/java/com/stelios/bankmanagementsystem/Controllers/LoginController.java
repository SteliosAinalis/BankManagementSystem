package com.stelios.bankmanagementsystem.Controllers;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public TextField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Label payee_address_lbl;
    public TextField payee_address_fld;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT,  AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
        login_btn.setOnAction(event -> onLogin());
    }


    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT){
            //Client
            Model.getInstance().evaluateClientCredentials(payee_address_fld.getText(), password_fld.getText());
            if (Model.getInstance().getClientLoginSuccess()){
                Model.getInstance().getViewFactory().showClientWindow();
                Model.getInstance().getViewFactory().closeStage(stage);

            }else{
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Credentials");
            }
        }else{
            //Admin
            Model.getInstance().evaluateAdminCredentials(payee_address_fld.getText(), password_fld.getText());
            if (Model.getInstance().getAdminLoginSuccess()){
                Model.getInstance().getViewFactory().showAdminWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            }else {
                payee_address_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Credentials");
            }
        }

    }



    private void setAcc_selector(){
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        if(acc_selector.getValue() == AccountType.ADMIN){
            payee_address_lbl.setText("Username:");
        }else {
            payee_address_lbl.setText("Payee Address:");
        }
    }
}
