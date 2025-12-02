package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fld;
    public CheckBox sv_acc_box;
    public TextField sv_acc_fld;
    public Button create_client_btn;
    public Text error_lbl;


    private String payeeAddress;
    private boolean createCheckingsAccountFlag = false;
    private boolean createSavingsAccountFlag = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        });
        ch_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createCheckingsAccountFlag= true;
            }
        });
        sv_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createSavingsAccountFlag = true;
            }
        });

    }

    private void createAccount(String accountType) {
        double balance = Double.parseDouble(ch_amount_fld.getText());

        //Create account number
        String firstSection = "0429";
        String lastSection = Integer.toString((new Random()).nextInt(9999)+1000);
        String accountNumber = firstSection + " " + lastSection;
        if (accountType.equals("Checking")) {
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 10, balance);
        }else if (accountType.equals("Savings")) {
            Model.getInstance().getDatabaseDriver().createSavingsAccount(payeeAddress, accountNumber, 2000, balance);
        }
    }

    private void createClient() {
        if (createCheckingsAccountFlag) {
            createAccount("Checking");
        }

        if (createSavingsAccountFlag) {
            createAccount("Savings");
        }

        String fName = fName_fld.getText();
        String lName = lName_fld.getText();
        String password = password_fld.getText();
        Model.getInstance().getDatabaseDriver().createClient(fName, lName, payeeAddress, password, LocalDate.now());
        error_lbl.setStyle("-fx-text-fill: green;  -fx-font-size: 1.3em; -fx-font-weight: bold;");
        error_lbl.setText("Client Account created successfully!");
        resetFields();
    }


    private void onCreatePayeeAddress() {
        if(fName_fld.getText() != null & lName_fld.getText() != null) {
            pAddress_lbl.setText(payeeAddress);
        }
    }

    private String createPayeeAddress() {
        int id = Model.getInstance().getDatabaseDriver().getLastClientsId()+1;
        char fChar = Character.toLowerCase(fName_fld.getText().charAt(0));
        return "@"+fChar+lName_fld.getText()+id;
    }




    private void resetFields() {
        fName_fld.clear();
        lName_fld.clear();
        password_fld.clear();
        pAddress_box.setSelected(false);
        pAddress_lbl.setText("");
        ch_acc_box.setSelected(false);
        ch_amount_fld.clear();
        sv_acc_box.setSelected(false);
        sv_acc_fld.clear();
    }








}
