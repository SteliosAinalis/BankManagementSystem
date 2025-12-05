package com.stelios.bankmanagementsystem.Views;

import com.stelios.bankmanagementsystem.Controllers.Client.ProfileController;
import com.stelios.bankmanagementsystem.Controllers.Client.SearchResultCellController;
import com.stelios.bankmanagementsystem.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import java.io.IOException;

public class SearchResultCellFactory extends ListCell<Client> {
    private final ProfileController profileController;

    public SearchResultCellFactory(ProfileController profileController) {
        this.profileController = profileController; // ADD THIS
    }




    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if (empty || client == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/SearchResultCell.fxml"));
            SearchResultCellController controller = new SearchResultCellController(client,  profileController);
            loader.setController(controller);

            try {
                setGraphic(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}