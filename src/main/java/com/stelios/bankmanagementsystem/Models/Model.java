package com.stelios.bankmanagementsystem.Models;

import com.stelios.bankmanagementsystem.Views.AccountType;
import com.stelios.bankmanagementsystem.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private final ObservableList<Client> clients;


    //client
    private Client client;
    private boolean clientLoginSuccess;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;

    //Admin
    private boolean adminLoginSuccess;



    public Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.adminLoginSuccess = false;
        this.clientLoginSuccess = false;
        this.client = new Client("","","",null,null,null,"");
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();
        this.clients = FXCollections.observableArrayList();
    }


    public static synchronized Model getInstance() {
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {return viewFactory;}

    public DatabaseDriver getDatabaseDriver() {return databaseDriver;}





    //Client methods
    public boolean getClientLoginSuccess() {return this.clientLoginSuccess;}
    public void setClientLoginSuccess(boolean flag) {this.clientLoginSuccess = flag;}

    public Client getClient() {return client;}

    public void evaluateClientCredentials(String pAddress, String password){
        this.clientLoginSuccess = false;

        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = databaseDriver.getClientData(pAddress, password);
        try{
            if(resultSet.isBeforeFirst()){
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.payeeAddressProperty().set(resultSet.getString("PayeeAddress"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
                this.client.dateCreatedProperty().set(date);
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                String imagePath = resultSet.getString("ProfileImagePath");
                this.client.profileImagePathProperty().set(imagePath);
                this.client.checkingAccountProperty().set(checkingAccount);
                this.client.savingsAccountProperty().set(savingsAccount);
                this.clientLoginSuccess = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions, int limit) {
        ResultSet resultSet = databaseDriver.getTransactions(this.client.payeeAddressProperty().get(), limit);
        try{
            while (resultSet.next()){
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
                String message = resultSet.getString("Message");
                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setLatestTransactions(){
        prepareTransactions(this.latestTransactions, 4);
    }

    public ObservableList<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    public void setAllTransactions(){
        prepareTransactions(this.allTransactions, -1);
    }

    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }





    //Admin Methods

    public boolean getAdminLoginSuccess() {return this.adminLoginSuccess;}
    public void setAdminLoginSuccess(boolean flag) {this.adminLoginSuccess = flag;}

    public void evaluateAdminCredentials(String username, String password){
        this.adminLoginSuccess = false;

        ResultSet resultSet = databaseDriver.getAdminData(username, password);
        try{
            if(resultSet.isBeforeFirst()){
                this.adminLoginSuccess = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<Client> getClients() {return clients;}

    public void setClients() {
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = databaseDriver.getAllClientsData();
        try{
            while (resultSet.next()){
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                String imagePath = resultSet.getString("ProfileImagePath");
                clients.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date,  imagePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> searchClient(String pAddress) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try{
            CheckingAccount checkingAccount = getCheckingAccount(pAddress);
            SavingsAccount savingsAccount = getSavingsAccount(pAddress);
            String fName = resultSet.getString("FirstName");
            String lName = resultSet.getString("LastName");
            String[] dateParts = resultSet.getString("Date").split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
            String imagePath = resultSet.getString("ProfileImagePath");
            searchResults.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date,  imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResults;
    }









    //other methods

    public CheckingAccount getCheckingAccount(String pAddress) {
        CheckingAccount account = null;
        ResultSet resultSet = databaseDriver.getCheckingAccountsData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int tLimit = (int) resultSet.getDouble("TransactionLimit");
            double balance = resultSet.getDouble("Balance");
            account = new CheckingAccount(pAddress, num, balance, tLimit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }


    public SavingsAccount getSavingsAccount(String pAddress) {
        SavingsAccount account = null;
        ResultSet resultSet = databaseDriver.getSavingsAccountsData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int wLimit = (int) resultSet.getDouble("WithdrawalLimit");
            double balance = resultSet.getDouble("Balance");
            account = new SavingsAccount(pAddress, num, balance, wLimit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }









}
