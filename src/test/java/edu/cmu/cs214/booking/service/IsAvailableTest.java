package edu.cmu.cs214.booking.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import edu.cmu.cs214.booking.domain.Room;
import edu.cmu.cs214.booking.domain.TimeInterval;
import edu.cmu.cs214.booking.domain.User;
import edu.cmu.cs214.booking.repo.InMemoryBookingStore;
import org.junit.jupiter.api.Test;

class IsAvailableTest {

    private final Room roomA = new Room("A", "Alpha", 10);
    private final User alice = new User("u1", "Alice");
    private final User bob = new User("u2", "Bob");

    private BookingService newService() {
        return new BookingService(new InMemoryBookingStore());
    }

    /**
     * A booking [570,630) starts before the requested [600,660) and runs into it,
     * so the room must be reported unavailable. The agent's isAvailable only
     * checks whether an existing booking's start falls inside the request, so it
     * wrongly reports available for this case.
     */
    @Test
    void notAvailableWhenAnEarlierBookingRunsIntoTheRequest() {
        BookingService svc = newService();
        svc.book(roomA, alice, new TimeInterval(570, 630));

        assertFalse(svc.isAvailable(roomA, new TimeInterval(600, 660)));
    }

    /** An existing booking [540,720) contains the request and must block it. */
    @Test
    void notAvailableWhenAnExistingBookingContainsTheRequest() {
        BookingService svc = newService();
        svc.book(roomA, alice, new TimeInterval(540, 720));

        assertFalse(svc.isAvailable(roomA, new TimeInterval(600, 660)));
    }

    /** isAvailable must agree with what book() actually does. */
    @Test
    void isAvailableAgreesWithBook() {
        BookingService svc = newService();
        svc.book(roomA, alice, new TimeInterval(570, 630));

        TimeInterval request = new TimeInterval(600, 660);
        boolean available = svc.isAvailable(roomA, request);
        BookingResult result = svc.book(roomA, bob, request);
        if (available) {
            assertInstanceOf(BookingResult.Confirmed.class, result);
        } else {
            assertInstanceOf(BookingResult.Waitlisted.class, result);
        }
    }
}
