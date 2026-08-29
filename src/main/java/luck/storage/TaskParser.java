package luck.storage;

import java.util.ArrayList;
import java.util.List;
import luck.model.Deadline;
import luck.model.Event;
import luck.model.Task;
import luck.model.Todo;
import luck.util.DateTimeParser;

public class TaskParser {
    public List<Task> parseAll(String content) {
        List<Task> tasks = new ArrayList<>();
        if (content == null || content.isBlank()) {
            return tasks;
        }

        String[] lines = content.split("\\R");
        for (String rawLine : lines) {
            if (rawLine == null) {
                continue;
            }

            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            Task task = parse(line);
            if (task != null) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    public Task parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\s*\\|\\s*", 4);
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        boolean isDone = "1".equals(parts[1].trim());
        String description = parts[2].trim();

        Task task;
        if ("T".equals(type)) {
            task = new Todo(description);
        } else if ("D".equals(type)) {
            String by = parts.length > 3 ? parts[3].trim() : "";
            task = new Deadline(description, by);
        } else if ("E".equals(type)) {
            String detail = parts.length > 3 ? parts[3].trim() : "";
            String[] dateParts = detail.split("\\s+to\\s+", 2);
            String from = dateParts.length > 0 ? dateParts[0].trim() : "";
            String to = dateParts.length > 1 ? dateParts[1].trim() : "";
            task = new Event(description, from, to);
        } else {
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }

    public String serialize(Task task) {
        if (task == null) {
            return "";
        }

        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline) {
            return "D | " + status + " | " + task.getDescription() + " | "
                    + DateTimeParser.toStorageString(((Deadline) task).getBy());
        }

        if (task instanceof Event) {
            return "E | " + status + " | " + task.getDescription() + " | "
                    + ((Event) task).getFrom() + " to " + ((Event) task).getTo();
        }

        return "T | " + status + " | " + task.getDescription();
    }
}
