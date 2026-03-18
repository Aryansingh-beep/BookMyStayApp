import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType);
    }
}

// Inventory Service with rollback support
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRooms(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailable(String roomType) throws Exception {
        if (!inventory.containsKey(roomType)) throw new Exception("Invalid room type: " + roomType);
        return inventory.get(roomType);
    }

    public void decrement(String roomType) throws Exception {
        int available = getAvailable(roomType);
        if (available <= 0) throw new Exception("No rooms available for type: " + roomType);
        inventory.put(roomType, available - 1);
    }

    public void increment(String roomType) throws Exception {
        int available = getAvailable(roomType);
        inventory.put(roomType, available + 1);
    }
}

// Booking History Service
class BookingHistory {
    private Map<String, Reservation> confirmedBookings = new HashMap<>();

    public void addReservation(Reservation r) {
        confirmedBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String reservationId) {
        return confirmedBookings.get(reservationId);
    }

    public boolean exists(String reservationId) {
        return confirmedBookings.containsKey(reservationId);
    }

    public void removeReservation(String reservationId) {
        confirmedBookings.remove(reservationId);
    }

    public void displayAll() {
        System.out.println("\n--- Current Bookings ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for (Reservation r : confirmedBookings.values()) {
            r.display();
        }
    }
}

// Cancellation Service
class CancellationService {
    private InventoryService inventory;
    private BookingHistory history;
    private Stack<String> releasedRoomIds = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {
        try {
            if (!history.exists(reservationId)) {
                System.out.println("Cancellation failed: Reservation ID " + reservationId + " does not exist.");
                return;
            }

            Reservation r = history.getReservation(reservationId);

            // Rollback inventory
            inventory.increment(r.getRoomType());

            // Track released room
            releasedRoomIds.push(reservationId);

            // Remove from booking history
            history.removeReservation(reservationId);

            System.out.println("Booking cancelled successfully: " + reservationId +
                    " | Room type restored: " + r.getRoomType());

        } catch (Exception ex) {
            System.out.println("Cancellation error: " + ex.getMessage());
        }
    }

    // Display recently released room IDs
    public void displayReleasedRooms() {
        System.out.println("\nRecently released reservations (LIFO order):");
        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations yet.");
            return;
        }
        for (String id : releasedRoomIds) {
            System.out.println("- " + id);
        }
    }
}

// Booking Service (basic for confirmation)
class BookingService {
    private InventoryService inventory;
    private BookingHistory history;

    public BookingService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void confirmBooking(Reservation r) {
        try {
            inventory.decrement(r.getRoomType());
            history.addReservation(r);
            System.out.println("Booking confirmed: " + r.getReservationId() +
                    " | Guest: " + r.getGuestName() +
                    " | Room: " + r.getRoomType());
        } catch (Exception ex) {
            System.out.println("Booking failed: " + ex.getMessage());
        }
    }
}

// Main class (matches file name)
public class BookMyStay {
    public static void main(String[] args) {

        try {
            // Step 1: Setup inventory
            InventoryService inventory = new InventoryService();
            inventory.addRooms("Single", 2);
            inventory.addRooms("Suite", 1);

            // Step 2: Booking history
            BookingHistory history = new BookingHistory();

            // Step 3: Services
            BookingService bookingService = new BookingService(inventory, history);
            CancellationService cancellationService = new CancellationService(inventory, history);

            // Step 4: Confirm some bookings
            Reservation r1 = new Reservation("RES101", "Alice", "Single");
            Reservation r2 = new Reservation("RES102", "Bob", "Suite");
            Reservation r3 = new Reservation("RES103", "Charlie", "Single");

            bookingService.confirmBooking(r1);
            bookingService.confirmBooking(r2);
            bookingService.confirmBooking(r3);

            // Display current bookings
            history.displayAll();

            // Step 5: Cancel some bookings
            cancellationService.cancelBooking("RES103"); // valid
            cancellationService.cancelBooking("RES104"); // invalid
            cancellationService.cancelBooking("RES102"); // valid

            // Display released rooms
            cancellationService.displayReleasedRooms();

            // Display remaining bookings
            history.displayAll();

        } catch (Exception ex) {
            System.out.println("Error initializing system: " + ex.getMessage());
        }
    }
}