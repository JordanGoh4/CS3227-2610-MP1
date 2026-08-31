package luck.command;
import luck.model.TaskList;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;
/** Shared services used by commands. */
public class CommandContext {
    private TaskList taskList;
    private TaskStorage storage;
    private final ConsoleUI ui;

    public CommandContext(TaskList taskList, TaskStorage storage, ConsoleUI ui) {
        this.taskList = taskList;
        this.storage = storage;
        this.ui = ui;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    /** Changes the task list used by commands when the active trip changes. */
    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    /** Changes the storage used by commands when the active trip changes. */
    public void setStorage(TaskStorage storage) {
        this.storage = storage;
    }

    public TaskStorage getStorage() {
        return storage;
    }

    public ConsoleUI getUi() {
        return ui;
    }
}
