package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class FriendCellController implements Initializable {
    public ImageView profile_pic_image;
    public Label payee_address_lbl;
    public Button remove_friend_btn;

    private final Client client;
    private final ProfileController profileController;


    public FriendCellController(Client client, ProfileController profileController) {
        this.client = client;
        this.profileController = profileController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        payee_address_lbl.textProperty().bind(client.payeeAddressProperty());
        remove_friend_btn.setOnAction(event -> onRemoveFriend());

        String imagePath = client.profileImagePathProperty().get();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                profile_pic_image.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            } catch (Exception e) {
                loadDefaultImage();
            }
        } else {
            loadDefaultImage();
        }
    }

    private void onRemoveFriend() {
        String currentUserAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        String friendAddress = client.payeeAddressProperty().get();
        Model.getInstance().getDatabaseDriver().removeFriend(currentUserAddress, friendAddress);
        profileController.refreshFriendsList();
    }

    private void loadDefaultImage() {
        profile_pic_image.setImage(new Image(getClass().getResourceAsStream("/images/profile_pics/default.jpg")));
    }
}