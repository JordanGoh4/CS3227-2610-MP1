package luck.command;
import luck.exception.LuckException; import luck.model.Task;
/** Marks a task incomplete. */
public class UnmarkCommand implements Command {
    private final CommandContext context;

    public UnmarkCommand(CommandContext context) { this.context = context; }

    @Override
    public void execute(String arguments) throws LuckException {
        Task task;
        try {
            task = context.getTaskList().get(Integer.parseInt(arguments.trim()) - 1);
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
        task.markAsNotDone();
        context.getStorage().saveTasks(context.getTaskList().getAll());
        context.getUi().printMessage("     OK, I've marked this task as not done yet:");
        context.getUi().printMessage("       " + task);
    }
}
