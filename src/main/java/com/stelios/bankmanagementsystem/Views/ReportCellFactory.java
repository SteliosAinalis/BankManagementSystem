package com.stelios.bankmanagementsystem.Views;

import com.stelios.bankmanagementsystem.Controllers.Admin.ReportCellController;
import com.stelios.bankmanagementsystem.Controllers.Admin.ReportsController;
import com.stelios.bankmanagementsystem.Models.Report;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import java.io.IOException;

public class ReportCellFactory extends ListCell<Report> {
    private final ReportsController reportsController;

    public ReportCellFactory(ReportsController reportsController) {
        this.reportsController = reportsController;
    }

    @Override
    protected void updateItem(Report report, boolean empty) {
        super.updateItem(report, empty);
        if (empty || report == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ReportsCell.fxml"));

            ReportCellController controller = new ReportCellController(report, reportsController);
            loader.setController(controller);

            try {
                setGraphic(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}