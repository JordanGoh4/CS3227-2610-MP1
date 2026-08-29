package luck.command;
import luck.exception.LuckException; import luck.model.Task;
/** Deletes a task and saves the updated list. */
public class DeleteCommand implements Command {
    private final CommandContext context;

    public DeleteCommand(CommandContext context) { this.context = context; }

    @Override
    public void execute(String arguments) throws LuckException {
        int index;
        try {
            index = Integer.parseInt(arguments.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
        Task task = context.taskList.get(index);
        context.taskList.remove(index);
        context.storage.saveTasks(context.taskList.getAll());
        context.ui.printMessage("     Got it. I've removed this task:");
        context.ui.printMessage("       " + task);
    }
}
