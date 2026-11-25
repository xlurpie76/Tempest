package hotel;

public class Room {

    private String name;
    private String bedType;
    private String capacity;
    private String roomSize;
    private String description;
    private boolean available;
    private double pricePerNight;

    // Constructor
    public Room(String name, String bedType,
                String capacity, String roomSize,
                String description, boolean available,
                double pricePerNight) {

        this.name = name;
        this.bedType = bedType;
        this.capacity = capacity;
        this.roomSize = roomSize;
        this.description = description;
        this.available = available;
        this.pricePerNight = pricePerNight;
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

    public boolean isAvailable() {
        return available;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    // Setters
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
