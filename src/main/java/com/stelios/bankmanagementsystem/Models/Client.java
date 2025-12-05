package com.stelios.bankmanagementsystem.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final ObjectProperty<Account> checkingAccount;
    private final ObjectProperty<Account> savingsAccount;
    private final ObjectProperty<LocalDate> dateCreated;
    private final StringProperty profileImagePath;


    public Client(String fName, String lName, String pAddress, Account cAccount, Account sAccount, LocalDate date, String imagePath) {
        this.firstName = new SimpleStringProperty(this, "FirstName", fName);
        this.lastName = new SimpleStringProperty(this, "LastName", lName);
        this.payeeAddress = new SimpleStringProperty(this, "PayeeAddress", pAddress);
        this.checkingAccount = new SimpleObjectProperty<>(this, "CheckingAccount", cAccount);
        this.savingsAccount = new SimpleObjectProperty<>(this, "SavingsAccount", sAccount);
        this.dateCreated = new SimpleObjectProperty<>(this, "DateCreated", date);
        this.profileImagePath = new SimpleStringProperty(this, "ProfileImagePath", imagePath);

    }

    public StringProperty firstNameProperty() {return firstName;}
    public StringProperty lastNameProperty() {return lastName;}
    public StringProperty payeeAddressProperty() {return payeeAddress;}
    public ObjectProperty<Account> checkingAccountProperty() {return checkingAccount;}
    public ObjectProperty<Account> savingsAccountProperty() {return savingsAccount;}
    public ObjectProperty<LocalDate> dateCreatedProperty() {return dateCreated;}
    public StringProperty profileImagePathProperty() {
        return profileImagePath;
    }


}
