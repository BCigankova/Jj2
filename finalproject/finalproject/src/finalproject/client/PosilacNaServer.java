package finalproject.client;

import java.io.*;
import java.net.Socket;

public class PosilacNaServer {
    private Socket socket;

    public PosilacNaServer(String ip, int port) throws IOException {
        try (Socket s = new Socket(ip, port);
             InputStream inpStream = socket.getInputStream();
             OutputStream outStream = socket.getOutputStream();
        ) {
            socket = s;
        } catch (IOException e) {

        }
    }

    public void login(String a, String b) {}

    public void signup(String a, String b) {}

    //posilac a prijmac metody

    public void close() throws IOException {
        socket.close();
    }
}

