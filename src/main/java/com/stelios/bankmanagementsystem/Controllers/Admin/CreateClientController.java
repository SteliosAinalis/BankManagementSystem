package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.CheckingAccount;
import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Models.SavingsAccount;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal) {
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        });
    }

    private void createClient() {
        if (fName_fld.getText().isBlank() || lName_fld.getText().isBlank() || password_fld.getText().isBlank() || payeeAddress == null) {
            error_lbl.setText("All client fields are required.");
            return;
        }
        if (!ch_acc_box.isSelected() && !sv_acc_box.isSelected()) {
            error_lbl.setText("Must create at least one account.");
            return;
        }
        CheckingAccount checkingAccount = null;

        if (ch_acc_box.isSelected()) {
            double balance = Double.parseDouble(ch_amount_fld.getText());
            String accNum = createAccountNumber();
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accNum, 10, balance);
            checkingAccount = new CheckingAccount(payeeAddress, accNum, balance, 10);
        }

        SavingsAccount savingsAccount = null;

        if (sv_acc_box.isSelected()) {
            double balance = Double.parseDouble(sv_acc_fld.getText());
            String accNum = createAccountNumber();
            Model.getInstance().getDatabaseDriver().createSavingsAccount(payeeAddress, accNum, 2000, balance);
            savingsAccount = new SavingsAccount(payeeAddress, accNum, balance, 2000);
        }

        String fName = fName_fld.getText();
        String lName = lName_fld.getText();
        String password = password_fld.getText();
        Model.getInstance().getDatabaseDriver().createClient(fName, lName, payeeAddress, password, LocalDate.now());
        Client newClient = new Client(fName, lName, payeeAddress, checkingAccount, savingsAccount, LocalDate.now(), null);
        Model.getInstance().addClientToAllClientsList(newClient);
        error_lbl.setStyle("-fx-text-fill: green; -fx-font-size: 1.3em; -fx-font-weight: bold;");
        error_lbl.setText("Client created successfully!");
        resetFields();
    }

    private void onCreatePayeeAddress() {
        // Use isBlank() for proper validation (checks for empty strings and whitespace)
        if (!fName_fld.getText().isBlank() && !lName_fld.getText().isBlank()) {
            pAddress_lbl.setText(payeeAddress);
        }
    }

    private String createPayeeAddress() {
        int id = Model.getInstance().getDatabaseDriver().getLastClientsId() + 1;
        char fChar = Character.toLowerCase(fName_fld.getText().charAt(0));
        return "@" + fChar + lName_fld.getText().toLowerCase() + id;
    }

    private String createAccountNumber() {
        String firstSection = "0429";
        String lastSection = Integer.toString(new Random().nextInt(9000) + 1000);
        return firstSection + " " + lastSection;
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