package luck.command;
import luck.exception.LuckException; import luck.model.Task;
/** Marks a task complete. */
public class MarkCommand implements Command {
    private final CommandContext context;

    public MarkCommand(CommandContext context) { this.context = context; }

    @Override
    public void execute(String arguments) throws LuckException {
        Task task = getTask(arguments);
        task.markAsDone();
        context.storage.saveTasks(context.taskList.getAll());
        context.ui.printMessage("     Nice! I've marked this task as done:");
        context.ui.printMessage("       " + task);
    }

    private Task getTask(String arguments) throws LuckException {
        try {
            return context.taskList.get(Integer.parseInt(arguments.trim()) - 1);
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
    }
}
