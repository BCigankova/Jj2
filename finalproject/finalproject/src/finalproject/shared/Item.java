package finalproject.shared;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String owner;
    private String name;
    private byte[] pic;
    private int price;
    private String description;

    public Item(){
    }

    public Item(int id, String owner, String name, byte[] pic, int price, String description) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.pic = pic;
        this.price = price;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "id: " + id + " owner: " + owner + " name: " + name + " price: " + price + " description: " + description;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }
}
