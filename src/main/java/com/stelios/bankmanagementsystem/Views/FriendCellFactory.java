package com.stelios.bankmanagementsystem.Views;

import com.stelios.bankmanagementsystem.Controllers.Client.FriendCellController;
import com.stelios.bankmanagementsystem.Controllers.Client.ProfileController;
import com.stelios.bankmanagementsystem.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import java.io.IOException;

public class FriendCellFactory extends ListCell<Client> {
    private final ProfileController profileController;

    public FriendCellFactory(ProfileController profileController) {
        this.profileController = profileController;
    }

    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if (empty || client == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/FriendCell.fxml"));
            FriendCellController controller = new FriendCellController(client, profileController);
            loader.setController(controller);

            try {
                setGraphic(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}