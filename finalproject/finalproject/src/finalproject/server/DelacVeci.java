package finalproject.server;

import finalproject.database.Database;
import finalproject.database.User;

import java.io.IOException;

public class DelacVeci {
    private Database db;

    public DelacVeci(Database db) {
        this.db = db;
    }

    public String processRequest(String request) throws IOException {
        while (true) {
            String[] tokens = request.split(" ");
            switch (tokens[0]) {
                case "SIGNUP":
                    db.addUser(new User(tokens[1], tokens[2].getBytes()));
                    System.out.println("JUPI");
                    return "JUP";
                default:
                    return "ERROR";
            }
        }
    }
}
