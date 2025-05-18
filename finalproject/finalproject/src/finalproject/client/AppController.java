package finalproject.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private Stage primaryStage;
    private PosilacNaServer pns;
    @FXML private TextField username;
    @FXML private TextField password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void connectToServer() {
        pns.connectToServer();
    }

    @FXML
    public void logInAction() {
        if(!username.getText().isEmpty() && !password.getText().isEmpty())
            pns.login(username.getText(), password.getText());
    }

    @FXML
    public void signUpAction() {
        if(!username.getText().isEmpty() && !password.getText().isEmpty())
            pns.signup(username.getText(), password.getText());
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public PosilacNaServer getPns() {
        return pns;
    }

    public void setPns(PosilacNaServer pns) {
        this.pns = pns;
    }
}
