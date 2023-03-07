package tracker.input;

import tracker.data.StudentData;
import tracker.data.StudentRecord;

import java.util.Optional;
import java.util.Scanner;

public final class FindHandler {
    private static final FindHandler instance = new FindHandler();

    private FindHandler() {
    }

    public static FindHandler getInstance() {
        return instance;
    }

    private StudentData studentData = StudentData.getInstance();

    /**
     * Set student data class to use.
     *
     * @param studentData Student data class to use.
     */
    public void setStudentData(StudentData studentData) {
        this.studentData = studentData;
    }

    /**
     * Do find input loop.
     *
     * @param scanner Scanner to use for input.
     */
    public void doFindLoop(Scanner scanner) {
        System.out.println("Enter an id or 'back' to return:");

        // Enter id or 'back'.
        while (true) {
            String input = scanner.nextLine()
                    .trim()
                    .replaceAll("\\s+", " ");

            // Show warning if nothing was entered.
            if (input.isEmpty()) {
                System.out.println("No input");
                continue;
            }

            // Exit entry loop if user is done with entry.
            if (input.equalsIgnoreCase("back")) {
                break;
            }

            // Get student record from data store.
            Optional<StudentRecord> studentRecordOptional = getStudent(input);
            if (studentRecordOptional.isEmpty()) {
                System.out.printf("No student is found for id=%s.%n", input);
                continue;
            }

            // Display student information.
            var studentRecord = studentRecordOptional.get();
            System.out.printf("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d%n",
                    studentRecord.studentId(),
                    studentRecord.course1Points(),
                    studentRecord.course2Points(),
                    studentRecord.course3Points(),
                    studentRecord.course4Points());
        }
    }

    /**
     * Get student record from data store.
     *
     * @param input Student id entered by user.
     * @return Optional student record. The Optional is empty if the student was not found.
     */
    Optional<StudentRecord> getStudent(String input) {
        // Is there an incorrect number of input elements?
        String[] elements = input.split(" ");
        if (elements.length != 1) {
            return Optional.empty();
        }

        // Convert input to int.
        int inputInt;
        try {
            inputInt = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        // Get student from data store.
        return studentData.getStudent(inputInt);
    }
}
