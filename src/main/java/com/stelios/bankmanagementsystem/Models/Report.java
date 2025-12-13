package com.stelios.bankmanagementsystem.Models;


import javafx.beans.property.*;
import java.time.LocalDate;

public class Report {
    private final IntegerProperty id;
    private final StringProperty clientAddress;
    private final StringProperty message;
    private final ObjectProperty<LocalDate> date;

    public Report(int id, String clientAddress, String message, LocalDate date) {
        this.id = new SimpleIntegerProperty(this, "ID", id);
        this.clientAddress = new SimpleStringProperty(this, "ClientAddress", clientAddress);
        this.message = new SimpleStringProperty(this, "Message", message);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty clientAddressProperty() { return clientAddress; }
    public StringProperty messageProperty() { return message; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

}
