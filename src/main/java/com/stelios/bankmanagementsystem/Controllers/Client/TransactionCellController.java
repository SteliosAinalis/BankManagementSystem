package com.stelios.bankmanagementsystem.Controllers.Client;


import com.stelios.bankmanagementsystem.Models.Transaction;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color; // You may need to import this if you use it elsewhere

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {
    public Label trans_date_lbl;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;

    private final Transaction transaction;

    public TransactionCellController(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trans_date_lbl.textProperty().bind(transaction.dateProperty().asString());
        sender_lbl.textProperty().bind(transaction.senderProperty());
        receiver_lbl.textProperty().bind(transaction.receiverProperty());
        amount_lbl.textProperty().bind(transaction.amountProperty().asString("$%,.2f"));
    }
}