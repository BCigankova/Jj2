package finalproject.database;

import java.util.Arrays;

public class User {
    private final String username;
    private byte[] password;
    private String account;

    public User(String username, byte[] password, String account) {
        this.username = username;
        this.password = password;
        this.account = account;
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
        return "Username: " + username + ", Password: " + Arrays.toString(password) + ", Account: " + account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
