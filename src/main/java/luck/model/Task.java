package luck.model;

public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;
    protected String by;
    protected String from;
    protected String to;

    public Task(String description) {
        this(description, TaskType.TODO);
    }

    public Task(String description, TaskType type) {
        this(description, type, null, null, null);
    }

    public Task(String description, TaskType type, String by, String from, String to) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public String getTypeIcon() {
        return type.getSymbol();
    }

    @Override
    public String toString() {
        if (TaskType.DEADLINE.equals(type) && by != null) {
            return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
        }

        if (TaskType.EVENT.equals(type) && from != null && to != null) {
            return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description
                    + " (from: " + from + " to: " + to + ")";
        }

        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
