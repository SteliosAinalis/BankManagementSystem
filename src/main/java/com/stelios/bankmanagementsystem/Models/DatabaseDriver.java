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


    public ResultSet getTransactions(String pAddress, int limit){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Transactions WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"' LIMIT "+limit+";");
        }catch(SQLException e){
            System.err.println("DATABASE ERROR executing getTransactions query.");
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
