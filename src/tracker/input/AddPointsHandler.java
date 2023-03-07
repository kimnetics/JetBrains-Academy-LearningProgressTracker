package tracker.input;

import tracker.data.StudentData;
import tracker.data.StudentPointsData;
import tracker.data.StudentPointsRecord;
import tracker.data.StudentRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public final class AddPointsHandler {
    private static final AddPointsHandler instance = new AddPointsHandler();

    private AddPointsHandler() {
        totalCoursePoints[1] = 600;
        totalCoursePoints[2] = 400;
        totalCoursePoints[3] = 480;
        totalCoursePoints[4] = 550;
    }

    public static AddPointsHandler getInstance() {
        return instance;
    }

    private StudentData studentData = StudentData.getInstance();
    private StudentPointsData studentPointsData = StudentPointsData.getInstance();

    private final int[] totalCoursePoints = new int[5];

    /**
     * Set student data class to use.
     *
     * @param studentData Student data class to use.
     */
    public void setStudentData(StudentData studentData) {
        this.studentData = studentData;
    }

    /**
     * Set student points data class to use.
     *
     * @param studentPointsData Student points data class to use.
     */
    public void setStudentPointsData(StudentPointsData studentPointsData) {
        this.studentPointsData = studentPointsData;
    }

    /**
     * Do add points input loop.
     *
     * @param scanner Scanner to use for input.
     */
    public void doAddPointsLoop(Scanner scanner) {
        System.out.println("Enter an id and points or 'back' to return:");

        // Enter id and points or 'back'.
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

            // Parse input elements into a student points record.
            String[] elements = input.split(" ");
            Optional<StudentPointsRecord> studentPointsRecordOptional = buildStudentPointsRecord(elements);
            if (studentPointsRecordOptional.isEmpty()) {
                System.out.println("Incorrect points format.");
                continue;
            }

            // Validate student points record.
            var studentPointsRecord = studentPointsRecordOptional.get();
            List<String> results = validateStudentPointsRecord(studentPointsRecord);

            // Show warning if validation failed.
            if (!results.isEmpty()) {
                System.out.println(results.get(0));
                continue;
            }

            // Update student course points and notification status in data store.
            Optional<StudentRecord> studentRecordOptional = studentData.updatePoints((studentPointsRecord));
            if (studentRecordOptional.isPresent()) {
                var studentRecord = studentRecordOptional.get();
                int studentId = studentRecord.studentId();
                if ((studentRecord.course1Points() >= totalCoursePoints[1])
                        && (studentRecord.course1NotificationStatus() == 0)) {
                    studentData.setCourseNotificationStatus(studentId, 1, 1);
                }
                if ((studentRecord.course2Points() >= totalCoursePoints[2])
                        && (studentRecord.course2NotificationStatus() == 0)) {
                    studentData.setCourseNotificationStatus(studentId, 2, 1);
                }
                if ((studentRecord.course3Points() >= totalCoursePoints[3])
                        && (studentRecord.course3NotificationStatus() == 0)) {
                    studentData.setCourseNotificationStatus(studentId, 3, 1);
                }
                if ((studentRecord.course4Points() >= totalCoursePoints[4])
                        && (studentRecord.course4NotificationStatus() == 0)) {
                    studentData.setCourseNotificationStatus(studentId, 4, 1);
                }

                // Add student points record to data store.
                studentPointsData.addStudentPoints(studentPointsRecord);
            }
            System.out.println("Points updated.");
        }
    }

    /**
     * Parse input elements into a student points record.
     *
     * @param elements Information entered by user.
     * @return Optional student points record containing the input information. The
     * Optional is empty if the input elements could not be parsed.
     */
    Optional<StudentPointsRecord> buildStudentPointsRecord(String[] elements) {
        final int NUMBER_OF_ELEMENTS = 5;

        // Is there an incorrect number of input elements?
        if (elements.length != NUMBER_OF_ELEMENTS) {
            return Optional.empty();
        }

        // Convert input elements to ints.
        int[] elementsInt = new int[NUMBER_OF_ELEMENTS];
        try {
            for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                elementsInt[i] = Integer.parseInt(elements[i]);
                // Input element must not be negative.
                if (elementsInt[i] < 0) {
                    return Optional.empty();
                }
                // Course id input element must not be greater than total course points.
                if ((i > 0) && (elementsInt[i] > totalCoursePoints[i])) {
                    return Optional.empty();
                }
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return Optional.of(new StudentPointsRecord(elementsInt[0], elementsInt[1], elementsInt[2], elementsInt[3], elementsInt[4]));
    }

    /**
     * Validate a student points record.
     *
     * @param studentPointsRecord Student points record to validate.
     * @return Empty List if validation was successful. Otherwise, returns a List containing
     * validation failure messages if validation failed.
     */
    List<String> validateStudentPointsRecord(StudentPointsRecord studentPointsRecord) {
        List<String> results = new ArrayList<>();

        // Was student id not found?
        Optional<StudentRecord> studentRecord = studentData.getStudent(studentPointsRecord.studentId());
        if (studentRecord.isEmpty()) {
            results.add((String.format("No student is found for id=%d.", studentPointsRecord.studentId())));
        }

        return results;
    }
}
