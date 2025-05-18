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

    //private PreparedStatement getAllUsers;
    private PreparedStatement getUserByName;
    private PreparedStatement addUser;
    private PreparedStatement updatePassword;
    private PreparedStatement deleteUser;


    //items
    private PreparedStatement getItemById;
    private PreparedStatement getItemsForUser;
    private PreparedStatement addItemToUser;
    private PreparedStatement deleteItemById;

    public Database(Connection con) {
        try {
            //getAllUsers = con.prepareStatement("SELECT * FROM users");
            getUserByName = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            addUser = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            updatePassword = con.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
            deleteUser = con.prepareStatement("DELETE FROM users WHERE username = ?");

            //items
            getItemById = con.prepareStatement("SELECT * FROM items WHERE id = ?");
            getItemsForUser = con.prepareStatement("SELECT * FROM items WHERE owner = ?");
            addItemToUser = con.prepareStatement("INSERT INTO items (name, owner, pic_url, price, description) VALUES (?, ?, ?, ?, ?)");
            deleteItemById = con.prepareStatement("DELETE FROM items WHERE id = ?");
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize prepared statements", e); //lepsi exception s textem
        }
    }

    /*
    public List<String> getAllUsers() {
        List<String> items = new ArrayList<>();
        try (ResultSet results = getAllUsers.executeQuery()) {
            while (results.next())
                items.add(results.getString(3));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to list all itins of receipts", e);
        }
        return items;
    }
     */
    public User getUserByName(String name) {
        User user = null;
        try {
            getUserByName.setString(1, name);
            try (ResultSet results = getUserByName.executeQuery()) {
                user = new User(results.getString(1), results.getBytes(2));
            } catch (SQLException e) {
                throw new RuntimeException("User doesnt exist", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting user", e);
        }
        return user;
    }

    public void addUser(User user) {
        try {
            addUser.setString(1, user.getUsername());
            addUser.setBytes(2, user.getPassword());
            addUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to add user", e);
        }
    }

    public void updatePassword(User user) {
        try {
            updatePassword.setBytes(1, user.getPassword());
            updatePassword.setString(2, user.getUsername());
            updatePassword.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update password", e);
        }
    }

    public void deleteUser(User user) {
        try {
            deleteUser.setString(1, user.getUsername());
            deleteUser.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Unable to delete user", e);
        }
    }

    public Item getItemById(int id) {
        Item i = null;
        try {
            getItemById.setInt(1, id);
            try(ResultSet rs = getItemById.executeQuery()) {
                if (rs.next())
                    i = new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6));
            }
            catch (SQLException e) {
                throw new RuntimeException("unable to get item by id", e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("unable to get item by id", e);
        }
        return i;
    }

    public List<Item> getItemsForUser(String username) throws SQLException {
        List<Item> items = new ArrayList<>();
        try {
            getItemsForUser.setString(1, username);
            try (ResultSet rs = getItemsForUser.executeQuery()) {
                while (rs.next()) {
                    items.add(new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)));
                }
            } catch (SQLException e) {
                throw new RuntimeException("unable to get items", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("unable to get items", e);
        }
        return items;
    }

    public void addItemToUser(String name, String pic_url, int price, String description, String username) {
        try {
            addItemToUser.setString(1, name);
            addItemToUser.setString(2, username);
            addItemToUser.setString(3, pic_url);
            addItemToUser.setInt(4, price);
            addItemToUser.setString(5, description);
            addItemToUser.executeQuery();
        }
        catch (SQLException e) {
            throw new RuntimeException("unable to add item to user", e);
        }
    }

    public void deleteItemById(int id) {
        try {
            deleteItemById.setInt(1, id);
        }
        catch (SQLException e) {
            throw new RuntimeException("unable to delete item by id", e);
        }
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
