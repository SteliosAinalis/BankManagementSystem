package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Models.Transaction;
import com.stelios.bankmanagementsystem.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;
    public ToggleGroup snapshot_toggle_group;
    public ToggleButton trend_btn;
    public ToggleButton breakdown_btn;
    public LineChart<String, Number> net_worth_chart;
    public PieChart spending_pie_chart;
    public PieChart income_pie_chart;
    public ToggleButton income_btn;
    public ToggleButton spending_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userData();
        initiallizeLatestTransactions();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        send_money_btn.setOnAction(e -> onSendMoney());

        snapshot_toggle_group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            ToggleButton selectedBtn = (ToggleButton) newVal;
            if (selectedBtn != null) {
                if (selectedBtn.equals(income_btn)) {
                    income_pie_chart.setVisible(true);
                    spending_pie_chart.setVisible(false);
                    populateIncomeByPayeeChart();
                } else {
                    income_pie_chart.setVisible(false);
                    spending_pie_chart.setVisible(true);
                    populateSpendingByPayeeChart();
                }
            }
        });
        populateIncomeByPayeeChart();
    }



    private void userData() {
        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getClient().firstNameProperty()));
        login_date.setText("Today, " + LocalDate.now());
        checking_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        savings_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
        savings_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
    }

    private void initiallizeLatestTransactions() {
        if (Model.getInstance().getLatestTransactions().isEmpty()) {
            Model.getInstance().setLatestTransactions();
        }
    }

    private void onSendMoney() {
        String receiver = payee_fld.getText();
        double amount = Double.parseDouble(amount_fld.getText());
        String message = message_fld.getText();
        String sender = Model.getInstance().getClient().payeeAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()) {
                Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount, "ADD");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error sending Money in Dashboard");
        }

        Model.getInstance().getDatabaseDriver().updateBalance(sender, amount, "SUBFROMSENDER");
        try {
            Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender));
        } catch (Exception e) {
            System.out.println("Error updating savings account");
        }
        Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);

        payee_fld.clear();
        amount_fld.clear();
        message_fld.clear();

    }


    private void accountSummary() {
        double income = 0;
        double expenses = 0;
        if (Model.getInstance().getAllTransactions().isEmpty()) {
            Model.getInstance().setAllTransactions();
        }
        for (Transaction transaction : Model.getInstance().getAllTransactions()) {
            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                expenses = expenses + transaction.amountProperty().get();
            } else {
                income = income + transaction.amountProperty().get();
            }
        }
        income_lbl.setText("+$" + income);
        expense_lbl.setText("-$" + expenses);

    }

    private void populateIncomeByPayeeChart() {
        String currentUserAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getIncomeByPayee(currentUserAddress);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            while (resultSet != null && resultSet.next()) {
                String sender = resultSet.getString("Sender");
                double totalReceived = resultSet.getDouble("TotalReceived");
                pieChartData.add(new PieChart.Data(sender, totalReceived));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet);
        }

        income_pie_chart.setData(pieChartData);
        income_pie_chart.setTitle("Income by Payee");
    }

    private void populateSpendingByPayeeChart() {
        String currentUserAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSpendingByPayee(currentUserAddress);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            while (resultSet != null && resultSet.next()) {
                String receiver = resultSet.getString("Receiver");
                double totalSent = resultSet.getDouble("TotalSent");
                pieChartData.add(new PieChart.Data(receiver, totalSent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet);
        }
        spending_pie_chart.setData(pieChartData);
        spending_pie_chart.setTitle("Spending by Payee");
    }

    private void closeResources(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.getStatement().close();
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}