package tracker.data;

import java.util.Objects;

public final class StudentPointsRecord {
    private int studentPointsId;
    private int studentId;
    private int course1Points;
    private int course2Points;
    private int course3Points;
    private int course4Points;

    public StudentPointsRecord(
            int studentId,
            int course1Points,
            int course2Points,
            int course3Points,
            int course4Points
    ) {
        this(0, studentId, course1Points, course2Points, course3Points, course4Points);
    }

    public StudentPointsRecord(
            int studentPointsId,
            int studentId,
            int course1Points,
            int course2Points,
            int course3Points,
            int course4Points
    ) {
        this.studentPointsId = studentPointsId;
        this.studentId = studentId;
        this.course1Points = course1Points;
        this.course2Points = course2Points;
        this.course3Points = course3Points;
        this.course4Points = course4Points;
    }

    public int studentPointsId() {
        return studentPointsId;
    }

    public void setStudentPointsId(int studentPointsId) {
        this.studentPointsId = studentPointsId;
    }

    public int studentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int course1Points() {
        return course1Points;
    }

    public void setCourse1Points(int course1Points) {
        this.course1Points = course1Points;
    }

    public int course2Points() {
        return course2Points;
    }

    public void setCourse2Points(int course2Points) {
        this.course2Points = course2Points;
    }

    public int course3Points() {
        return course3Points;
    }

    public void setCourse3Points(int course3Points) {
        this.course3Points = course3Points;
    }

    public int course4Points() {
        return course4Points;
    }

    public void setCourse4Points(int course4Points) {
        this.course4Points = course4Points;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StudentPointsRecord) obj;
        return this.studentPointsId == that.studentPointsId &&
                this.studentId == that.studentId &&
                this.course1Points == that.course1Points &&
                this.course2Points == that.course2Points &&
                this.course3Points == that.course3Points &&
                this.course4Points == that.course4Points;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentPointsId, studentId, course1Points, course2Points, course3Points, course4Points);
    }

    @Override
    public String toString() {
        return "StudentPointsRecord[" +
                "studentPointsId=" + studentPointsId + ", " +
                "studentId=" + studentId + ", " +
                "course1Points=" + course1Points + ", " +
                "course2Points=" + course2Points + ", " +
                "course3Points=" + course3Points + ", " +
                "course4Points=" + course4Points + ']';
    }
}
