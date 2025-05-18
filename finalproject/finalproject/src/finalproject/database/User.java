package finalproject.database;

import java.util.Arrays;

public class User {
    private final String username;
    private byte[] password;

    public User(String username, byte[] password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String toString() {
        return "Username: " + username + ", Password: " + Arrays.toString(password);
    }

}
