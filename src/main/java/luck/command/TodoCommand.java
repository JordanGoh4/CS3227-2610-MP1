package luck.command;
import luck.exception.LuckException;
import luck.model.Task;
import luck.model.Todo;
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
        context.getTaskList().add(task);
        context.getStorage().saveTasks(context.getTaskList().getAll());
        context.getUi().printTaskAdded(task, context.getTaskList().size());
    }
}
