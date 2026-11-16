
package hotel;

public class Room {

    private String name;
    private String bedType;
    private String capacity;
    private String roomSize;
    private String description;
    private String price;
    private boolean available;

    // Constructor
    public Room(String name, String bedType,
                String capacity, String roomSize,
                String description, String price, boolean available) {

        this.name = name;
        this.bedType = bedType;
        this.capacity = capacity;
        this.roomSize = roomSize;
        this.description = description;
        this.available = available;
        this.price = price;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getBedType() {
        return bedType;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public String getDescription() {
        return description;
    }
    public String getPrice() { return price; }

    public boolean isAvailable() {
        return available;
    }

    // Setter
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
