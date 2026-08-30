package luck.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import luck.model.TripInfo;

/** Persists trip details in a small properties file. */
public class TripInfoStorage {
    private final Path filePath;

    /** Creates storage backed by the supplied file. */
    public TripInfoStorage(Path filePath) {
        this.filePath = filePath;
    }

    /** Loads all saved trips, including legacy single-trip data. */
    public java.util.List<TripInfo> loadAll() {
        Properties properties = new Properties();
        try {
            if (Files.exists(filePath)) {
                try (var reader = Files.newBufferedReader(filePath)) {
                    properties.load(reader);
                }
            }
            if (properties.containsKey("destination")) {
                return java.util.List.of(new TripInfo("My trip", properties.getProperty("destination", ""),
                        properties.getProperty("startDate", ""), properties.getProperty("endDate", ""),
                        properties.getProperty("currency", ""), properties.getProperty("notes", "")));
            }
            int count = Integer.parseInt(properties.getProperty("trip.count", "0"));
            java.util.List<TripInfo> trips = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) {
                String prefix = "trip." + i + ".";
                trips.add(new TripInfo(properties.getProperty(prefix + "name", "My trip"),
                        properties.getProperty(prefix + "destination", ""),
                        properties.getProperty(prefix + "startDate", ""),
                        properties.getProperty(prefix + "endDate", ""),
                        properties.getProperty(prefix + "currency", ""),
                        properties.getProperty(prefix + "notes", "")));
            }
            return trips;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load trip details.", exception);
        }
    }

    /** Saves all trips in a numbered properties format. */
    public void saveAll(java.util.List<TripInfo> trips) {
        Properties properties = new Properties();
        for (int i = 0; i < trips.size(); i++) {
            TripInfo trip = trips.get(i);
            String prefix = "trip." + i + ".";
            properties.setProperty(prefix + "name", trip.name());
            properties.setProperty(prefix + "destination", trip.destination());
            properties.setProperty(prefix + "startDate", trip.startDate());
            properties.setProperty(prefix + "endDate", trip.endDate());
            properties.setProperty(prefix + "currency", trip.currency());
            properties.setProperty(prefix + "notes", trip.notes());
        }
        properties.setProperty("trip.count", String.valueOf(trips.size()));
        try {
            Files.createDirectories(filePath.getParent());
            try (var writer = Files.newBufferedWriter(filePath)) {
                properties.store(writer, "Luck trip details");
            }
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save trip details.", exception);
        }
    }

    /** Loads a single saved trip for backward compatibility. */
    public TripInfo load() {
        java.util.List<TripInfo> trips = loadAll();
        return trips.isEmpty() ? new TripInfo("My trip", "", "", "", "", "") : trips.get(0);
    }

    /** Saves a single trip for backward compatibility. */
    public void save(TripInfo tripInfo) {
        saveAll(java.util.List.of(tripInfo));
    }
}
