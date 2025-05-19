package finalproject.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Stage primaryStage;
    private PosilacNaServer pns;
    @FXML private TextField username;
    @FXML private TextField password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pns.connectToServer();
    }


    //pridat account text!!!
    @FXML
    public void logInAction(ActionEvent event) {
        String response;
        if(username.getText().isEmpty() || password.getText().isEmpty())
            //alert na empty udaje do funkce
            return;
        response = pns.login(username.getText(), password.getText());
        if(!response.equals("OK"))
            //alert error signup
            return;
        changeToMainScene(username.getText(), event);
    }

    @FXML
    public void signUpAction() {
        String response;
        if(username.getText().isEmpty() || password.getText().isEmpty())
            //alert na empty udaje do
            return;
        response = pns.signup(username.getText(), password.getText());
        if(response.equals("OK"))
            return;
            //alert signed up successfully
    }

    private void changeToMainScene(final String username, final ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            MainController controller = loader.getController();
            controller.setPns(pns);
            controller.setUser(username);
            Parent parent = loader.load();

            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            //alert error
        }
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
