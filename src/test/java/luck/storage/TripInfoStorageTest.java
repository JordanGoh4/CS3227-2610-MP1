package luck.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import luck.model.TripInfo;
import luck.model.Todo;
import org.junit.jupiter.api.Test;

/** Tests persistence of multiple trips. */
class TripInfoStorageTest {
    @Test
    void saveAllAndLoadAll_preservesTripDetails() throws Exception {
        Path file = Files.createTempFile("trip-info", ".properties");
        TripInfoStorage storage = new TripInfoStorage(file);
        List<TripInfo> trips = List.of(new TripInfo("Japan", "Tokyo", "10/09/2026",
                "15/09/2026", "JPY", "Food and museums"));

        storage.saveAll(trips);

        assertEquals(trips, storage.loadAll());
        Files.deleteIfExists(file);
    }

    @Test
    void saveAllAndLoadAll_keepsMultipleTripsSeparate() throws Exception {
        Path file = Files.createTempFile("trip-info", ".properties");
        TripInfoStorage storage = new TripInfoStorage(file);
        List<TripInfo> trips = List.of(
                new TripInfo("Japan", "Tokyo", "10/09/2026", "15/09/2026", "JPY", "Food"),
                new TripInfo("Singapore", "Singapore", "20/10/2026", "25/10/2026", "SGD", "Museums"));

        storage.saveAll(trips);

        assertEquals("Japan", storage.loadAll().get(0).name());
        assertEquals("Singapore", storage.loadAll().get(1).name());
        assertEquals("SGD", storage.loadAll().get(1).currency());
        Files.deleteIfExists(file);
    }

    @Test
    void separateTripTaskFiles_doNotShareTasks() throws Exception {
        Path japanFile = Files.createTempFile("japan-trip", ".txt");
        Path singaporeFile = Files.createTempFile("singapore-trip", ".txt");
        TaskStorage japanStorage = new TaskStorage(japanFile);
        TaskStorage singaporeStorage = new TaskStorage(singaporeFile);

        japanStorage.saveTasks(List.of(new Todo("visit Tokyo")));
        singaporeStorage.saveTasks(List.of(new Todo("visit Marina Bay")));

        assertEquals("visit Tokyo", japanStorage.loadTasks().get(0).getDescription());
        assertEquals("visit Marina Bay", singaporeStorage.loadTasks().get(0).getDescription());
        Files.deleteIfExists(japanFile);
        Files.deleteIfExists(singaporeFile);
    }
}
