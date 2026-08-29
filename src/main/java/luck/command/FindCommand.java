package luck.command;

import luck.exception.LuckException;

/** Searches task descriptions for a keyword. */
public class FindCommand implements Command {
    private final CommandContext context;

    public FindCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(String arguments) throws LuckException {
        String keyword = arguments.trim();
        if (keyword.isEmpty()) {
            throw new LuckException("Please provide a keyword to search for.");
        }

        context.getUi().printMatchingTasks(
                context.getTaskList().find(keyword));
    }
}
