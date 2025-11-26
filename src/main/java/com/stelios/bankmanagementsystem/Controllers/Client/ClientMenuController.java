package com.stelios.bankmanagementsystem.Controllers.Client;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // --- DIAGNOSTICS START ---
        String imagePath = "/images/stelios.JPG";
        InputStream inputStream = getClass().getResourceAsStream(imagePath);

        if (inputStream == null) {
            System.err.println("Error: Image not found at path: " + imagePath);
            // You could set a default placeholder image here if you want
            return; // Exit the method if the image can't be found
        }
        // --- DIAGNOSTICS END ---

        Image image = new Image(inputStream);
        profile_image.setImage(image);

        // Ensure the ImageView has a size. Set this in Scene Builder or here.
        profile_image.setFitWidth(70);
        profile_image.setFitHeight(70);

        // The radius should be half of the fitWidth/fitHeight
        double radius = profile_image.getFitWidth() / 2;
        Circle clip = new Circle(radius);

        // Bind the center of the clip to the center of the ImageView
        clip.centerXProperty().bind(profile_image.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profile_image.fitHeightProperty().divide(2));

        profile_image.setClip(clip);
    }
}