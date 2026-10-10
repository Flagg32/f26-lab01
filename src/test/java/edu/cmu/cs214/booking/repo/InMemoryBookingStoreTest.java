package edu.cmu.cs214.booking.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.cmu.cs214.booking.domain.Booking;
import edu.cmu.cs214.booking.domain.Room;
import edu.cmu.cs214.booking.domain.TimeInterval;
import edu.cmu.cs214.booking.domain.User;
import org.junit.jupiter.api.Test;

class InMemoryBookingStoreTest {

    private final Room roomA = new Room("A", "Alpha", 10);
    private final Room roomB = new Room("B", "Beta", 4);

    /**
     * Regression test for the Milestone 1 root cause: {@code bookingsForRoom}
     * must return only the bookings held for the given room, matching the
     * contract documented on {@link BookingStore#bookingsForRoom(Room)}. Before
     * the fix it returned every booking, so bookings in other rooms appeared to
     * collide and {@code BookingService.book} wrongly waitlisted them.
     */
    @Test
    void bookingsForRoomReturnsOnlyThatRoomsBookings() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        store.addBooking(new Booking("b1", roomA, new User("u1", "Alice"), new TimeInterval(600, 660)));
        store.addBooking(new Booking("b2", roomB, new User("u2", "Bob"), new TimeInterval(600, 660)));

        assertEquals(1, store.bookingsForRoom(roomA).size());
        assertEquals(1, store.bookingsForRoom(roomB).size());
    }
}
