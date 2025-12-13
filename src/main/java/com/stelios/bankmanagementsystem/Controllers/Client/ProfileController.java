package com.stelios.bankmanagementsystem.Controllers.Client;

import com.stelios.bankmanagementsystem.Models.Client;
import com.stelios.bankmanagementsystem.Models.Model;
import com.stelios.bankmanagementsystem.Views.FriendCellFactory;
import com.stelios.bankmanagementsystem.Views.SearchResultCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public ImageView profile_image;
    public Button change_picture_btn;
    public PasswordField new_password_fld;
    public PasswordField confirm_password_fld;
    public Button save_password_btn;
    public Label message_lbl;
    public ListView<Client> friends_listview;
    public TextField search_fld;
    public Button search_btn;
    public ListView<Client> search_results_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getClient().payeeAddressProperty().addListener((observable, oldAddress, newAddress) -> {
            if (newAddress != null && !newAddress.isEmpty()) {
                updateProfilePictureUI(Model.getInstance().getClient().profileImagePathProperty().get());
                loadFriendsList();
            }
        });
        updateProfilePictureUI(Model.getInstance().getClient().profileImagePathProperty().get());
        loadFriendsList();
        save_password_btn.setOnAction(event -> onSavePassword());
        change_picture_btn.setOnAction(event -> onChangePicture());
        search_btn.setOnAction(event -> onSearch());
    }

    private void updateProfilePictureUI(String imagePath) {
        Image image = null;
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                if (imagePath.startsWith("/") && !imagePath.contains("src/main/resources")) {
                    image = new Image(getClass().getResourceAsStream(imagePath));
                } else {
                    image = new Image(new File(imagePath).toURI().toString());
                }
            } catch (Exception e) {
                System.err.println("Error loading image, falling back to default. Path: " + imagePath);
            }
        }

        if (image == null || image.isError()) {
            image = new Image(getClass().getResourceAsStream("/images/profile_pics/default.jpg"));
            change_picture_btn.setText("Set Up Profile Picture");
        } else {
            change_picture_btn.setText("Change Picture");
        }

        profile_image.setImage(image);
        Circle clip = new Circle(40.0);
        clip.centerXProperty().bind(profile_image.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profile_image.fitHeightProperty().divide(2));
        profile_image.setClip(clip);
    }

    private void onChangePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) profile_image.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                String pAddress = Model.getInstance().getClient().payeeAddressProperty().get();
                File destDir = new File("src/main/resources/images/profile_pics");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                String originalFileName = selectedFile.getName();
                File destinationFile = new File(destDir.getPath() + "/" + originalFileName);
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                String dbPath = "/images/profile_pics/" + destinationFile.getName();
                String absolutePath = destinationFile.getAbsolutePath();

                Model.getInstance().getDatabaseDriver().updateClientProfileImagePath(pAddress, dbPath);
                Model.getInstance().getClient().profileImagePathProperty().set(dbPath);
                updateProfilePictureUI(absolutePath);
                showMessage("Profile picture updated!", false);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error updating picture.", true);
            }
        }
    }

    private void onSearch() {
        String searchTerm = search_fld.getText();
        if (searchTerm.isBlank()) {
            return;
        }
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClientsByFirstName(searchTerm);
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        try {
            while (resultSet != null && resultSet.next()) {
                searchResults.add(createClientFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet);
        }
        search_results_listview.setItems(searchResults);
        search_results_listview.setCellFactory(e -> new SearchResultCellFactory(this));
        if (searchResults.isEmpty()) {
            showMessage("No clients found with that name.", true);
        } else {
            message_lbl.setText("");
        }
    }

    public void loadFriendsList() {
        String currentUserAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getFriends(currentUserAddress);
        ObservableList<Client> friends = FXCollections.observableArrayList();
        try {
            while (resultSet != null && resultSet.next()) {
                friends.add(createClientFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet);
        }
        friends_listview.setItems(friends);
        friends_listview.setCellFactory(e -> new FriendCellFactory(this));
    }

    private Client createClientFromResultSet(ResultSet resultSet) throws SQLException {
        String fName = resultSet.getString("FirstName");
        String lName = resultSet.getString("LastName");
        String pAddress = resultSet.getString("PayeeAddress");
        String imagePath = resultSet.getString("ProfileImagePath");
        String[] dateParts = resultSet.getString("Date").split("-");
        LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
        return new Client(fName, lName, pAddress, null, null, date, imagePath);
    }

    private void closeResources(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.getStatement().close();
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void onSavePassword() {
        String pAddress = Model.getInstance().getClient().payeeAddressProperty().get();
        String newPassword = new_password_fld.getText();
        String confirmPassword = confirm_password_fld.getText();
        if (newPassword.isBlank() || !newPassword.equals(confirmPassword)) {
            showMessage("Passwords do not match or are empty.", true);
            return;
        }
        Model.getInstance().getDatabaseDriver().updateClientPassword(pAddress, newPassword);
        showMessage("Password changed successfully!", false);
        new_password_fld.clear();
        confirm_password_fld.clear();
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) return "";
        return name.substring(lastIndexOf + 1);
    }

    private void showMessage(String message, boolean isError) {
        message_lbl.setText(message);
        message_lbl.getStyleClass().removeAll("success", "error");
        if (isError) {
            message_lbl.getStyleClass().add("error");
        } else {
            message_lbl.getStyleClass().add("success");
        }
    }

    public void refreshFriendsList() {
        loadFriendsList();
    }
}