package com.stelios.bankmanagementsystem.Controllers.Admin;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Models.Report;
import com.stelios.bankmanagementsystem.Views.ReportCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    public ListView<Report> reports_listview;

    private final ObservableList<Report> reports;

    public ReportsController() {
        this.reports = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reports_listview.setItems(reports);
        reports_listview.setCellFactory(e -> new ReportCellFactory(this));
        refreshReportsList();
    }

    public void refreshReportsList() {
        reports.clear();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getAllReports();
        try {
            while (resultSet.next()) {
                reports.add(new Report(
                        resultSet.getInt("ID"),
                        resultSet.getString("ClientAddress"),
                        resultSet.getString("Message"),
                        LocalDate.parse(resultSet.getString("Date"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}