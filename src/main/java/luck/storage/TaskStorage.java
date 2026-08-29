package luck.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import luck.model.Task;

public class TaskStorage {
    private final Path filePath;
    private final TaskParser parser;

    public TaskStorage(Path filePath) {
        this.filePath = filePath;
        this.parser = new TaskParser();
    }

    public void ensureFileExists() {
        try {
            Files.createDirectories(filePath.getParent());
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage file.", e);
        }
    }

    public List<Task> loadTasks() {
        try {
            if (Files.notExists(filePath)) {
                return new ArrayList<>();
            }

            String content = Files.readString(filePath);
            return parser.parseAll(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tasks from disk.", e);
        }
    }

    public void saveTasks(List<Task> tasks) {
        try {
            Files.createDirectories(filePath.getParent());
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }

            StringBuilder content = new StringBuilder();
            for (Task task : tasks) {
                if (task == null) {
                    continue;
                }
                content.append(parser.serialize(task)).append(System.lineSeparator());
            }

            Files.writeString(filePath, content.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to disk.", e);
        }
    }
}
