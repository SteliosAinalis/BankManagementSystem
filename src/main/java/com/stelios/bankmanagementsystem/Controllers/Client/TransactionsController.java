package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Models.Transaction;
import com.stelios.bankmanagementsystem.Views.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {
    public ListView<Transaction> transactions_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Model.getInstance().getAllTransactions().isEmpty()) {
            Model.getInstance().setAllTransactions();
        }
        transactions_listview.setItems(Model.getInstance().getAllTransactions());
        transactions_listview.setCellFactory(e -> new TransactionCellFactory());
    }
}