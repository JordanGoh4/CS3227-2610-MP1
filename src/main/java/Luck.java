import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Luck {
    /**
     * ASCII banner used as the chatbot banner.
     */
    private static final String BANNER =
            " _                _        \n"
                    + "| |    _   _  ___| | __    \n"
                    + "| |   | | | |/ __| |/ /    \n"
                    + "| |___| |_| | (__|   <     \n"
                    + "|_____|\\__,_|\\___|_|\\_\\    \n";

    private static final String BOT_NAME = "Luck";
    private static final String SEPARATOR = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final Path DATA_FILE = findProjectRoot().resolve("data").resolve("luck.txt");
    private static final TaskStorage TASK_STORAGE = new TaskStorage(DATA_FILE);
    private static final TaskList TASK_LIST = new TaskList();
    private static final ConsoleUI UI = new ConsoleUI();

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

    public static String getBanner() {
        return BANNER;
    }

    public static void main(String[] args) {
        ensureStorageExists();
        loadTasks();
        UI.printGreeting();

        while (true) {
            String input = UI.readInput();
            UI.printSeparator();

            try {
                if (input == null || input.trim().isEmpty()) {
                    throw new LuckException("This can't be empty, do better.");
                }

                if (input.equalsIgnoreCase("bye")) {
                    UI.printMessage("     Bye. Hope to see you again soon!");
                    UI.printSeparator();
                    break;
                }

                if (input.equalsIgnoreCase("list")) {
                    printTasks();
                    continue;
                }

                if (input.toLowerCase().startsWith("mark ")) {
                    markTask(input.substring(5));
                    continue;
                }

                if (input.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(input.substring(7));
                    continue;
                }

                if (input.toLowerCase().startsWith("todo ")) {
                    addTodoTask(input.substring(5));
                    continue;
                }

                if (input.toLowerCase().startsWith("deadline ")) {
                    addDeadlineTask(input.substring(9));
                    continue;
                }

                if (input.toLowerCase().startsWith("event ")) {
                    addEventTask(input.substring(6));
                    continue;
                }

                throw new LuckException("No luck there, I have no idea what this mean LOL.");
            } catch (LuckException e) {
                UI.printMessage("     OOPS!!! " + e.getMessage());
            }
        }

        UI.close();
    }

    private static void addTodoTask(String taskDescription) throws LuckException {
        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }

        if (TASK_LIST.size() >= MAX_TASKS) {
            throw new LuckException("Your task list is full.");
        }

        Task task = new Todo(taskDescription.trim());
        TASK_LIST.add(task);
        saveTasks();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + TASK_LIST.size() + " tasks in the list.");
    }

    private static void addDeadlineTask(String rawInput) throws LuckException {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }

        String[] parts = rawInput.split(" /by ", 2);
        String description = parts[0].trim();
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }

        if (by.isEmpty()) {
            throw new LuckException("This ain't valid my friend.");
        }

        if (TASK_LIST.size() >= MAX_TASKS) {
            throw new LuckException("Your task list is full.");
        }

        Task task = new Deadline(description, by);
        TASK_LIST.add(task);
        saveTasks();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + TASK_LIST.size() + " tasks in the list.");
    }

    private static void addEventTask(String rawInput) throws LuckException {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }

        String[] firstSplit = rawInput.split(" /from ", 2);
        String description = firstSplit[0].trim();
        String from = "";
        String to = "";

        if (description.isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }

        if (firstSplit.length > 1) {
            String[] secondSplit = firstSplit[1].split(" /to ", 2);
            from = secondSplit[0].trim();
            to = secondSplit.length > 1 ? secondSplit[1].trim() : "";
        }

        if (from.isEmpty() || to.isEmpty()) {
            throw new LuckException("This ain't valid my friend.");
        }

        if (TASK_LIST.size() >= MAX_TASKS) {
            throw new LuckException("Your task list is full.");
        }

        Task task = new Event(description, from, to);
        TASK_LIST.add(task);
        saveTasks();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + TASK_LIST.size() + " tasks in the list.");
    }

    private static void markTask(String indexText) throws LuckException {
        try {
            int index = Integer.parseInt(indexText) - 1;
            if (index < 0 || index >= TASK_LIST.size()) {
                throw new LuckException("This ain't valid my friend.");
            }

            TASK_LIST.get(index).markAsDone();
            saveTasks();
            System.out.println("     Nice! I've marked this task as done:");
            System.out.println("       " + TASK_LIST.get(index));
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
    }

    private static void unmarkTask(String indexText) throws LuckException {
        try {
            int index = Integer.parseInt(indexText) - 1;
            if (index < 0 || index >= TASK_LIST.size()) {
                throw new LuckException("This ain't valid my friend.");
            }

            TASK_LIST.get(index).markAsNotDone();
            saveTasks();
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("       " + TASK_LIST.get(index));
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
    }

    private static void ensureStorageExists() {
        TASK_STORAGE.ensureFileExists();
    }

    private static void loadTasks() {
        List<Task> tasks = TASK_STORAGE.loadTasks();
        for (Task task : tasks) {
            if (TASK_LIST.size() >= MAX_TASKS) {
                break;
            }
            try {
                TASK_LIST.add(task);
            } catch (LuckException e) {
                throw new RuntimeException("Unable to reload saved tasks.", e);
            }
        }
    }

    private static void saveTasks() {
        TASK_STORAGE.saveTasks(TASK_LIST.getAll());
    }

    private static void printTasks() {
        UI.printTasks(TASK_LIST);
    }

    private static void printGreeting() {
        UI.printGreeting();
    }
}
