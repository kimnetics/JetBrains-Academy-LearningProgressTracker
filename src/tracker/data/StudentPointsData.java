package tracker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StudentPointsData {
    private static final StudentPointsData instance = new StudentPointsData();

    private StudentPointsData() {
    }

    public static StudentPointsData getInstance() {
        return instance;
    }

    private int nextStudentPointsId = 10000;
    private final Map<Integer, StudentPointsRecord> studentPoints = new HashMap<>();

    /**
     * Add student points.
     *
     * @param studentPointsRecord Student points record to add.
     * @return Student points id of student points record.
     */
    public synchronized int addStudentPoints(StudentPointsRecord studentPointsRecord) {
        int studentPointsId = nextStudentPointsId;
        studentPointsRecord.setStudentId(studentPointsId);
        studentPoints.put(studentPointsId, studentPointsRecord);
        nextStudentPointsId++;
        return studentPointsId;
    }

    /**
     * Get list of student points.
     *
     * @return List of student points records.
     */
    public List<StudentPointsRecord> getStudentPoints() {
        List<StudentPointsRecord> studentPointsRecords = new ArrayList<>();
        studentPoints.forEach((key, value) -> studentPointsRecords.add(value));
        return studentPointsRecords;
    }

    /**
     * Get list of student points by course id.
     *
     * @param courseId Course id to find.
     * @return List of student points records.
     */
    public List<StudentPointsRecord> getStudentPointsByCourseId(int courseId) {
        List<StudentPointsRecord> studentPointsRecords = new ArrayList<>();
        for (Map.Entry<Integer, StudentPointsRecord> entry : studentPoints.entrySet()) {
            StudentPointsRecord studentPointsRecord = entry.getValue();
            switch (courseId) {
                case 1 -> {
                    if (studentPointsRecord.course1Points() > 0) studentPointsRecords.add(studentPointsRecord);
                }
                case 2 -> {
                    if (studentPointsRecord.course2Points() > 0) studentPointsRecords.add(studentPointsRecord);
                }
                case 3 -> {
                    if (studentPointsRecord.course3Points() > 0) studentPointsRecords.add(studentPointsRecord);
                }
                case 4 -> {
                    if (studentPointsRecord.course4Points() > 0) studentPointsRecords.add(studentPointsRecord);
                }
                default -> throw new IllegalStateException("Unexpected value: " + courseId);
            }
        }
        return studentPointsRecords;
    }

    /**
     * Delete student points by id.
     *
     * @param studentPointsId Student points id to delete.
     */
    public void deleteStudentPoints(int studentPointsId) {
        studentPoints.remove(studentPointsId);
    }
}
