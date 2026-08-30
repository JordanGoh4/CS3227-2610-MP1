package luck.command;
import java.util.HashMap;
import java.util.Map;
import luck.exception.LuckException;
/** Dispatches input to command objects. */
public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();
    public CommandHandler(CommandContext context) {
        commands.put("todo", new TodoCommand(context));
        commands.put("deadline", new DeadlineCommand(context));
        commands.put("event", new EventCommand(context));
        commands.put("list", new ListCommand(context));
        commands.put("find", new FindCommand(context));
        commands.put("weather", new WeatherCommand(context));
        commands.put("mark", new MarkCommand(context));
        commands.put("unmark", new UnmarkCommand(context));
        commands.put("delete", new DeleteCommand(context));
        commands.put("bye", new ByeCommand(context));
    }
    /** Executes one input line; returns false for bye. */
    public boolean handle(String input) throws LuckException {
        if (input == null || input.trim().isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }
        String[] parts = input.trim().split("\\s+", 2);
        Command command = commands.get(parts[0].toLowerCase());
        if (command == null) {
            throw new LuckException("No luck there, I have no idea what this mean LOL.");
        }
        command.execute(parts.length > 1 ? parts[1] : "");
        return !(command instanceof ByeCommand);
    }
}
