package finalproject.server;

import finalproject.database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppServer {

//observer na clientovi, jestli server neco neposlal, ne timeline
    //nechat stav obrazovek a jen zneviditelnit?
    private static boolean stopServer = false;
    private static final String DBNAME = "AppDB";
    private static final String HOST = "localhost";
    private static final String PORT = "1527";

    public static void main(String[] args) throws IOException, SQLException {       //handlovat errory
        ServerSocket srvSocket = new ServerSocket(4321);
        while (!isStopServer()) {
            System.out.println("Waiting for a client");
            Socket clientSocket = srvSocket.accept();
            if(isStopServer()){
                clientSocket.close();
                break;
            }
            Connection con = DriverManager.getConnection("jdbc:derby://" + HOST + ":" + PORT + "/" + DBNAME);
            Database db = new Database(con);
            Thread serverThread = new ServerThread(clientSocket, db);
            serverThread.start();
        }
        System.out.println("So long!");
        srvSocket.close();
    }


    private static class ServerThread extends Thread {
        private Socket clientSocket;
        private Database db;

        public ServerThread(Socket clientSocket, Database db) {
            this.clientSocket = clientSocket;
            this.db = db;
        }

        public void run() {
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
                DelacVeci dv = new DelacVeci(db);
                String line;
                while ((line = rd.readLine()) != null) {
                    String response = dv.processRequest(line);
                    wr.write(response);
                    wr.newLine();
                    wr.flush();
                }
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
