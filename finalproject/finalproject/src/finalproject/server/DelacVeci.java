package finalproject.server;

import finalproject.database.DBItem;
import finalproject.database.Database;
import finalproject.database.User;
import finalproject.shared.Item;
import finalproject.shared.Message;
import finalproject.shared.Methods;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DelacVeci {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Database db;

    public DelacVeci(ObjectInputStream ois, ObjectOutputStream oos, Database db) {
        this.ois = ois;
        this.oos = oos;
        this.db = db;
    }

    public void handleRequests() throws IOException {
        Message r = null;
        try {
            r = (Message) ois.readObject();
        } catch (ClassNotFoundException e) {
            //send error
        }
        if(r != null)
            processRequest(r);
    }

    public void processRequest(Message r) {
        switch (r.getmethod()) {
            case Methods.SIGNUP:
                addUser(r.getData()[0], r.getData()[1], r.getData()[2]);
                break;
            case Methods.LOGIN:
                login(r.getData()[0], r.getData()[1]);
                break;
            case Methods.LOAD_ITEMS:
                loadItems(r.getData()[0]);
                break;
            case Methods.BUY_ITEM:
                buyItem(r.getData()[0], r.getData()[1]); //usrname, item id
                break;
            case Methods.UPLOAD_ITEM:
                uploadItem(r.getData()[0], r.getItems());
                break;
            case Methods.DELETE_ITEM:
                deleteItem(r.getData()[0], r.getData()[1]);
                break;
            default:
                sendResponse(Methods.ERROR, null, new String[]{"Error processing request"});
        }
    }
    private void addUser(String username, String password, String account) {
        if(db.addUser(new User(username, password.getBytes(), account)) != null)
            sendResponse(Methods.OK, null, null);
        else
            sendResponse(Methods.ERROR, null, new String[]{"Username taken"});
    }

    private void login(String username, String password) {
        if(db.getUserByName(username) != null && new String(db.getUserByName(username).getPassword()).equals(password))
            sendResponse(Methods.OK, null, null);
        else
            sendResponse(Methods.ERROR, null, new String[]{"Bad login"});
    }


    private void loadItems(String username) {
        List<DBItem> items = db.getItemsForUser(username);
        ArrayList<Item> clientItems = new ArrayList<>();
        for(DBItem i : items)
            clientItems.add(new Item(i.getId(), i.getOwner(), i.getName(), encodeImage(i.getPic_url()), i.getPrice(), i.getDescription()));
        sendResponse(Methods.OK, clientItems, null);
    }

    private void buyItem(String username, String itemId) {
        DBItem item = db.getItemById(Integer.parseInt(itemId));
        db.deleteItemById(Integer.parseInt(itemId));
        sendResponse(Methods.OK, null, new String[]{item.getOwner()});   //hodit alert s cislem uctu od owner
    }
    private void uploadItem(String username, ArrayList<Item> item) {

    }

    private void deleteItem(String username, String itemId) {
        db.deleteItemById(Integer.parseInt(itemId));
        sendResponse(Methods.OK, null, null);       //hazet sqlexceptiony sem a pak informvat uzivatele pres sendresponse
    }

    private byte[] encodeImage(String url) {
        Path path = Paths.get("path/to/image.jpg");
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(Methods method, ArrayList<Item> items, String[] data) {
        Message res = new Message(method, items, data);
        try {
            oos.writeObject(res);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error sending response");
        }
    }
}
