package finalproject.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Message implements Serializable {
    private Methods method;
    private ArrayList<Item> items;
    private String[] data;

    public Message(Methods method, ArrayList<Item> items, String[] data) {
        this.method = method;
        this.items = items;
        this.data = data;
    }

    public Methods getmethod() {
        return method;
    }

    public void setmethod(Methods method) {
        this.method = method;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String toString() {
        return method.toString() + " " + (items == null ? "null" : items.toString()) + " " + (data == null ? "null" : Arrays.toString(data));
    }
}
