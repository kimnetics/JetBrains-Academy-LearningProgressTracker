package tracker.data;

import java.util.Objects;

public final class StudentRecord {
    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private int course1Points;
    private int course2Points;
    private int course3Points;
    private int course4Points;
    private int course1NotificationStatus;
    private int course2NotificationStatus;
    private int course3NotificationStatus;
    private int course4NotificationStatus;

    public StudentRecord(
            String firstName,
            String lastName,
            String email
    ) {
        this(0, firstName, lastName, email);
    }

    public StudentRecord(
            int studentId,
            String firstName,
            String lastName,
            String email
    ) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.course1Points = 0;
        this.course2Points = 0;
        this.course3Points = 0;
        this.course4Points = 0;
        this.course1NotificationStatus = 0;
        this.course2NotificationStatus = 0;
        this.course3NotificationStatus = 0;
        this.course4NotificationStatus = 0;
    }

    public int studentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String firstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String lastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String email() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int course1NotificationStatus() {
        return course1NotificationStatus;
    }

    public void setCourse1NotificationStatus(int course1NotificationStatus) {
        this.course1NotificationStatus = course1NotificationStatus;
    }

    public int course2NotificationStatus() {
        return course2NotificationStatus;
    }

    public void setCourse2NotificationStatus(int course2NotificationStatus) {
        this.course2NotificationStatus = course2NotificationStatus;
    }

    public int course3NotificationStatus() {
        return course3NotificationStatus;
    }

    public void setCourse3NotificationStatus(int course3NotificationStatus) {
        this.course3NotificationStatus = course3NotificationStatus;
    }

    public int course4NotificationStatus() {
        return course4NotificationStatus;
    }

    public void setCourse4NotificationStatus(int course4NotificationStatus) {
        this.course4NotificationStatus = course4NotificationStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StudentRecord) obj;
        return this.studentId == that.studentId &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.email, that.email) &&
                this.course1Points == that.course1Points &&
                this.course2Points == that.course2Points &&
                this.course3Points == that.course3Points &&
                this.course4Points == that.course4Points &&
                this.course1NotificationStatus == that.course1NotificationStatus &&
                this.course2NotificationStatus == that.course2NotificationStatus &&
                this.course3NotificationStatus == that.course3NotificationStatus &&
                this.course4NotificationStatus == that.course4NotificationStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, firstName, lastName, email, course1Points, course2Points, course3Points, course4Points,
                course1NotificationStatus, course2NotificationStatus, course3NotificationStatus, course4NotificationStatus);
    }

    @Override
    public String toString() {
        return "StudentRecord[" +
                "studentId=" + studentId + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "email=" + email + ", " +
                "course1Points=" + course1Points + ", " +
                "course2Points=" + course2Points + ", " +
                "course3Points=" + course3Points + ", " +
                "course4Points=" + course4Points + ", " +
                "course1NotificationSent=" + course1NotificationStatus + ", " +
                "course2NotificationSent=" + course2NotificationStatus + ", " +
                "course3NotificationSent=" + course3NotificationStatus + ", " +
                "course4NotificationSent=" + course4NotificationStatus + ']';
    }
}
