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
