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
    private final User carol = new User("u3", "Carol");
    private final User dave = new User("u4", "Dave");
    private final User charlie = new User("u5", "Charlie");

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

    /**
     * The promotion test that must fail on a "removal only" implementation: after
     * cancelling Alice's booking, Bob, who was waitlisted for an overlapping slot,
     * must now hold the only confirmed booking for the room.
     */
    @Test
    void cancellingPromotesTheWaitlistedUser() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        BookingService svc = newService(store);
        Booking a = assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, alice, new TimeInterval(600, 660))).booking();
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomA, bob, new TimeInterval(630, 700)));

        svc.cancelBooking(a.id());

        assertEquals(1, svc.listBookings(roomA).size());
        Booking promoted = svc.listBookings(roomA).get(0);
        assertEquals(bob, promoted.user());
        assertEquals(new TimeInterval(630, 700), promoted.interval());
        assertEquals(0, store.waitlistForRoom(roomA).size());
    }

    @Test
    void cancellingPromotesOnlyTheEarliestFittingWaiter() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        BookingService svc = newService(store);
        Booking a = assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, alice, new TimeInterval(600, 720))).booking();
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomA, bob, new TimeInterval(600, 660)));
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomA, carol, new TimeInterval(660, 700)));

        svc.cancelBooking(a.id());

        assertEquals(1, svc.listBookings(roomA).size());
        Booking promoted = svc.listBookings(roomA).get(0);
        assertEquals(bob, promoted.user());
        assertEquals(new TimeInterval(600, 660), promoted.interval());
        assertEquals(1, store.waitlistForRoom(roomA).size());
    }

    @Test
    void cancellingSkipsWaiterStillInConflictAndPromotesNext() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        BookingService svc = newService(store);
        assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, alice, new TimeInterval(600, 660)));
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomA, bob, new TimeInterval(630, 730)));
        assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, dave, new TimeInterval(700, 760)));
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomA, carol, new TimeInterval(610, 650)));

        svc.cancelBooking(svc.listBookings(roomA).stream()
                .filter(b -> b.user().equals(alice)).findFirst().orElseThrow().id());

        assertEquals(2, svc.listBookings(roomA).size());
        // Bob still conflicts with Dave's booking, so Carol is promoted instead.
        Booking carolBooking = svc.listBookings(roomA).stream()
                .filter(b -> b.user().equals(carol)).findFirst().orElseThrow();
        assertEquals(new TimeInterval(610, 650), carolBooking.interval());
        Booking daveBooking = svc.listBookings(roomA).stream()
                .filter(b -> b.user().equals(dave)).findFirst().orElseThrow();
        assertEquals(new TimeInterval(700, 760), daveBooking.interval());
        assertEquals(1, store.waitlistForRoom(roomA).size());
        assertEquals(bob, store.waitlistForRoom(roomA).get(0).user());
    }

    @Test
    void cancellingDoesNotPromoteFromAnotherRoomsWaitlist() {
        InMemoryBookingStore store = new InMemoryBookingStore();
        BookingService svc = newService(store);
        Booking a = assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomA, alice, new TimeInterval(600, 660))).booking();
        assertInstanceOf(
                BookingResult.Confirmed.class,
                svc.book(roomB, charlie, new TimeInterval(600, 660)));
        assertInstanceOf(
                BookingResult.Waitlisted.class,
                svc.book(roomB, dave, new TimeInterval(630, 700)));

        svc.cancelBooking(a.id());

        assertEquals(0, svc.listBookings(roomA).size());
        // Dave is still on room B's waitlist; cancelling a booking in room A
        // must not promote him.
        assertEquals(1, store.waitlistForRoom(roomB).size());
        assertEquals(dave, store.waitlistForRoom(roomB).get(0).user());
    }
}
