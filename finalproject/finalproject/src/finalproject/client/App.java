package finalproject.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private PosilacNaServer pns;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        pns = new PosilacNaServer("127.0.0.1", 4321);

        FXMLLoader loader = new FXMLLoader(App.class.getResource("/LoginWindow.fxml"));
        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setPns(pns);
        Parent root = loader.load();

        primaryStage.setTitle("App");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
    }


    //nejaky exit kdyz zmacknu exit
}

    /*
    TODO:   xml
                dodelat metody pns pro itemy, pak dv a db statementy nove
            password hashing
            logika client
            vykreslovani client
            vsude pohandlovat exceptiony (SQLWrapper + aby to vyhodilo nejaky alert a nespadlo)
            pridat account policko u signupu a moznost zmenit account a password v db a obecne
     */


/*
Uzivatel zapne apku
Tim se pripoji na server
Server ma delac veci
Delac veci je pripojen k db
Uzivatel se prihlasi -> controller posle jmeno a heslo serveru, ktery to preda delaci, ktery to overi
Delac preda serveru uspech nebo ne, server to posle do apky + data o uzivateli (jeho itemy k prodeji)
Uspech -> zmeni to na logged in: user a tlacitko na logout
Tlacitka na pridani itemu -> popup window? nebo jina stranka
Tlacitko na odstraneni itemu ke kazdemu itemu -> server -> dv odstrani z db
Tlacitko na prepnuti do shop modu: posle request serveru -> delac veci -> db na itemy uzivatelu (krome sveho)
Tlacitko na koupi itemu ke kazdemu itemu -> server -> dv odstrani z db a prida do uzivatelovy db (+ popup opravdu chcete koupit?)


Jak udelat aby controller posilal serveru?
Jak udelat synchornizaci vlaken s uzivateli?
 */
