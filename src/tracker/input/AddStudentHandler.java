package tracker.input;

import tracker.data.StudentData;
import tracker.data.StudentRecord;

import java.util.*;

public final class AddStudentHandler {
    private static final AddStudentHandler instance = new AddStudentHandler();

    private AddStudentHandler() {
    }

    public static AddStudentHandler getInstance() {
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
     * Do add student input loop.
     *
     * @param scanner Scanner to use for input.
     */
    public void doAddStudentLoop(Scanner scanner) {
        System.out.println("Enter student credentials or 'back' to return:");

        // Enter student credentials or 'back'.
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
                System.out.printf("Total %d students have been added.%n", studentData.getNumberOfStudents());
                break;
            }

            // Parse input elements into a student record.
            String[] elements = input.split(" ");
            Optional<StudentRecord> studentRecordOptional = buildStudentRecord(elements);
            if (studentRecordOptional.isEmpty()) {
                System.out.println("Incorrect credentials.");
                continue;
            }

            // Validate student record.
            var studentRecord = studentRecordOptional.get();
            List<String> results = validateStudentRecord(studentRecord);

            // Show warning if validation failed.
            if (!results.isEmpty()) {
                System.out.println(results.get(0));
                continue;
            }

            // Add student to data store.
            studentData.addStudent((studentRecord));
            System.out.println("The student has been added.");
        }
    }

    /**
     * Parse input elements into a student record.
     *
     * @param elements Information entered by user.
     * @return Optional student record containing the input information. The Optional
     * is empty if the input elements could not be parsed.
     */
    Optional<StudentRecord> buildStudentRecord(String[] elements) {
        // Are there not enough input elements?
        if (elements.length < 3) {
            return Optional.empty();
        }

        // Split input elements into fields.

        int lastElementIndex = elements.length - 1;

        String firstName = elements[0];
        StringJoiner lastName = new StringJoiner(" ");
        for (int i = 1; i < lastElementIndex; i++) {
            lastName.add(elements[i]);
        }
        String email = elements[lastElementIndex];

        return Optional.of(new StudentRecord(firstName, lastName.toString(), email));
    }

    /**
     * Validate a student record.
     *
     * @param studentRecord Student record to validate.
     * @return Empty List if validation was successful. Otherwise, returns a List containing
     * validation failure messages if validation failed.
     */
    List<String> validateStudentRecord(StudentRecord studentRecord) {
        List<String> results = new ArrayList<>();

        if (!isNameValid(studentRecord.firstName())) {
            results.add("Incorrect first name.");
        }

        // A last name can have multiple words so validate each one.
        String[] lastNameWords = studentRecord.lastName().split(" ");
        for (String lastNameWord : lastNameWords) {
            if (!isNameValid(lastNameWord)) {
                results.add("Incorrect last name.");
                break;
            }
        }

        if (!isEmailValid(studentRecord.email())) {
            results.add("Incorrect email.");
        }

        // Was email already used?
        if (studentData.getStudentByEmail(studentRecord.email()).isPresent()) {
            results.add("This email is already taken.");
        }

        return results;
    }

    boolean isNameValid(String name) {
        return (name.matches("['\\-A-Za-z]{2,}"))
                && (!name.matches("^['\\-].+|.+['\\-]$"))
                && (!name.matches(".+['\\-]{2,}.+"));
    }

    boolean isEmailValid(String email) {
        return (email.matches("^.+@.+\\..+$"));
    }
}
