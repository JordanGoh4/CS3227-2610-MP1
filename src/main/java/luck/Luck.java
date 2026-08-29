package luck;

import java.nio.file.Files;
import java.nio.file.Path;
import luck.command.CommandHandler;
import luck.exception.LuckException;
import luck.storage.TaskStorage;
import luck.ui.ConsoleUI;
import luck.model.TaskList;

/** Entry point for the Luck task-management chatbot. */
public class Luck {
    private static final String BANNER =
            " _                _        \n"
                    + "| |    _   _  ___| | __    \n"
                    + "| |   | | | |/ __| |/ /    \n"
                    + "| |___| |_| | (__|   <     \n"
                    + "|_____|\\__,_|\\___|_|\\_\\    \n";

    private static final Path DATA_FILE = findProjectRoot().resolve("data").resolve("luck.txt");

    public static String getBanner() {
        return BANNER;
    }

    public static void main(String[] args) {
        TaskStorage storage = new TaskStorage(DATA_FILE);
        storage.ensureFileExists();
        TaskList taskList = new TaskList(storage.loadTasks());
        ConsoleUI ui = new ConsoleUI();
        CommandHandler handler = new CommandHandler(taskList, storage, ui);

        ui.printGreeting();
        while (true) {
            String input = ui.readInput();
            ui.printSeparator();
            try {
                if (!handler.handle(input)) {
                    break;
                }
            } catch (LuckException e) {
                ui.printMessage("     OOPS!!! " + e.getMessage());
            }
        }
        ui.close();
    }

    private static Path findProjectRoot() {
        Path current = Path.of("").toAbsolutePath();
        for (int i = 0; i < 10; i++) {
            if (Files.exists(current.resolve("src"))
                    && (Files.exists(current.resolve("build.gradle"))
                    || Files.exists(current.resolve("pom.xml"))
                    || Files.exists(current.resolve(".git")))) {
                return current;
            }
            if (current.getParent() == null) {
                break;
            }
            current = current.getParent();
        }
        return Path.of("").toAbsolutePath();
    }
}
