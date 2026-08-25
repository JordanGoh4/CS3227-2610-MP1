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
    private static final String[] TASKS = new String[MAX_TASKS];
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

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            }

            if (input.equalsIgnoreCase("list")) {
                printTasks();
                continue;
            }

            addTask(input);
            System.out.println("     added: " + input);
        }

        scanner.close();
    }

    private static void addTask(String task) {
        if (taskCount < MAX_TASKS) {
            TASKS[taskCount] = task;
            taskCount++;
        }
    }

    private static void printTasks() {
        if (taskCount == 0) {
            System.out.println("     No tasks yet.");
            return;
        }

        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + ". " + TASKS[i]);
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
