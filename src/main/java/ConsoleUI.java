import java.util.Scanner;

public class ConsoleUI {
    private static final String SEPARATOR = "____________________________________________________________";
    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void printGreeting() {
        System.out.println(SEPARATOR);
        System.out.print(Luck.getBanner());
        System.out.println("Hello! I'm Luck.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }

    public void printSeparator() {
        System.out.println(SEPARATOR);
    }

    public void printTasks(TaskList taskList) {
        if (taskList == null || taskList.isEmpty()) {
            System.out.println("     No tasks yet.");
            return;
        }

        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            try {
                System.out.println("     " + (i + 1) + "." + taskList.get(i));
            } catch (LuckException e) {
                System.out.println("     OOPS!!! " + e.getMessage());
            }
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    public String readInput() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
