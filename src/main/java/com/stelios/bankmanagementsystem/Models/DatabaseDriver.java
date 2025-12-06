package com.stelios.bankmanagementsystem.Models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver() {
        try{
            this.connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
        } catch (SQLException e) {
            System.err.println("Error connecting to database.");
        }
    }





    //Client
    public ResultSet getClientData(String pAddress, String password){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '" + pAddress + "' AND Password = '" + password + "';");

        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing getClientData query.");
        }
        return resultSet;
    }


    public ResultSet getTransactions(String pAddress, int limit) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Transactions " +
                    "WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"' " +
                    "ORDER BY Date DESC LIMIT "+limit+";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public double getSavingsAccountBalance(String pAddress){
        Statement statement;
        ResultSet resultSet;
        double balance =0;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
            balance = resultSet.getDouble("Balance");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing getSavingsAccount query.");
        }
        return balance;
    }


    public void updateBalance(String pAddress, double amount, String action){
        Statement statement;
        ResultSet resultSet;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
            double newBalance;
            if(action.equals("ADD")){
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+newBalance+" WHERE Owner='"+pAddress+"';");
            }else {
                if (resultSet.getDouble("Balance") >= amount) {
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+newBalance+" WHERE Owner='"+pAddress+"';");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating balance.");
        }
    }



    public void newTransaction(String sender, String receiver, double amount, String message){
        Statement statement;
        try{
            statement = this.connection.createStatement();
            LocalDate date = LocalDate.now();
            statement.executeUpdate("INSERT INTO " +
             "Transactions(Sender, Receiver, Amount, Date, Message)" +
            "VALUES ('"+sender+"', '"+receiver+"', '"+amount+"', '"+date+"', '"+message+"');");
        }catch (SQLException e){
            System.err.println("Error executing newTransaction.");
        }
    }


    public double getCheckingAccountBalance(String pAddress) {
        Statement statement;
        ResultSet resultSet;
        double balance = 0;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT Balance FROM CheckingAccounts WHERE Owner='"+pAddress+"';");
            if (resultSet.next()){
                balance = resultSet.getDouble("Balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void updateCheckingBalance(String pAddress, double newBalance) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("UPDATE CheckingAccounts SET Balance="+newBalance+" WHERE Owner='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSavingsBalance(String pAddress, double newBalance) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+newBalance+" WHERE Owner='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void updateClientPassword(String pAddress, String newPassword) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("UPDATE Clients SET Password='"+newPassword+"' WHERE PayeeAddress='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClientProfileImagePath(String pAddress, String path) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("UPDATE Clients SET ProfileImagePath='"+path+"' WHERE PayeeAddress='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public ResultSet searchClientsByFirstName(String firstName) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            // Using LIKE allows for partial matches (e.g., "Stel" will find "Stelios")
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE FirstName LIKE '%"+firstName+"%';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addFriend(String clientAddress, String friendAddress) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            // Add the friendship in both directions to make it mutual
            statement.executeUpdate("INSERT INTO Friends(ClientAddress, FriendAddress) VALUES ('"+clientAddress+"', '"+friendAddress+"');");
            statement.executeUpdate("INSERT INTO Friends(ClientAddress, FriendAddress) VALUES ('"+friendAddress+"', '"+clientAddress+"');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getFriends(String clientAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            // This query joins the Friends and Clients tables to get the full details of each friend
            resultSet = statement.executeQuery("SELECT c.* FROM Friends f JOIN Clients c ON f.FriendAddress = c.PayeeAddress WHERE f.ClientAddress = '"+clientAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getIncomeByPayee(String receiverAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            // This query groups all transactions by the SENDER and sums the amounts
            resultSet = statement.executeQuery("SELECT Sender, SUM(Amount) as TotalReceived " +
                    "FROM Transactions " +
                    "WHERE Receiver = '"+receiverAddress+"' " +
                    "GROUP BY Sender;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public double getTotalIncome(String pAddress) {
        Statement statement;
        ResultSet resultSet;
        double total = 0;
        try {
            statement = this.connection.createStatement();
            // The SUM(Amount) function calculates the total directly in the database
            resultSet = statement.executeQuery("SELECT SUM(Amount) FROM Transactions WHERE Receiver='"+pAddress+"';");
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public double getTotalExpenses(String pAddress) {
        Statement statement;
        ResultSet resultSet;
        double total = 0;
        try {
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT SUM(Amount) FROM Transactions WHERE Sender='"+pAddress+"';");
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void submitReport(String pAddress, String message) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            LocalDate date = LocalDate.now();
            // Note: A PreparedStatement would be more secure against SQL Injection here.
            statement.executeUpdate("INSERT INTO Reports(ClientAddress, Message, Date) " +
                    "VALUES ('"+pAddress+"', '"+message+"', '"+date.toString()+"');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    //Admin
    public ResultSet getAdminData(String username, String password){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username='"+username+"' AND Password= '"+password + "';");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing getAdminData query.");
        }
        return resultSet;
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date){
        Statement statement;
        try{
            statement = this.connection.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Clients(FirstName, LastName, PayeeAddress, Password, Date)"+
                    "VALUES ('"+fName+"', '"+lName+"', '"+pAddress+"', '"+password+"', '"+date.toString()+"');");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing createClient query.");
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance){
        Statement statement;
        try{
            statement = this.connection.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "CheckingAccounts(Owner, AccountNumber, TransactionLimit, Balance)"+
                    "VALUES ('"+owner+"', '"+number+"', '"+tLimit+"', '"+balance+"');");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing createCheckingAccount query.");
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance){
        Statement statement;
        try{
            statement = this.connection.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "SavingsAccounts(Owner, AccountNumber, WithdrawalLimit, Balance)"+
                    "VALUES ('"+owner+"', '"+number+"', '"+wLimit+"', '"+balance+"');");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing createSavingsAccount query.");
        }
    }

    public ResultSet getAllClientsData(){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients;");
        } catch (SQLException e) {
            System.err.println("DATABASE ERROR executing get all clients data query.");
        }
        return resultSet;
    }


    public void removeFriend(String clientAddress, String friendAddress) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM Friends WHERE ClientAddress='"+clientAddress+"' AND FriendAddress='"+friendAddress+"';");
            statement.executeUpdate("DELETE FROM Friends WHERE ClientAddress='"+friendAddress+"' AND FriendAddress='"+clientAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getSpendingByPayee(String senderAddress) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.connection.createStatement();
            // This query groups all transactions by the receiver and sums the amounts
            resultSet = statement.executeQuery("SELECT Receiver, SUM(Amount) as TotalSent " +
                    "FROM Transactions " +
                    "WHERE Sender = '"+senderAddress+"' " +
                    "GROUP BY Receiver;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void deleteClient(String pAddress) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM CheckingAccounts WHERE Owner='"+pAddress+"';");
            statement.executeUpdate("DELETE FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
            statement.executeUpdate("DELETE FROM Transactions WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"';");
            statement.executeUpdate("DELETE FROM Friends WHERE ClientAddress='"+pAddress+"' OR FriendAddress='"+pAddress+"';");
            statement.executeUpdate("DELETE FROM Clients WHERE PayeeAddress='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    //Methods
    public int getLastClientsId(){
        Statement statement;
        ResultSet resultSet;
        int id = 0;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM sqlite_sequence WHERE name='Clients'");
            id = resultSet.getInt("seq");
        }catch(SQLException e) {
            System.err.println("DATABASE ERROR executing getLastClientsId query.");
        }
        return id;
    }

    public ResultSet getCheckingAccountsData(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner='"+pAddress+"';");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing getCheckingAccountsData query.");
        }
        return resultSet;

    }


    public ResultSet getSavingsAccountsData(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing getSavingsAccountsData query.");
        }
        return resultSet;

    }

    public ResultSet searchClient(String pAddress){
        Statement statement;
        ResultSet resultSet = null;

        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"';");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing searchClient query.");
        }
        return resultSet;
    }


    public void depositSavings(String pAddress, double amount){
        Statement statement;
        try{
            statement = this.connection.createStatement();
            statement.executeUpdate("UPDATE SavingsAccounts SET Balance="+amount+" WHERE Owner='"+pAddress+"';");
        } catch (SQLException e) {
            System.err.println("DATABASE ERROR executing depositSavings query.");
        }
    }



}
