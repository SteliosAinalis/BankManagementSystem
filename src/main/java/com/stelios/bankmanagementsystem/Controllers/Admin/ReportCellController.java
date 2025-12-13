package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Models.Report;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportCellController implements Initializable {
    public Label sender_lbl;
    public Label date_lbl;
    public Button delete_btn;
    public Text message_txt;

    private final Report report;
    private final ReportsController reportsController;

    public ReportCellController(Report report, ReportsController reportsController) {
        this.report = report;
        this.reportsController = reportsController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sender_lbl.textProperty().bind(report.clientAddressProperty());
        date_lbl.textProperty().bind(report.dateProperty().asString());
        message_txt.textProperty().bind(report.messageProperty());
        delete_btn.setOnAction(event -> onDelete());
    }

    private void onDelete() {
        Model.getInstance().getDatabaseDriver().deleteReport(report.idProperty().get());
        reportsController.refreshReportsList();
    }
}