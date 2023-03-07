package tracker.data;

import java.util.*;

public final class StudentData {
    private static final StudentData instance = new StudentData();

    private StudentData() {
    }

    public static StudentData getInstance() {
        return instance;
    }

    private int nextStudentId = 10000;
    private final Map<Integer, StudentRecord> students = new HashMap<>();

    /**
     * Add student.
     *
     * @param studentRecord Student record to add.
     * @return Student id of student record.
     */
    public synchronized int addStudent(StudentRecord studentRecord) {
        int studentId = nextStudentId;
        studentRecord.setStudentId(studentId);
        students.put(studentId, studentRecord);
        nextStudentId++;
        return studentId;
    }

    /**
     * Get number of students.
     *
     * @return Number of student records.
     */
    public int getNumberOfStudents() {
        return students.size();
    }

    /**
     * Get list of students.
     *
     * @return List of student records.
     */
    public List<StudentRecord> getStudents() {
        List<StudentRecord> studentRecords = new ArrayList<>();
        students.forEach((key, value) -> studentRecords.add(value));
        return studentRecords;
    }

    /**
     * Get student by id.
     *
     * @param studentId Student id to find.
     * @return Optional student record. The Optional is empty if the student was not found.
     */
    public Optional<StudentRecord> getStudent(int studentId) {
        StudentRecord studentRecord = students.getOrDefault(studentId, null);
        return studentRecord == null ? Optional.empty() : Optional.of(studentRecord);
    }

    /**
     * Get student by email.
     *
     * @param email Email to find.
     * @return Optional student record. The Optional is empty if the student was not found.
     */
    public Optional<StudentRecord> getStudentByEmail(String email) {
        StudentRecord studentRecord = null;
        for (Map.Entry<Integer, StudentRecord> entry : students.entrySet()) {
            if (entry.getValue().email().equals(email)) {
                studentRecord = entry.getValue();
            }
        }
        return studentRecord == null ? Optional.empty() : Optional.of(studentRecord);
    }

    /**
     * Get student course notification status for studentId and courseId.<br>
     * Status values:<br>
     * 0 = Course not completed.<br>
     * 1 = Course completed, needs notification.<br>
     * 2 = Course completed, notification sent.
     *
     * @param studentId Student id to find.
     * @param courseId  Course id to find.
     * @return Optional Integer indicating notification status. The Optional is empty
     * if the student was not found.
     */
    public Optional<Integer> getCourseNotificationStatus(int studentId, int courseId) {
        StudentRecord studentRecord = students.getOrDefault(studentId, null);
        if (studentRecord == null) {
            return Optional.empty();
        } else {
            switch (courseId) {
                case 1 -> {
                    return Optional.of(studentRecord.course1NotificationStatus());
                }
                case 2 -> {
                    return Optional.of(studentRecord.course2NotificationStatus());
                }
                case 3 -> {
                    return Optional.of(studentRecord.course3NotificationStatus());
                }
                case 4 -> {
                    return Optional.of(studentRecord.course4NotificationStatus());
                }
                default -> throw new IllegalStateException("Unexpected value: " + courseId);
            }
        }
    }

    /**
     * Update student course points.
     *
     * @param studentPointsRecord Student points record with values to add.
     * @return Optional student record with updated points. The Optional is empty
     * if the student was not found.
     */
    public Optional<StudentRecord> updatePoints(StudentPointsRecord studentPointsRecord) {
        StudentRecord studentRecord = students.getOrDefault(studentPointsRecord.studentId(), null);
        if (studentRecord != null) {
            studentRecord.setCourse1Points(studentRecord.course1Points() + studentPointsRecord.course1Points());
            studentRecord.setCourse2Points(studentRecord.course2Points() + studentPointsRecord.course2Points());
            studentRecord.setCourse3Points(studentRecord.course3Points() + studentPointsRecord.course3Points());
            studentRecord.setCourse4Points(studentRecord.course4Points() + studentPointsRecord.course4Points());
        }
        students.put(studentPointsRecord.studentId(), studentRecord);
        return studentRecord == null ? Optional.empty() : Optional.of(studentRecord);
    }

    /**
     * Set student course notification status for studentId and courseId.
     *
     * @param studentId Student id to update.
     * @param courseId  Course id to update.
     */
    public void setCourseNotificationStatus(int studentId, int courseId, int status) {
        StudentRecord studentRecord = students.getOrDefault(studentId, null);
        if (studentRecord != null) {
            switch (courseId) {
                case 1 -> studentRecord.setCourse1NotificationStatus(status);
                case 2 -> studentRecord.setCourse2NotificationStatus(status);
                case 3 -> studentRecord.setCourse3NotificationStatus(status);
                case 4 -> studentRecord.setCourse4NotificationStatus(status);
                default -> throw new IllegalStateException("Unexpected value: " + courseId);
            }
            students.put(studentId, studentRecord);
        }
    }

    /**
     * Delete student by id.
     *
     * @param studentId Student id to delete.
     */
    public void deleteStudent(int studentId) {
        students.remove(studentId);
    }
}
