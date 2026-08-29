package luck.model;

import java.util.ArrayList;
import java.util.List;
import luck.exception.LuckException;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) throws LuckException {
        if (task == null) {
            throw new LuckException("This ain't valid my friend.");
        }

        tasks.add(task);
    }

    public void remove(int index) throws LuckException {
        if (index < 0 || index >= tasks.size()) {
            throw new LuckException("This ain't valid my friend.");
        }

        tasks.remove(index);
    }

    public Task get(int index) throws LuckException {
        if (index < 0 || index >= tasks.size()) {
            throw new LuckException("This ain't valid my friend.");
        }

        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    /** Returns tasks whose descriptions contain the keyword, ignoring case. */
    public List<Task> find(String keyword) {
        String searchTerm = keyword.toLowerCase();
        List<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(searchTerm)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    public void clear() {
        tasks.clear();
    }
}
