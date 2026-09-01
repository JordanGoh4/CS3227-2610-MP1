package luck.model;

import java.time.LocalDate;
import luck.util.DateTimeParser;

/** Stores the user's current travel-planning details. */
public record TripInfo(String name, String destination, String startDate, String endDate,
                       String currency, String notes) {
    /** Returns whether the required destination and dates are present. */
    public boolean isComplete() {
        return !destination.isBlank() && !startDate.isBlank() && !endDate.isBlank();
    }

    /** Returns whether the trip dates are valid, ordered, and not in the past. */
    public boolean hasValidDateRange() {
        var start = DateTimeParser.parse(startDate);
        var end = DateTimeParser.parse(endDate);
        if (start == null || end == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !start.toLocalDate().isBefore(today)
                && !end.toLocalDate().isBefore(start.toLocalDate());
    }
}
