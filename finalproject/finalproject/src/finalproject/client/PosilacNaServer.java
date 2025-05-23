package finalproject.client;

import finalproject.shared.Item;
import finalproject.shared.Message;
import finalproject.shared.Methods;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PosilacNaServer {
    private Socket socket;
    private String ip;
    private int port;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public PosilacNaServer(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
    }

    public void connectToServer() {
        try {
            Socket s = new Socket(ip, port);
            ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());
            //outStream.flush();
            ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
            System.out.println("Connecting to " + ip + ":" + port);
            socket = s;
            ois = inStream;
            oos = outStream;
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server", e);
        }
    }

    private void sendMessage(Methods method, ArrayList<Item> items, String[] data) {
        Message res = new Message(method, items, data);
        try {
            System.out.println(res);
            oos.writeObject(res);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending resp pns");
        }
    }

    public String login(String username, String password) {
        try {
            sendMessage(Methods.LOGIN, new ArrayList<>(), new String[]{username, password});
            Message res = (Message) ois.readObject();
            System.out.println("Login resp: " + res);
            return res.getmethod().toString();
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public String signup(String username, String password, String account) {
        try {
            sendMessage(Methods.SIGNUP, new ArrayList<>(), new String[]{username, password, account});
            Message res = (Message) ois.readObject();
            return res.getmethod().toString();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to signup", e);
        }
    }

    public ArrayList<Item> getMyItems(String username) {
        sendMessage(Methods.LOAD_ITEMS, new ArrayList<>(), new String[]{username});
        Message res = null;
        try {
            res = (Message) ois.readObject();
            System.out.println("res LOAD ITEMS " + res);
            return res.getItems();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public ArrayList<Item> getBuyItems(String username) {
        sendMessage(Methods.LOAD_BUY_ITEMS, new ArrayList<>(), new String[]{username});
        Message res = null;
        try {
            res = (Message) ois.readObject();
            System.out.println("res LOAD BUY ITEMS" + res);
            return res.getItems();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public String deleteItem(int itemId) {
        sendMessage(Methods.DELETE_ITEM, null, new String[]{String.valueOf(itemId)});
        try {
            Message res = (Message) ois.readObject();
            System.out.println("res DELETE ITEM " + res);
            return res.getmethod().toString();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public String buyItem(String username, int itemId){
        sendMessage(Methods.BUY_ITEM, null, new String[]{username, String.valueOf(itemId)});
        Message res = null;
        try {
            res = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        if(res.getmethod() == Methods.ERROR)
            return null;
        return res.getData()[0];
    }

    public String addItem(String name, String owner, byte[] img, int price, String description) throws IOException, ClassNotFoundException {
        ArrayList<Item> item = new ArrayList<>();
        item.add(new Item(0, owner, name, img, price, description));
        sendMessage(Methods.UPLOAD_ITEM, item, new String[]{owner});
        Message res = (Message) ois.readObject();
        System.out.println("res UPLOAD_ITEM " + res);
        if(res.getmethod() == Methods.ERROR)
            return null;
        return res.getmethod().toString();
    }

    public void close() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

