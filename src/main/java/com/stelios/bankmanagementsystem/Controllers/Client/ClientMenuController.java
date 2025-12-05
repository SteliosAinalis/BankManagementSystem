package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.ClientMenuOptions;
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
    public ImageView profile_image;

    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        accounts_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogout());
        profile_btn.setOnAction(event -> onProfile());
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        bindProfileImage();

    }

    private void bindProfileImage() {

        String imagePath = Model.getInstance().getClient().profileImagePathProperty().get();


        if (imagePath != null && !imagePath.isEmpty()) {

            profile_image.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } else {

            profile_image.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
        }


        double radius = profile_image.getFitWidth() / 2;
        Circle clip = new Circle(radius);
        clip.centerXProperty().bind(profile_image.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profile_image.fitHeightProperty().divide(2));
        profile_image.setClip(clip);
    }

}