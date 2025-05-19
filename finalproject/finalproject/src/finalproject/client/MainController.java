package finalproject.client;

import finalproject.shared.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private PosilacNaServer pns;
    private String user;
    @FXML private Label username;


    @FXML private TilePane userItemGrid;
    @FXML private TilePane itemGrid;
    @FXML private VBox userItemsSection;
    @FXML private VBox itemsSection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(user);
        addUserItems();
        addItems();
    }

    private void addUserItems() {
        ArrayList<Item> myItems = pns.getMyItems(user);
        for (Item item : myItems) {
            VBox itemCard = createItemBox(item, true);
            userItemGrid.getChildren().add(itemCard);
        }
    }

    private void addItems() {
        ArrayList<Item> items = pns.getBuyItems(user);
        for (Item item : items) {
            VBox itemCard = createItemBox(item, false);
            itemGrid.getChildren().add(itemCard);
        }
    }

    private VBox createItemBox(Item item, boolean isUserItem) {
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

    private void deleteItem(Item item) {
        pns.deleteItem(item.getId());
    }

    private void buyItem(Item item) {
        pns.buyItem(user, item.getId());
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
