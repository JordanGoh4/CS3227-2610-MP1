package luck.command;
import luck.exception.LuckException;
import luck.model.*;
/** Creates an event task. */
public class EventCommand implements Command {
    private final CommandContext context;

    public EventCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(String text) throws LuckException {
        String[] fromParts = text.split(" /from ", 2);
        String[] toParts = fromParts.length == 2
                ? fromParts[1].split(" /to ", 2) : new String[0];
        if (fromParts.length < 2 || toParts.length < 2
                || fromParts[0].trim().isEmpty()
                || toParts[0].trim().isEmpty()
                || toParts[1].trim().isEmpty()) {
            throw new LuckException("This ain't valid my friend.");
        }
        Task task = new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
        context.taskList.add(task);
        context.storage.saveTasks(context.taskList.getAll());
        context.ui.printTaskAdded(task, context.taskList.size());
    }
}
