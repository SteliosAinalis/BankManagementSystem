package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    public TextArea report_textarea;
    public Label char_counter_lbl;
    public Label message_lbl;
    public Button submit_btn;

    private static final int MAX_CHARS = 200;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit_btn.setOnAction(event -> onSubmit());

        report_textarea.textProperty().addListener((observable, oldValue, newValue) -> {
            int length = newValue.length();
            char_counter_lbl.setText(length + "/" + MAX_CHARS);
            if (length > MAX_CHARS) {
                char_counter_lbl.setStyle("-fx-text-fill: red;");
            } else {
                char_counter_lbl.setStyle("-fx-text-fill: black;");
            }
        });
    }

    private void onSubmit() {
        String message = report_textarea.getText();

        if (message.isBlank()) {
            showMessage("Report cannot be empty.", true);
            return;
        }
        if (message.length() > MAX_CHARS) {
            showMessage("Report exceeds 200 characters.", true);
            return;
        }

        String pAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        Model.getInstance().getDatabaseDriver().submitReport(pAddress, message);
        showMessage("Report submitted successfully!", false);
        submit_btn.setDisable(true);
        closeWindowAfterDelay();
    }

    private void closeWindowAfterDelay() {
        KeyFrame kf = new KeyFrame(Duration.seconds(1), event -> {
            Stage stage = (Stage) submit_btn.getScene().getWindow();
            stage.close();
        });
        Timeline timeline = new Timeline(kf);
        timeline.play();
    }

    private void showMessage(String message, boolean isError) {
        message_lbl.setText(message);
        message_lbl.getStyleClass().removeAll("success", "error");
        if (isError) {
            message_lbl.getStyleClass().add("error");
        } else {
            message_lbl.getStyleClass().add("success");
        }
    }
}