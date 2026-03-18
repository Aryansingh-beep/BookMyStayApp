import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRooms(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailable(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// Booking Service (Room Allocation)
class BookingService {
    private Queue<Reservation> queue;

    // Ensure unique room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track allocations per room type
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private InventoryService inventory;

    public BookingService(Queue<Reservation> queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    // Generate unique Room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" +
                    UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Process bookings (FIFO)
    public void processBookings() {
        System.out.println("\nProcessing Booking Requests...\n");

        while (!queue.isEmpty()) {
            Reservation r = queue.poll(); // FIFO

            String type = r.getRoomType();

            if (inventory.getAvailable(type) > 0) {

                String roomId = generateRoomId(type);

                // Atomic allocation
                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrement(type);

                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Room ID: " + roomId);
                System.out.println("-----------------------------");

            } else {
                System.out.println("Booking Failed (No Availability)");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Requested: " + type);
                System.out.println("-----------------------------");
            }
        }
    }
}

// Main Class (MUST match file name)
public class BookMyStay {
    public static void main(String[] args) {

        // Booking Queue (FIFO)
        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single"));
        queue.offer(new Reservation("Bob", "Suite"));
        queue.offer(new Reservation("Charlie", "Single"));
        queue.offer(new Reservation("David", "Single"));

        // Inventory Setup
        InventoryService inventory = new InventoryService();
        inventory.addRooms("Single", 2);
        inventory.addRooms("Suite", 1);

        // Booking Processing
        BookingService bookingService = new BookingService(queue, inventory);
        bookingService.processBookings();
    }
}