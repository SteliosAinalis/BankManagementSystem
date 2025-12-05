package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchResultCellController implements Initializable {
    public Text name_lbl;
    public Button add_friend_btn;

    private final Client client;
    private final ProfileController profileController;

    public SearchResultCellController(Client client,  ProfileController profileController) {
        this.client = client;
        this.profileController = profileController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_lbl.setText(client.firstNameProperty().get() + " " + client.lastNameProperty().get());
        add_friend_btn.setOnAction(event -> onAddFriend());
    }

    private void onAddFriend() {
        String currentUserAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        String friendAddress = client.payeeAddressProperty().get();
        Model.getInstance().getDatabaseDriver().addFriend(currentUserAddress, friendAddress);
        add_friend_btn.setText("Added");
        add_friend_btn.setDisable(true);
        profileController.refreshFriendsList();

    }
}