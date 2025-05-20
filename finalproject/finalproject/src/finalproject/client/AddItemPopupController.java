package finalproject.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AddItemPopupController {

    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField priceField;

    private File selectedImage;
    private String name;
    private String description;
    private int price;
    private byte[] image;

    @FXML
    private void onChooseImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        selectedImage = fileChooser.showOpenDialog(null);
    }

    @FXML
    private void onAddAction(ActionEvent event) {
        try {
            if (selectedImage == null || titleField.getText().isEmpty() || descField.getText().isEmpty() || priceField.getText().isEmpty()) {
                showAlert("Please fill all fields and choose an image");
                return;
            }

            image = Files.readAllBytes(selectedImage.toPath());
            name = titleField.getText();
            description = descField.getText();
            price = Integer.parseInt(priceField.getText());
            ((Stage) titleField.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert("Failed to read image file.");
        }
    }

    @FXML
    private void onCancelAction(ActionEvent event) {
        name = null;
        ((Stage) titleField.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }


    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}