import java.util.Scanner;

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
    private static final Task[] TASKS = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static String getBanner() {
        return BANNER;
    }

    public static void main(String[] args) {
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            System.out.println(SEPARATOR);

            try {
                if (input == null || input.trim().isEmpty()) {
                    throw new LuckException("This can't be empty, do better.");
                }

                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println(SEPARATOR);
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
                System.out.println("     OOPS!!! " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void addTodoTask(String taskDescription) throws LuckException {
        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            throw new LuckException("This can't be empty, do better.");
        }

        if (taskCount >= MAX_TASKS) {
            throw new LuckException("Your task list is full.");
        }

        TASKS[taskCount] = new Todo(taskDescription.trim());
        taskCount++;
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + TASKS[taskCount - 1]);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
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

        if (taskCount >= MAX_TASKS) {
            throw new LuckException("Your task list is full.");
        }

        TASKS[taskCount] = new Deadline(description, by);
        taskCount++;
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + TASKS[taskCount - 1]);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
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

        if (taskCount >= MAX_TASKS) {
            throw new LuckException("Your task list is full.");
        }

        TASKS[taskCount] = new Event(description, from, to);
        taskCount++;
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + TASKS[taskCount - 1]);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    private static void markTask(String indexText) throws LuckException {
        try {
            int index = Integer.parseInt(indexText) - 1;
            if (index < 0 || index >= taskCount) {
                throw new LuckException("This ain't valid my friend.");
            }

            TASKS[index].markAsDone();
            System.out.println("     Nice! I've marked this task as done:");
            System.out.println("       " + TASKS[index]);
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
    }

    private static void unmarkTask(String indexText) throws LuckException {
        try {
            int index = Integer.parseInt(indexText) - 1;
            if (index < 0 || index >= taskCount) {
                throw new LuckException("This ain't valid my friend.");
            }

            TASKS[index].markAsNotDone();
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("       " + TASKS[index]);
        } catch (NumberFormatException e) {
            throw new LuckException("This ain't valid my friend.");
        }
    }

    private static void printTasks() {
        if (taskCount == 0) {
            System.out.println("     No tasks yet.");
            return;
        }

        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + "." + TASKS[i]);
        }
    }

    private static void printGreeting() {
        System.out.println(SEPARATOR);
        System.out.print(getBanner());
        System.out.println("Hello! I'm " + BOT_NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }
}
