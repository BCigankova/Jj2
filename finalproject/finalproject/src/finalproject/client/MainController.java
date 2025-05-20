package finalproject.client;

import finalproject.shared.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private PosilacNaServer pns;
    private String user;
    private StAXWR stAXWR;


    @FXML private Label username;
    @FXML private TilePane userItemGrid;
    @FXML private TilePane itemGrid;
    @FXML private VBox userItemsSection;
    @FXML private VBox itemsSection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stAXWR = new StAXWR();
    }

    public void init() {
        username.setText(user);
        addUserItems();
        addItems();
    }

    private void addUserItems() {
        ArrayList<Item> myItems = pns.getMyItems(user);
        if(myItems != null)
            System.out.println("items size: " + myItems.size());
        if(myItems == null)
            return;
        for (Item item : myItems) {
            VBox itemCard = createItemBox(item, true);
            userItemGrid.getChildren().add(itemCard);
        }
    }

    private void addItems() {
        ArrayList<Item> items = pns.getBuyItems(user);
        if(items != null)
            System.out.println("items size: " + items.size());
        if(items == null)
            return;
        for (Item item : items) {
            VBox itemCard = createItemBox(item, false);
            itemGrid.getChildren().add(itemCard);
        }
    }

    private VBox createItemBox(Item item, boolean isUserItem) {
        System.out.println(isUserItem);
        VBox itemsBox = new VBox(10);
        itemsBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-radius: 5;");

        ImageView imageView = new ImageView();
        Image image = new Image(new ByteArrayInputStream(item.getPic()));
        imageView.setImage(image);

        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label priceLabel = new Label("Price: " + item.getPrice());
        Label descriptionLabel = new Label(item.getDescription());

        itemsBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel);

        Button actionButton = new Button(isUserItem ? "Delete" : "Buy");
        actionButton.setOnAction(e -> {
            if (isUserItem)
                deleteItem(item);
            else
                buyItem(item);
        });
        itemsBox.getChildren().add(actionButton);
        return itemsBox;
    }

    @FXML
    private void onExportAction(ActionEvent event) {
        ArrayList<Item> myItems = pns.getMyItems(user);
        ArrayList<Item> items = pns.getBuyItems(user);
        exportStAX(myItems, items);
    }

    public void exportStAX(ArrayList<Item> myItems, ArrayList<Item> items) {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(new Stage());

            if (file == null) return;

            try (OutputStream os = new FileOutputStream(file)) {
                stAXWR.storeItems(os, myItems, items);
            } catch (IOException | XMLStreamException e) {
                showAlert("Unable to export to XML");
                e.printStackTrace();
            }
        } catch (RuntimeException e) {
            showAlert("Unable to export to XML");
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogoutAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/LoginWindow.fxml"));
            Parent parent = loader.load();

            LoginController controller = loader.getController();
            controller.setPns(pns);
            //controller.getPns().clearStream();

            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent, 700, 300));
            stage.show();

        } catch (IOException e) {
            showAlert("Error loading login window");
        }
    }
    /*
    @FXML
    private void onChangeAccountAction(ActionEvent event) {
        pns.changeAccount(user);
    }

    private void showPopUp(boolean changeAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/ChangeStuffWindow.fxml"));
            Parent root = loader.load();

            ChangeStuffPopupController controller = loader.getController();

            Stage addItemStage = new Stage();
            addItemStage.setTitle("Change");
            addItemStage.setScene(new Scene(root));
            addItemStage.initModality(Modality.APPLICATION_MODAL);
            addItemStage.showAndWait();


            if (controller.getNewValue() != null ) {
                pns.changeAccount(user, controller.getNewValue());
                System.out.println("Account changed");
                username.setText(controller.getNewValue());
            }
        } catch (IOException e) {
            showAlert("Something went wrong while adding new item");
        }
    }

    @FXML
    private void onChangePasswordAction(ActionEvent event) {
        pns.changePassword(user);
    }
     */

    @FXML
    private void onAddItemAction(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/AddItemWindow.fxml"));
                Parent root = loader.load();

                AddItemPopupController controller = loader.getController();

                Stage addItemStage = new Stage();
                addItemStage.setTitle("Add Item");
                addItemStage.setScene(new Scene(root));
                addItemStage.initModality(Modality.APPLICATION_MODAL);
                addItemStage.showAndWait();


                if (controller.getName() != null) {
                    pns.addItem(controller.getName(), user, controller.getImage(), controller.getPrice(), controller.getDescription());
                    System.out.println("New item added.");
                    addUserItems();
                }
            } catch (IOException | ClassNotFoundException e) {
                showAlert("Something went wrong while adding new item");
            }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private void deleteItem(Item item) {
        pns.deleteItem(item.getId());
        addUserItems();
    }

    private void buyItem(Item item) {
        String res = null;
        try {
            res = pns.buyItem(user, item.getId());
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Something went wrong while buying an item");
        }
        addUserItems();
        if(res != null)
            showAlert("Pay here: " + res);
        //nejaky listener mozna? at se to furt nestahuje
    }

    public PosilacNaServer getPns() {
        return pns;
    }

    public void setPns(PosilacNaServer pns) {
        this.pns = pns;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
