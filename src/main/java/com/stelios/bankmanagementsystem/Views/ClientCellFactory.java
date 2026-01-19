package com.stelios.bankmanagementsystem.Views;

import com.stelios.bankmanagementsystem.Controllers.Admin.ClientCellController;
import com.stelios.bankmanagementsystem.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ClientCellFactory extends ListCell<Client> {

    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClientCell.fxml"));
            ClientCellController controller = new ClientCellController(client);
            loader.setController(controller);
            setText(null);
            try{
                setGraphic(loader.load());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        updateSelectionStyle();
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        updateSelectionStyle();
    }

    private void updateSelectionStyle() {
        if (getGraphic() != null) {
            if (isSelected()) {
                getGraphic().setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 10;");
            } else {
                getGraphic().setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            }
        }
    }
}
