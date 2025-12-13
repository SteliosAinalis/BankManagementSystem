package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.CheckingAccount;
import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Models.SavingsAccount;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_account_date;
    public Label ch_acc_bal;

    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;


    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_to_sv_btn.setOnAction(event -> onMoveToSavings());
        trans_to_ch_btn.setOnAction(event -> onMoveToChecking());
    }


    private void bindData() {
        Client client = Model.getInstance().getClient();


        ch_acc_num.textProperty().bind(client.checkingAccountProperty().get().accountNumberProperty());
        transaction_limit.textProperty().bind(
                ((CheckingAccount) client.checkingAccountProperty().get()).transactionLimitProperty().asString()
        );
        ch_account_date.textProperty().bind(client.dateCreatedProperty().asString());
        ch_acc_bal.textProperty().bind(client.checkingAccountProperty().get().balanceProperty().asString("$%,.2f"));


        sv_acc_num.textProperty().bind(client.savingsAccountProperty().get().accountNumberProperty());
        withdrawal_limit.textProperty().bind(
                ((SavingsAccount) client.savingsAccountProperty().get()).withdrawalLimitProperty().asString()
        );
        sv_acc_date.textProperty().bind(client.dateCreatedProperty().asString());
        sv_acc_bal.textProperty().bind(client.savingsAccountProperty().get().balanceProperty().asString("$%,.2f"));
    }


    private void onMoveToSavings() {
        String pAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        double amount;
        try {
            amount = Double.parseDouble(amount_to_sv.getText());
        } catch (NumberFormatException e) {
            amount_to_sv.setText("Invalid Amount");
            return;
        }


        double checkingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().get();
        if (amount > 0 && amount <= checkingBalance) {
            double newCheckingBalance = checkingBalance - amount;
            double newSavingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().get() + amount;

            Model.getInstance().getDatabaseDriver().updateCheckingBalance(pAddress, newCheckingBalance);
            Model.getInstance().getDatabaseDriver().updateSavingsBalance(pAddress, newSavingsBalance);

            Model.getInstance().getClient().checkingAccountProperty().get().setBalance(newCheckingBalance);
            Model.getInstance().getClient().savingsAccountProperty().get().setBalance(newSavingsBalance);
        }
        amount_to_sv.clear();
    }


    private void onMoveToChecking() {
        String pAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        double amount;
        try {
            amount = Double.parseDouble(amount_to_ch.getText());
        } catch (NumberFormatException e) {
            amount_to_ch.setText("Invalid Amount");
            return;
        }


        double savingsBalance = Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().get();
        if (amount > 0 && amount <= savingsBalance) {
            double newSavingsBalance = savingsBalance - amount;
            double newCheckingBalance = Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().get() + amount;

            Model.getInstance().getDatabaseDriver().updateSavingsBalance(pAddress, newSavingsBalance);
            Model.getInstance().getDatabaseDriver().updateCheckingBalance(pAddress, newCheckingBalance);

            Model.getInstance().getClient().savingsAccountProperty().get().setBalance(newSavingsBalance);
            Model.getInstance().getClient().checkingAccountProperty().get().setBalance(newCheckingBalance);
        }
        amount_to_ch.clear();
    }
}