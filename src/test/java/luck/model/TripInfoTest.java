package luck.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests validation of saved trip details. */
class TripInfoTest {
    @Test
    void completeTripDetails_areAccepted() {
        TripInfo trip = new TripInfo("Japan", "Tokyo", "10/09/2026", "15/09/2026", "JPY", "Food");

        assertTrue(trip.isComplete());
    }

    @Test
    void missingRequiredDetails_areRejected() {
        TripInfo trip = new TripInfo("Japan", "", "", "", "", "");

        assertFalse(trip.isComplete());
    }
}
