package luck.command;
import luck.model.TaskList;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;
/** Shared services used by commands. */
public class CommandContext {
    public final TaskList taskList;
    public final TaskStorage storage;
    public final ConsoleUI ui;

    public CommandContext(TaskList taskList, TaskStorage storage, ConsoleUI ui) {
        this.taskList = taskList;
        this.storage = storage;
        this.ui = ui;
    }
}
