package finalproject.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class AppServer {

//observer na clientovi, jestli server neco neposlal, ne timeline
    //nechat stav obrazovek a jen zneviditelnit?
    private static boolean stopServer = false;

    public static void main(String[] args) throws IOException {
        ServerSocket srvSocket = new ServerSocket(4321);
        while (!isStopServer()) {
            System.out.println("Waiting for a client");
            Socket clientSocket = srvSocket.accept();
            if(isStopServer()){
                clientSocket.close();
                break;
            }
            Thread serverThread = new ServerThread(clientSocket);
            serverThread.start();
        }
        System.out.println("So long!");
        srvSocket.close();
    }


    private static class ServerThread extends Thread {
        //bude managovat login, requesty atd... protokol
        //runnable? bude rozhodovat co je to za request a podle toho bude rikat Delaci Veci, co ma udelat a fetchnout z DB

        private Socket clientSocket;

        public ServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static synchronized void setStopServer(boolean value) {
        stopServer = value;
    }

    private static synchronized boolean isStopServer() {
        return stopServer;
    }

}
