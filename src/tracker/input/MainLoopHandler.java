package tracker.input;

import tracker.data.StudentData;
import tracker.data.StudentRecord;
import tracker.notification.Notify;

import java.util.List;
import java.util.Scanner;

public final class MainLoopHandler {
    private static final MainLoopHandler instance = new MainLoopHandler();

    private MainLoopHandler() {
    }

    public static MainLoopHandler getInstance() {
        return instance;
    }

    private final AddPointsHandler addPointsHandler = AddPointsHandler.getInstance();
    private final AddStudentHandler addStudentHandler = AddStudentHandler.getInstance();
    private final FindHandler findHandler = FindHandler.getInstance();
    private final Notify notify = Notify.getInstance();
    private final StatisticsHandler statisticsHandler = StatisticsHandler.getInstance();
    private final StudentData studentData = StudentData.getInstance();

    enum Command {
        add_students("Add Students"),
        list("List Student Ids"),
        find("Find Student"),
        add_points("Add Points for Students"),
        statistics("Show Course Statistics"),
        notify("Notify Students"),
        exit("Exit");

        public final String label;

        Command(String label) {
            this.label = label;
        }
    }

    /**
     * Do main command input loop.
     *
     * @param scanner Scanner to use for input.
     */
    public void doMainLoop(Scanner scanner) {
        boolean exitLoop = false;
        do {
            // Enter command.
            Command command = enterCommand(scanner);

            // Handle command.
            switch (command) {
                // Add students.
                case add_students -> addStudentHandler.doAddStudentLoop(scanner);
                // List student ids.
                case list -> displayStudentIdList();
                // Find student.
                case find -> findHandler.doFindLoop(scanner);
                // Add points for students.
                case add_points -> addPointsHandler.doAddPointsLoop(scanner);
                // Show course statistics.
                case statistics -> statisticsHandler.doStatisticsLoop(scanner);
                // Notify students.
                case notify -> notify.sendNotifications();
                // Exit program.
                case exit -> {
                    System.out.println("Bye!");
                    exitLoop = true;
                }
            }
        } while (!exitLoop);
    }

    /**
     * Enter command from user.
     *
     * @param scanner Scanner to use for input.
     * @return Command entered by user. Only valid commands will be returned.
     */
    Command enterCommand(Scanner scanner) {
        Command command = null;

        // Enter command.
        while (true) {
            String input = scanner.nextLine()
                    .trim()
                    .replaceAll("\\s+", " ")
                    .replaceAll(" ", "_");

            // Show warning if nothing was entered.
            if (input.isEmpty()) {
                System.out.println("No input");
                continue;
            }

            // Check if known command was entered.
            for (Command option : Command.values()) {
                if (option.name().equalsIgnoreCase(input)) {
                    command = option;
                    break;
                }
            }

            // Show warning if unknown command was entered.
            if (command == null) {
                System.out.println("Enter 'exit' to exit the program.");
                continue;
            }

            // We have a known command so exit entry loop,
            break;
        }

        return command;
    }

    /**
     * Display list of student ids.
     */
    void displayStudentIdList() {
        List<StudentRecord> studentRecords = studentData.getStudents();
        System.out.println("Students:");
        if (studentRecords.isEmpty()) {
            System.out.println("No students found.");
        } else {
            studentRecords.stream()
                    .mapToInt(StudentRecord::studentId)
                    .forEach(System.out::println);
        }
    }
}
