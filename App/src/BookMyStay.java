import java.util.*;

// Reservation (with ID)
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
}

// Add-On Service
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> servicesMap = new HashMap<>();

    // Add service
    public void addService(String reservationId, AddOnService service) {
        servicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added: " + service.getName() +
                " for Reservation: " + reservationId);
    }

    // View services
    public void viewServices(String reservationId) {
        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> list = servicesMap.get(reservationId);

        if (list == null || list.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        for (AddOnService s : list) {
            System.out.println("- " + s.getName() + " (₹" + s.getCost() + ")");
        }
    }

    // Calculate total cost
    public double getTotalCost(String reservationId) {
        List<AddOnService> list = servicesMap.get(reservationId);

        if (list == null) return 0;

        double total = 0;
        for (AddOnService s : list) {
            total += s.getCost();
        }
        return total;
    }
}

// Main Class (matches file name)
public class BookMyStay {
    public static void main(String[] args) {

        // Existing reservation (from previous use case)
        Reservation reservation = new Reservation("RES101", "Alice", "Single");

        // Add-On Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(reservation.getReservationId(), new AddOnService("Breakfast", 500));
        manager.addService(reservation.getReservationId(), new AddOnService("Airport Pickup", 1200));
        manager.addService(reservation.getReservationId(), new AddOnService("Extra Bed", 800));

        // Display services
        manager.viewServices(reservation.getReservationId());

        // Total cost
        double total = manager.getTotalCost(reservation.getReservationId());
        System.out.println("\nTotal Add-On Cost: ₹" + total);
    }
}