package luck.command;
/** Lists all tasks. */
public class ListCommand implements Command {
    private final CommandContext context;

    public ListCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(String arguments) {
        context.ui.printTasks(context.taskList);
    }
}
