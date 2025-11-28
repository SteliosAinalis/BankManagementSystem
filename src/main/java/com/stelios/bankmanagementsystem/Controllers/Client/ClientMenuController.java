package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

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
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientMenuItem().set("Dashboard");
    }

    private void onTransactions() {
        Model.getInstance().getViewFactory().getClientMenuItem().set("Transactions");
    }

    private void onAccounts() {
        Model.getInstance().getViewFactory().getClientMenuItem().set("Accounts");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        addListeners();







        //       profile image in menu
        String imagePath = "/images/stelios.JPG";
        InputStream inputStream = getClass().getResourceAsStream(imagePath);

        if (inputStream == null) {
            System.err.println("Error: Image not found at path: " + imagePath);
            return;
        }
        Image image = new Image(inputStream);
        profile_image.setImage(image);
        profile_image.setFitWidth(70);
        profile_image.setFitHeight(70);
        double radius = profile_image.getFitWidth() / 2;
        Circle clip = new Circle(radius);
        clip.centerXProperty().bind(profile_image.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profile_image.fitHeightProperty().divide(2));

        profile_image.setClip(clip);
    }
}