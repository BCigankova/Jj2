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
    private ObjectInputStream inStream;
    private ObjectOutput outStream;

    public PosilacNaServer(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
    }

    public void connectToServer() {
        try {
            Socket s = new Socket(ip, port);
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            socket = s;
            inStream = ois;
            outStream = oos;
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server", e);
        }
    }
    public String login(String username, String password) {
        try {
            Message r = new Message(Methods.LOGIN, new ArrayList<>(), new String[]{username, password});
            outStream.writeObject(r);
            outStream.flush();
            Message res = (Message) inStream.readObject();
            return res.getmethod().toString();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to login", e);
        }
    }

    public String signup(String username, String password) {
        try {
            Message r = new Message(Methods.SIGNUP, new ArrayList<>(), new String[]{username, password});
            outStream.writeObject(r);
            outStream.flush();
            Message res = (Message) inStream.readObject();
            return res.getmethod().toString();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Unable to signup", e);
        }
    }

    public ArrayList<Item> getMyItems(String username) {
        ArrayList<Item> items = new ArrayList<>();
    }

    public ArrayList<Item> getBuyItems(String username) {

    }

    public void deleteItem(int itemId) {

    }

    public void buyItem(String username, int itemId) {

    }

    public void close() {
        try {
            if (inStream != null) inStream.close();
            if (outStream != null) outStream.close();
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

