package finalproject.database;

public class Item {
    private final int id;
    private String owner;
    private String name;
    private String pic_url;
    private int price;
    private String description;

    public Item(int id, String owner, String name, String pic_url, int price, String description) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.pic_url = pic_url;
        this.price = price;
        this.description = description;
    }


    public int getId() {
        return id;
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

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
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
        return "id: " + id + " owner: " + owner + " name: " + name + " pic_url: " + pic_url + " price: " + price + " description: " + description;
    }
}
