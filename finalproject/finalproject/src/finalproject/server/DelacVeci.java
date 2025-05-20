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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DelacVeci {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Database db;

    public DelacVeci(ObjectInputStream ois, ObjectOutputStream oos, Database db) {
        this.ois = ois;
        this.oos = oos;
        this.db = db;
    }

    public void handleRequests() {
        try {
            while (true) {
                Message request = (Message) ois.readObject();
                if (request == null) {
                    break;
                }
                processRequest(request);
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error processing request: " + e.getMessage());
        }
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
            case Methods.LOAD_BUY_ITEMS:
                loadBuyItems(r.getData()[0]);
                break;
            case Methods.BUY_ITEM:
                buyItem(r.getData()[0], r.getData()[1]); //usrname, item id
                break;
            case Methods.UPLOAD_ITEM:
                uploadItem(r.getData()[0], r.getItems());
                break;
            case Methods.DELETE_ITEM:
                deleteItem(r.getData()[0]);
                break;
            default:
                sendResponse(Methods.ERROR, null, new String[]{"Error processing request"});
        }
    }
    private void addUser(String username, String password, String account) {
        try {
            db.addUser(new User(username, password.getBytes(), account));
            sendResponse(Methods.OK, null, null);
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error creating account"});
        }
    }

    private void login(String username, String password) {
        try {
            if(db.getUserByName(username) != null && new String(db.getUserByName(username).getPassword()).equals(password))
                sendResponse(Methods.OK, null, null);
            else
                sendResponse(Methods.ERROR, null, new String[]{"Bad login"});
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error loggin in"});
        }
    }


    private void loadItems(String username) {
        List<DBItem> items = null;
        try {
            items = db.getItemsForUser(username);
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error loading item"});
        }
        ArrayList<Item> clientItems = new ArrayList<>();
        for(DBItem i : items) {
            clientItems.add(new Item(i.getId(), i.getOwner(), i.getName(), encodeImage(i.getPic_url()), i.getPrice(), i.getDescription()));
        }
        sendResponse(Methods.OK, clientItems, null);
    }

    private void loadBuyItems(String username) {
        List<DBItem> items = null;
        try {
            items = db.getItemsForNotUser(username);
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error loading item"});
        }
        ArrayList<Item> notClientItems = new ArrayList<>();
        for(DBItem i : items) {
            notClientItems.add(new Item(i.getId(), i.getOwner(), i.getName(), encodeImage(i.getPic_url()), i.getPrice(), i.getDescription()));
        }
        sendResponse(Methods.OK, notClientItems, null);
    }

    private void buyItem(String username, String itemId) {
        DBItem item = null;
        try {
            item = db.getItemById(Integer.parseInt(itemId));
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error deleting item"});
        }
        try {
            db.deleteItemById(Integer.parseInt(itemId));
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error deleting item"});
        }
        try {
            sendResponse(Methods.OK, null, new String[]{db.getUserByName(item.getOwner()).getAccount()});   //hodit alert s cislem uctu od owner
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error deleting item"});
        }
    }

    private void uploadItem(String username, ArrayList<Item> item) {
        Item itemToUpload = item.getFirst();
        String url = null;
        try {
            url = saveImg(itemToUpload.getPic());
        } catch (IOException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error saving image"});
            e.printStackTrace();
        }
        try {
            db.addItemToUser(itemToUpload.getName(), username, url, itemToUpload.getPrice(), itemToUpload.getDescription());
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error saving image"});
            e.printStackTrace();
        }
        sendResponse(Methods.OK, null, null);
    }

    public String saveImg(byte[] img) throws IOException {
        String folderPath = "/home/barbora/Documents/Inf/2/2LS/JJ2/server_images";  //   /home/barbora/2LS/JJ2/ symlink
        File dir = new File(folderPath);

        String randomValue = UUID.randomUUID().toString();
        String fileName = randomValue + ".png";
        File imageFile = new File(dir, fileName);

        FileOutputStream fos = new FileOutputStream(imageFile);
        fos.write(img);
        fos.close();
        return folderPath + "/" + fileName;
    }

    private void deleteItem(String itemId) {
        try {
            db.deleteItemById(Integer.parseInt(itemId));
        } catch (SQLException e) {
            sendResponse(Methods.ERROR, null, new String[]{"Error deleting item"});
        }
        sendResponse(Methods.OK, null, null);       //hazet sqlexceptiony sem a pak informvat uzivatele pres sendresponse
    }

    private byte[] encodeImage(String url) {
        Path path = Paths.get(url);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(Methods method, ArrayList<Item> items, String[] data) {
        Message res = new Message(method, items, data);
        try {
            System.out.println(res);
            oos.writeObject(res);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error sending response");
        }
    }
}
