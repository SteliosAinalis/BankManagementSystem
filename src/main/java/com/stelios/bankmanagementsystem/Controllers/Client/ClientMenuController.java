package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.ClientMenuOptions;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {

    @FXML
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;
    public ImageView profile_image;

    private void addListeners(){
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        accounts_btn.setOnAction(event -> onAccounts());
        profile_btn.setOnAction(event -> onProfile());
        logout_btn.setOnAction(event -> onLogout());
        report_btn.setOnAction(event -> onReport());
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    private void onTransactions() {
        Model.getInstance().getViewFactory().getClientMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    private void onAccounts() {
        Model.getInstance().getViewFactory().getClientMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    private void onProfile() {
        Model.getInstance().getViewFactory().getClientMenuItem().set(ClientMenuOptions.PROFILE);
    }

    private void onLogout() {
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        Model.getInstance().logout();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    private void onReport() {
        Model.getInstance().getViewFactory().showReportWindow();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        bindProfileImage();

    }

    private void bindProfileImage() {
        var imagePathProperty = Model.getInstance().getClient().profileImagePathProperty();

        profile_image.imageProperty().bind(Bindings.createObjectBinding(() -> {
            String path = imagePathProperty.get();
            if (path != null && !path.isEmpty()) {
                try {
                    return new Image(getClass().getResourceAsStream(path));
                } catch (Exception e) {
                    System.err.println("Menu: Failed to load image from resource: " + path);
                }
            }
            return new Image(getClass().getResourceAsStream("/images/profile_pics/default.jpg"));
        }, imagePathProperty));
        Circle clip = new Circle();
        clip.radiusProperty().bind(profile_image.fitWidthProperty().divide(2));
        clip.centerXProperty().bind(profile_image.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profile_image.fitHeightProperty().divide(2));
        profile_image.setClip(clip);
    }


}