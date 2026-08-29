package luck.command;
import luck.exception.LuckException;
import luck.model.Deadline;
/** Creates a deadline with a valid date. */
public class DeadlineCommand implements Command {
    private final CommandContext context;

    public DeadlineCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(String text) throws LuckException {
        String[] parts = text.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty()) {
            throw new LuckException(
                    "Invalid deadline format. Use: deadline <task> /by <date>.");
        }
        Deadline deadline = new Deadline(parts[0].trim(), parts[1].trim());
        if (deadline.getBy() == null) {
            throw new LuckException(
                    "Invalid date format. Use d/M/yyyy or d/M/yyyy HHmm.");
        }
        context.getTaskList().add(deadline);
        context.getStorage().saveTasks(context.getTaskList().getAll());
        context.getUi().printTaskAdded(deadline, context.getTaskList().size());
    }
}
