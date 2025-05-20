package finalproject.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//do promennych si dam pripraveny statement, ktery ma funkci na vykonani
//kdyz klient zavola db funkci, ta zavola execute v pripravenem statementu

public class Database implements AutoCloseable {

    private PreparedStatement getUserByName;
    private PreparedStatement addUser;
    private PreparedStatement updatePassword;
    private PreparedStatement deleteUser;


    //items
    private PreparedStatement getItemById;
    private PreparedStatement getItemsForUser;
    private PreparedStatement getItemsForNotUser;
    private PreparedStatement addItemToUser;
    private PreparedStatement deleteItemById;

    public Database(Connection con) {
        try {
            getUserByName = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            addUser = con.prepareStatement("INSERT INTO users (username, password, account) VALUES (?, ?, ?)");
            updatePassword = con.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
            deleteUser = con.prepareStatement("DELETE FROM users WHERE username = ?");

            //items
            getItemById = con.prepareStatement("SELECT * FROM items WHERE id = ?");
            getItemsForUser = con.prepareStatement("SELECT * FROM items WHERE owner = ?");
            getItemsForNotUser = con.prepareStatement("SELECT * FROM items WHERE owner != ?");
            addItemToUser = con.prepareStatement("INSERT INTO items (owner, name, pic_url, price, description) VALUES (?, ?, ?, ?, ?)");
            deleteItemById = con.prepareStatement("DELETE FROM items WHERE id = ?");
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize prepared statements", e); //lepsi exception s textem
        }
    }

    public User getUserByName(String name) throws SQLException {
        User user = null;
        getUserByName.setString(1, name);
        ResultSet results = getUserByName.executeQuery();
        if(results.next())
            user = new User(results.getString(1), results.getBytes(2), results.getString(3));
        return user;
    }

    public void addUser(User user) throws SQLException {
            addUser.setString(1, user.getUsername());
            addUser.setBytes(2, user.getPassword());
            addUser.setString(3, user.getAccount());
            addUser.executeUpdate();
    }

    public void updatePassword(User user) throws SQLException {
            updatePassword.setBytes(1, user.getPassword());
            updatePassword.setString(2, user.getUsername());
            updatePassword.executeUpdate();
    }

    public void deleteUser(User user) throws SQLException {
            deleteUser.setString(1, user.getUsername());
            deleteUser.executeUpdate();
    }

    public DBItem getItemById(int id) throws SQLException {
        DBItem i = null;
        getItemById.setInt(1, id);
        ResultSet rs = getItemById.executeQuery();
        if (rs.next())
            i = new DBItem(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6));
        return i;
    }

    public List<DBItem> getItemsForUser(String username) throws SQLException {
        List<DBItem> DBItems = new ArrayList<>();
        getItemsForUser.setString(1, username);
        ResultSet rs = getItemsForUser.executeQuery();
        while (rs.next())
            DBItems.add(new DBItem(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)));
        return DBItems;
    }

    public List<DBItem> getItemsForNotUser(String username) throws SQLException {
        List<DBItem> DBItems = new ArrayList<>();
        getItemsForNotUser.setString(1, username);
        ResultSet rs = getItemsForNotUser.executeQuery();
        while (rs.next())
            DBItems.add(new DBItem(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)));
        return DBItems;
    }

    public void addItemToUser(String name, String username, String pic_url, int price, String description) throws SQLException {
        addItemToUser.setString(1, name);
        addItemToUser.setString(2, username);
        addItemToUser.setString(3, pic_url);
        addItemToUser.setInt(4, price);
        addItemToUser.setString(5, description);
        addItemToUser.executeUpdate();
    }

    public void deleteItemById(int id) throws SQLException {
        deleteItemById.setInt(1, id);
        deleteItemById.executeUpdate();
    }


    @Override
    public void close() {
        try {
            getUserByName.close();
            addUser.close();
            updatePassword.close();
            deleteUser.close();
            getItemById.close();
            getItemsForUser.close();
            addItemToUser.close();
            deleteItemById.close();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to close", e);
        }

    }
}
