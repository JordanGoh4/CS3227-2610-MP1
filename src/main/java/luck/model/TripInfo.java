package luck.model;

/** Stores the user's current travel-planning details. */
public record TripInfo(String name, String destination, String startDate, String endDate,
                       String currency, String notes) {
    /** Returns whether the required destination and dates are present. */
    public boolean isComplete() {
        return !destination.isBlank() && !startDate.isBlank() && !endDate.isBlank();
    }
}
