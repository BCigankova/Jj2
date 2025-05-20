package finalproject.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ChangeStuffPopupController {

    @FXML private TextField text;

    private String newValue;

    @FXML
    private void onSubmitAction(ActionEvent event) {
        if (text.getText().isEmpty()) {
            showAlert("Please write new value");
            return;
        }

        newValue = text.getText();
        ((Stage) text.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelAction(ActionEvent event) {
        newValue = null;
        ((Stage) text.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public String getNewValue() {
        return newValue;
    }
}