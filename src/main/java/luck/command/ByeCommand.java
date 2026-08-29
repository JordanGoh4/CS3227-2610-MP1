package luck.command;
/** Ends the session. */
public class ByeCommand implements Command {
    private final CommandContext context;

    public ByeCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(String arguments) {
        context.getUi().printMessage("     Bye. Hope to see you again soon!");
    }
}
