package luck.command;
import luck.model.TaskList;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;
/** Shared services used by commands. */
public class CommandContext {
    private final TaskList taskList;
    private final TaskStorage storage;
    private final ConsoleUI ui;

    public CommandContext(TaskList taskList, TaskStorage storage, ConsoleUI ui) {
        this.taskList = taskList;
        this.storage = storage;
        this.ui = ui;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public TaskStorage getStorage() {
        return storage;
    }

    public ConsoleUI getUi() {
        return ui;
    }
}
