package luck.gui;

import java.util.List;
import java.util.function.Consumer;
import luck.model.Task;
import luck.model.TaskList;
import luck.ui.ConsoleUI;

/** Adapts command output for display in the JavaFX chat panel. */
public class GuiConsoleUI extends ConsoleUI {
    private final Consumer<String> outputConsumer;

    /** Creates a UI that sends command output to the supplied consumer. */
    public GuiConsoleUI(Consumer<String> outputConsumer) {
        this.outputConsumer = outputConsumer;
    }

    /** Sends a plain command message to the chat panel. */
    @Override
    public void printMessage(String message) {
        outputConsumer.accept(message);
    }

    /** Sends matching tasks to the chat panel. */
    @Override
    public void printMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            printMessage("No matching tasks found.");
            return;
        }
        printMessage("Matching tasks:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            printMessage((i + 1) + ". " + matchingTasks.get(i));
        }
    }

    /** Sends the complete task list to the chat panel when explicitly requested. */
    @Override
    public void printTasks(TaskList tasks) {
        if (tasks == null || tasks.isEmpty()) {
            printMessage("No tasks yet.");
            return;
        }
        printMessage("Tasks in your itinerary:");
        for (int i = 0; i < tasks.size(); i++) {
            try {
                printMessage((i + 1) + ". " + tasks.get(i));
            } catch (Exception exception) {
                printMessage("Unable to display that task.");
            }
        }
    }

    /** Sends task-creation feedback to the chat panel. */
    @Override
    public void printTaskAdded(Task task, int taskCount) {
        printMessage("Added: " + task + " (" + taskCount + " item(s) in your itinerary)");
    }
}
