import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double roomCost;

    public Reservation(String reservationId, String guestName, String roomType, double roomCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomCost = roomCost;
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

    public double getRoomCost() {
        return roomCost;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Cost: ₹" + roomCost);
    }
}

// Booking History Service
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Booking added to history: " + reservation.getReservationId());
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history); // return copy to prevent modification
    }
}

// Reporting Service
class BookingReportService {

    // Display all reservations
    public void generateReport(List<Reservation> reservations) {
        System.out.println("\n--- Booking Report ---");
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        double totalRevenue = 0;

        for (Reservation r : reservations) {
            r.display();
            totalRevenue += r.getRoomCost();
        }

        System.out.println("\nTotal Bookings: " + reservations.size());
        System.out.println("Total Revenue: ₹" + totalRevenue);
        System.out.println("----------------------");
    }
}

// Main Class (matches file name)
public class BookMyStay {
    public static void main(String[] args) {

        // Step 1: Create booking history
        BookingHistory history = new BookingHistory();

        // Step 2: Simulate confirmed reservations
        Reservation r1 = new Reservation("RES101", "Alice", "Single", 2000);
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", 5000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Single", 2000);

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Step 3: Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getAllReservations());
    }
}