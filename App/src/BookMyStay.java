import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("-----------------------------");
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    // Read-only access
    public int getAvailableCount(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availability.keySet();
    }
}

// Search Service (Read-Only)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {
            int count = inventory.getAvailableCount(type);

            // Validation Logic: Only show available rooms
            if (count > 0) {
                Room room = roomCatalog.get(type);

                if (room != null) { // Defensive programming
                    room.displayDetails();
                    System.out.println("Available Count: " + count);
                    System.out.println("=============================");
                }
            }
        }
    }
}

// Main Class
public class BookMyStay { m
    public static void main(String[] args) {

        // Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Create Room Catalog (Domain Model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room(
                "Single",
                2000,
                Arrays.asList("WiFi", "TV", "AC")
        ));

        roomCatalog.put("Double", new Room(
                "Double",
                3500,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")
        ));

        roomCatalog.put("Suite", new Room(
                "Suite",
                6000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar", "Jacuzzi")
        ));

        // Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Guest initiates search
        searchService.searchAvailableRooms();
    }
}
