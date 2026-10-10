package edu.cmu.cs214.booking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import edu.cmu.cs214.booking.domain.Booking;
import edu.cmu.cs214.booking.domain.Room;
import edu.cmu.cs214.booking.domain.TimeInterval;
import edu.cmu.cs214.booking.domain.User;
import edu.cmu.cs214.booking.repo.InMemoryBookingStore;
import org.junit.jupiter.api.Test;

class CancelBookingTest {

    private final Room roomA = new Room("A", "Alpha", 10);
    private final Room roomB = new Room("B", "Beta", 4);
    private final User alice = new User("u1", "Alice");
    private final User bob = new User("u2", "Bob");

    private BookingService newService(InMemoryBookingStore store) {
        return new BookingService(store);
    }

    @Test
    void cancellingRemovesTheBooking() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        BookingService svc = newService(store);
        Booking a = assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, alice, new TimeInterval(600, 660))).booking();

        svc.cancelBooking(a.id());

        assertEquals(0, svc.listBookings(roomA).size());
    }

    @Test
    void cancellingUnknownIdDoesNothing() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        BookingService svc = newService(store);
        Booking a = assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, alice, new TimeInterval(600, 660))).booking();
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomA, bob, new TimeInterval(630, 700)));

        svc.cancelBooking("no-such-id");

        assertEquals(1, svc.listBookings(roomA).size());
        assertEquals(1, store.waitlistForRoom(roomA).size());
        assertEquals(a.id(), svc.listBookings(roomA).get(0).id());
    }
}
