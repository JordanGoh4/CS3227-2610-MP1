package luck.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests validation of saved trip details. */
class TripInfoTest {
    @Test
    void completeTripDetails_areAccepted() {
        TripInfo trip = new TripInfo("Japan", "Tokyo", "10/09/2027", "15/09/2027", "JPY", "Food");

        assertTrue(trip.isComplete());
    }

    @Test
    void missingRequiredDetails_areRejected() {
        TripInfo trip = new TripInfo("Japan", "", "", "", "", "");

        assertFalse(trip.isComplete());
    }

    @Test
    void reversedDateRange_isRejected() {
        TripInfo trip = new TripInfo("Japan", "Tokyo", "15/09/2027", "10/09/2027", "JPY", "Food");

        assertFalse(trip.hasValidDateRange());
    }

    @Test
    void pastDateRange_isRejected() {
        TripInfo trip = new TripInfo("Japan", "Tokyo", "10/09/2020", "15/09/2020", "JPY", "Food");

        assertFalse(trip.hasValidDateRange());
    }
}
