import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory Service with validation
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRooms(String roomType, int count) throws InvalidBookingException {
        if (count < 0) throw new InvalidBookingException("Cannot add negative room count.");
        inventory.put(roomType, count);
    }

    public int getAvailable(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) throw new InvalidBookingException("Invalid room type: " + roomType);
        return inventory.get(roomType);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int available = getAvailable(roomType);
        if (available <= 0) throw new InvalidBookingException("No available rooms for type: " + roomType);
        inventory.put(roomType, available - 1);
    }
}

// Booking Service with validation
class BookingService {
    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Validate and confirm booking
    public void bookRoom(Reservation reservation) {
        try {
            // Guard checks
            if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
                throw new InvalidBookingException("Room type cannot be empty.");
            }

            // Check availability and allocate
            inventory.decrement(reservation.getRoomType());

            // Confirm booking
            System.out.println("Booking confirmed for " + reservation.getGuestName() +
                    " in room type " + reservation.getRoomType());

        } catch (InvalidBookingException ex) {
            System.out.println("Booking failed: " + ex.getMessage());
        }
    }
}

// Main class (must match filename)
public class BookMyStay {
    public static void main(String[] args) {

        try {
            // Step 1: Setup inventory
            InventoryService inventory = new InventoryService();
            inventory.addRooms("Single", 2);
            inventory.addRooms("Suite", 1);

            // Step 2: Create booking service
            BookingService bookingService = new BookingService(inventory);

            // Step 3: Simulate bookings
            Reservation r1 = new Reservation("RES101", "Alice", "Single");
            Reservation r2 = new Reservation("RES102", "Bob", "Suite");
            Reservation r3 = new Reservation("RES103", "Charlie", "Single");
            Reservation r4 = new Reservation("RES104", "David", "Single"); // Should fail (no more Single rooms)
            Reservation r5 = new Reservation("RES105", "", "Single"); // Invalid guest name
            Reservation r6 = new Reservation("RES106", "Eve", "Penthouse"); // Invalid room type

            List<Reservation> reservations = Arrays.asList(r1, r2, r3, r4, r5, r6);

            for (Reservation r : reservations) {
                bookingService.bookRoom(r);
            }

        } catch (InvalidBookingException ex) {
            System.out.println("Inventory setup error: " + ex.getMessage());
        }
    }
}