package tracker.notification;

import tracker.data.StudentData;
import tracker.data.StudentRecord;

import java.util.List;

public final class Notify {
    private static final Notify instance = new Notify();

    private Notify() {
    }

    public static Notify getInstance() {
        return instance;
    }

    private StudentData studentData = StudentData.getInstance();

    private final String emailTemplate = """
            To: %EMAIL_ADDRESS%
            Re: Your Learning Progress
            Hello, %FULL_USER_NAME%! You have accomplished our %COURSE_NAME% course!""";

    /**
     * Set student data class to use.
     *
     * @param studentData Student data class to use.
     */
    public void setStudentData(StudentData studentData) {
        this.studentData = studentData;
    }

    /**
     * "Send" notifications to all students with completed courses who have not yet received
     * a notification for the course. Sending at the moment means displaying email text
     * to the console.
     */
    public void sendNotifications() {
        // Find students with completed courses needing notification.
        // Send emails to them and update notification status.
        int notificationCount = 0;
        List<StudentRecord> studentRecords = studentData.getStudents();
        for (StudentRecord studentRecord : studentRecords) {
            int studentId = studentRecord.studentId();
            if (studentRecord.course1NotificationStatus() == 1) {
                sendEmail(studentRecord, 1);
                studentData.setCourseNotificationStatus(studentId, 1, 2);
                notificationCount++;
            }
            if (studentRecord.course2NotificationStatus() == 1) {
                sendEmail(studentRecord, 2);
                studentData.setCourseNotificationStatus(studentId, 2, 2);
                notificationCount++;
            }
            if (studentRecord.course3NotificationStatus() == 1) {
                sendEmail(studentRecord, 3);
                studentData.setCourseNotificationStatus(studentId, 3, 2);
                notificationCount++;
            }
            if (studentRecord.course4NotificationStatus() == 1) {
                sendEmail(studentRecord, 4);
                studentData.setCourseNotificationStatus(studentId, 4, 2);
                notificationCount++;
            }
        }

        System.out.printf("Total %d students have been notified.%n", notificationCount);
    }

    /**
     * Display email.
     *
     * @param studentRecord Student record which provides information for email.
     * @param courseId      Course id which provides information for email.
     */
    private void sendEmail(StudentRecord studentRecord, int courseId) {
        final String[] courseNames = new String[5];
        courseNames[1] = "Java";
        courseNames[2] = "DSA";
        courseNames[3] = "Databases";
        courseNames[4] = "Spring";

        String email = emailTemplate
                .replace("%EMAIL_ADDRESS%", studentRecord.email())
                .replace("%FULL_USER_NAME%", studentRecord.firstName() + " " + studentRecord.lastName())
                .replace("%COURSE_NAME%", courseNames[courseId]);

        System.out.println(email);
    }
}
