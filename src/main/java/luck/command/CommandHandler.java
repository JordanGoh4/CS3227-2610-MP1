package luck.command;

import java.util.HashMap;
import java.util.Map;
import luck.exception.LuckException;
import luck.model.*;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;

/** Finds and executes commands entered by the user. */
public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    /** Creates a handler and registers all commands supported by Luck. */
    public CommandHandler(TaskList list, TaskStorage storage, ConsoleUI ui) {
        register("todo", new TodoCommand(list, storage, ui));
        register("deadline", new DeadlineCommand(list, storage, ui));
        register("event", new EventCommand(list, storage, ui));
        register("list", new ListCommand(list, ui));
        register("mark", new MarkCommand(list, storage, ui));
        register("unmark", new UnmarkCommand(list, storage, ui));
        register("bye", new ByeCommand(ui));
    }

    /** Associates a user-facing keyword with a command implementation. */
    private void register(String keyword, Command command) { commands.put(keyword, command); }

    /** Executes the command represented by the user's input. */
    public boolean handle(String input) throws LuckException {
        if (input == null || input.trim().isEmpty()) throw new LuckException("This can't be empty, do better.");
        String[] parts = input.trim().split("\\s+", 2);
        Command command = commands.get(parts[0].toLowerCase());
        if (command == null) throw new LuckException("No luck there, I have no idea what this mean LOL.");
        command.execute(parts.length > 1 ? parts[1] : "");
        return !(command instanceof ByeCommand);
    }

    /** Defines the behavior required by every Luck command. */
    private interface Command { void execute(String arguments) throws LuckException; }

    /** Adds a todo task to the task list. */
    private static class TodoCommand implements Command {
        private final TaskList list; private final TaskStorage storage; private final ConsoleUI ui;
        TodoCommand(TaskList list, TaskStorage storage, ConsoleUI ui) { this.list = list; this.storage = storage; this.ui = ui; }
        public void execute(String text) throws LuckException {
            if (text.trim().isEmpty()) throw new LuckException("This can't be empty, do better.");
            Task task = new Todo(text.trim()); list.add(task); storage.saveTasks(list.getAll()); ui.printTaskAdded(task, list.size());
        }
    }

    /** Adds a deadline task to the task list. */
    private static class DeadlineCommand implements Command {
        private final TaskList list; private final TaskStorage storage; private final ConsoleUI ui;
        DeadlineCommand(TaskList list, TaskStorage storage, ConsoleUI ui) { this.list = list; this.storage = storage; this.ui = ui; }
        public void execute(String text) throws LuckException {
            String[] parts = text.split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) throw new LuckException("This ain't valid my friend.");
            Task task = new Deadline(parts[0].trim(), parts[1].trim()); list.add(task); storage.saveTasks(list.getAll()); ui.printTaskAdded(task, list.size());
        }
    }

    /** Adds an event task to the task list. */
    private static class EventCommand implements Command {
        private final TaskList list; private final TaskStorage storage; private final ConsoleUI ui;
        EventCommand(TaskList list, TaskStorage storage, ConsoleUI ui) { this.list = list; this.storage = storage; this.ui = ui; }
        public void execute(String text) throws LuckException {
            String[] from = text.split(" /from ", 2);
            String[] to = from.length == 2 ? from[1].split(" /to ", 2) : new String[0];
            if (from.length < 2 || to.length < 2 || from[0].trim().isEmpty() || to[0].trim().isEmpty() || to[1].trim().isEmpty()) throw new LuckException("This ain't valid my friend.");
            Task task = new Event(from[0].trim(), to[0].trim(), to[1].trim()); list.add(task); storage.saveTasks(list.getAll()); ui.printTaskAdded(task, list.size());
        }
    }

    /** Displays all tasks in the task list. */
    private static class ListCommand implements Command {
        private final TaskList list; private final ConsoleUI ui;
        ListCommand(TaskList list, ConsoleUI ui) { this.list = list; this.ui = ui; }
        public void execute(String text) { ui.printTasks(list); }
    }

    /** Marks a selected task as completed. */
    private static class MarkCommand implements Command {
        private final TaskList list; private final TaskStorage storage; private final ConsoleUI ui;
        MarkCommand(TaskList list, TaskStorage storage, ConsoleUI ui) { this.list = list; this.storage = storage; this.ui = ui; }
        public void execute(String text) throws LuckException { Task task = getTask(text); task.markAsDone(); storage.saveTasks(list.getAll()); ui.printMessage("     Nice! I've marked this task as done:"); ui.printMessage("       " + task); }
        private Task getTask(String text) throws LuckException { try { return list.get(Integer.parseInt(text.trim()) - 1); } catch (NumberFormatException e) { throw new LuckException("This ain't valid my friend."); } }
    }

    /** Marks a selected task as incomplete. */
    private static class UnmarkCommand implements Command {
        private final TaskList list; private final TaskStorage storage; private final ConsoleUI ui;
        UnmarkCommand(TaskList list, TaskStorage storage, ConsoleUI ui) { this.list = list; this.storage = storage; this.ui = ui; }
        public void execute(String text) throws LuckException { Task task; try { task = list.get(Integer.parseInt(text.trim()) - 1); } catch (NumberFormatException e) { throw new LuckException("This ain't valid my friend."); } task.markAsNotDone(); storage.saveTasks(list.getAll()); ui.printMessage("     OK, I've marked this task as not done yet:"); ui.printMessage("       " + task); }
    }

    /** Ends the current Luck session. */
    private static class ByeCommand implements Command {
        private final ConsoleUI ui;
        ByeCommand(ConsoleUI ui) { this.ui = ui; }
        public void execute(String text) { ui.printMessage("     Bye. Hope to see you again soon!"); }
    }
}
