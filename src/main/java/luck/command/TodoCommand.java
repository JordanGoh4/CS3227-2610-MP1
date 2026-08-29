package luck.command;
import luck.exception.LuckException;
import luck.model.*;
/** Creates a todo task. */
public class TodoCommand implements Command {
    private final CommandContext context;

    public TodoCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(String text) throws LuckException {
        if (text.trim().isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }
        Task task = new Todo(text.trim());
        context.taskList.add(task);
        context.storage.saveTasks(context.taskList.getAll());
        context.ui.printTaskAdded(task, context.taskList.size());
    }
}
