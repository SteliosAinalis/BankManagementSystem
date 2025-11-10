module com.stelios.bankmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stelios.bankmanagementsystem to javafx.fxml;
    exports com.stelios.bankmanagementsystem;
}