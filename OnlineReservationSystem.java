 import java.util.*;

    // User class
    import java.util.*;

// User class
class User {
    int userId;
    String name;

    User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}

// Train class
class Train {
    int trainId;
    String trainName;
    int totalSeats;
    int availableSeats;

    Train(int trainId, String trainName, int totalSeats) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    // synchronized = thread-safe booking
    synchronized boolean bookSeats(int seats) {
        if (seats <= availableSeats) {
            availableSeats -= seats;
            return true;
        }
        return false;
    }

    synchronized void cancelSeats(int seats) {
        availableSeats += seats;
    }
}

// Ticket class
class Ticket {
    static int idCounter = 1;
    int ticketId;
    User user;
    Train train;
    int seats;

    Ticket(User user, Train train, int seats) {
        this.ticketId = idCounter++;
        this.user = user;
        this.train = train;
        this.seats = seats;
    }

    public String toString() {
        return "Ticket ID: " + ticketId + ", User: " + user.name +
                ", Train: " + train.trainName + ", Seats: " + seats;
    }
}

// Reservation System
class ReservationSystem {
    HashMap<Integer, Train> trains = new HashMap<>();
    HashMap<Integer, Ticket> tickets = new HashMap<>();

    ReservationSystem() {
        trains.put(1, new Train(1, "Express 101", 20));
        trains.put(2, new Train(2, "Superfast 202", 10));
    }

    Ticket bookTicket(User user, int trainId, int seats) {
        Train train = trains.get(trainId);
        if (train != null && train.bookSeats(seats)) {
            Ticket ticket = new Ticket(user, train, seats);
            tickets.put(ticket.ticketId, ticket);
            return ticket;
        }
        return null;
    }

    boolean cancelTicket(int ticketId) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket != null) {
            ticket.train.cancelSeats(ticket.seats);
            tickets.remove(ticketId);
            return true;
        }
        return false;
    }

    void checkAvailability(int trainId) {
        Train train = trains.get(trainId);
        if (train != null) {
            System.out.println("Available seats in " + train.trainName + ": " + train.availableSeats);
        }
    }

    void printTicket(int ticketId) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket != null) {
            System.out.println(ticket);
        } else {
            System.out.println("Ticket not found.");
        }
    }
}

// Multithreading Example
class BookingTask extends Thread {
    ReservationSystem system;
    User user;
    int trainId, seats;

    BookingTask(ReservationSystem system, User user, int trainId, int seats) {
        this.system = system;
        this.user = user;
        this.trainId = trainId;
        this.seats = seats;
    }

    public void run() {
        Ticket ticket = system.bookTicket(user, trainId, seats);
        if (ticket != null) {
            System.out.println("Booking Successful: " + ticket);
        } else {
            System.out.println("Booking Failed for " + user.name);
        }
    }
}

// Main class
public class OnlineReservationSystem {
    public static void main(String[] args) {
        ReservationSystem system = new ReservationSystem();

        User u1 = new User(1, "Satyendra");
        User u2 = new User(2, "Rahul");

        // Multithreading demo
        BookingTask t1 = new BookingTask(system, u1, 1, 4);
        BookingTask t2 = new BookingTask(system, u2, 1, 5);

        t1.start();
        t2.start();

        // Show availability
        system.checkAvailability(1);
    }
}
