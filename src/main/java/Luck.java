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
            System.out.println("     " + input);
            System.out.println(SEPARATOR);

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("     Bye. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            }
        }

        scanner.close();
    }

    private static void printGreeting() {
        System.out.println(SEPARATOR);
        System.out.print(getBanner());
        System.out.println("Hello! I'm " + BOT_NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }
}
