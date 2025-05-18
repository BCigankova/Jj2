package finalproject.client;

import finalproject.database.User;

import java.io.*;
import java.net.Socket;

public class PosilacNaServer {
    private Socket socket;
    private String ip;
    private int port;
    private BufferedWriter writer;
    private BufferedReader reader;

    public PosilacNaServer(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
    }

    public void connectToServer() {
        try {Socket s = new Socket(ip, port);
             InputStream inpStream = s.getInputStream();
             OutputStream outStream = s.getOutputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(inpStream));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream));
            socket = s;
            writer = out;
            reader = in;
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server", e);
        }
    }
    public void login(String a, String b) {}

    public String signup(String username, String password) {
        try {
            writer.write(Requests.SIGNUP + " " + username + " " + password + "\n");
            writer.flush();
            String response = reader.readLine();    //dodelat
            return "OK";
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to signup", e);      //predelat na vraceni erroru clientu
        }
    }

    public void close() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
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

