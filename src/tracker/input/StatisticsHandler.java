package tracker.input;

import tracker.data.StudentPointsData;
import tracker.data.StudentPointsRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class StatisticsHandler {
    private static final StatisticsHandler instance = new StatisticsHandler();

    private StatisticsHandler() {
    }

    public static StatisticsHandler getInstance() {
        return instance;
    }

    private StudentPointsData studentPointsData = StudentPointsData.getInstance();

    /**
     * Set student points data class to use.
     *
     * @param studentPointsData Student points data class to use.
     */
    public void setStudentPointsData(StudentPointsData studentPointsData) {
        this.studentPointsData = studentPointsData;
    }

    enum Command {
        back("Go Back"),
        // Note we are taking advantage of the fact that the enum ordinal value matches
        // course ids. Be careful when adjusting the order of the enums.
        java("Java"),
        dsa("DSA"),
        databases("Databases"),
        spring("Spring");

        public final String label;

        Command(String label) {
            this.label = label;
        }
    }

    /**
     * Do statistics input loop.
     *
     * @param scanner Scanner to use for input.
     */
    public void doStatisticsLoop(Scanner scanner) {
        System.out.println("Type the name of a course to see details or 'back' to quit:");

        // Display overall statistics.
        displayOverallStatistics();

        boolean exitLoop = false;
        do {
            // Enter command.
            Command command = enterCommand(scanner);

            // Handle command.
            switch (command) {
                // Display course statistics.
                case java, dsa, databases, spring -> {
                    displayCourseStatistics(command);
                }
                // Exit statistics.
                case back -> {
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
                System.out.println("Unknown course.");
                continue;
            }

            // We have a known command so exit entry loop,
            break;
        }

        return command;
    }

    /**
     * Display overall statistics.
     */
    void displayOverallStatistics() {
        // Calculate overall statistics.
        List<Set<Integer>> enrollment = new ArrayList<>();
        List<Integer> activity = new ArrayList<>();
        List<List<Integer>> grades = new ArrayList<>();
        calculateOverallStatistics(enrollment, activity, grades);

        // Display overall statistics.

        int highestEnrollmentCount = 0;
        int lowestEnrollmentCount = Integer.MAX_VALUE;
        for (Set<Integer> courseStudentIds : enrollment) {
            int enrollmentCount = courseStudentIds.size();
            if (enrollmentCount > highestEnrollmentCount) highestEnrollmentCount = enrollmentCount;
            if (enrollmentCount < lowestEnrollmentCount) lowestEnrollmentCount = enrollmentCount;
        }

        StringJoiner mostPopular = new StringJoiner(", ");
        StringJoiner leastPopular = new StringJoiner(", ");
        for (int i = 1; i < 5; i++) {
            int enrollmentCount = enrollment.get(i).size();
            if (enrollmentCount > 0) {
                if (enrollmentCount == highestEnrollmentCount) {
                    mostPopular.add(Command.values()[i].label);
                } else if (enrollmentCount == lowestEnrollmentCount) {
                    leastPopular.add(Command.values()[i].label);
                }
            }
        }

        System.out.printf("Most popular: %s%n", mostPopular.toString().isEmpty() ? "n/a" : mostPopular);
        System.out.printf("Least popular: %s%n", leastPopular.toString().isEmpty() ? "n/a" : leastPopular);

        int highestActivityCount = 0;
        int lowestActivityCount = Integer.MAX_VALUE;
        for (Integer count : activity) {
            if (count > highestActivityCount) highestActivityCount = count;
            if (count < lowestActivityCount) lowestActivityCount = count;
        }

        StringJoiner highestActivity = new StringJoiner(", ");
        StringJoiner lowestActivity = new StringJoiner(", ");
        for (int i = 1; i < 5; i++) {
            int count = activity.get(i);
            if (count > 0) {
                if (count == highestActivityCount) {
                    highestActivity.add(Command.values()[i].label);
                } else if (count == lowestActivityCount) {
                    lowestActivity.add(Command.values()[i].label);
                }
            }
        }

        System.out.printf("Highest activity: %s%n", highestActivity.toString().isEmpty() ? "n/a" : highestActivity);
        System.out.printf("Lowest activity: %s%n", lowestActivity.toString().isEmpty() ? "n/a" : lowestActivity);

        double highestAverageGrade = 0.0;
        double lowestAverageGrade = Double.MAX_VALUE;
        for (List<Integer> courseGrades : grades) {
            double averageGrade = courseGrades.stream()
                    .mapToInt(g -> g)
                    .average()
                    .orElse(0.0);
            if (averageGrade > highestAverageGrade) highestAverageGrade = averageGrade;
            if (averageGrade < lowestAverageGrade) lowestAverageGrade = averageGrade;
        }

        StringJoiner easiestCourses = new StringJoiner(", ");
        StringJoiner hardestCourses = new StringJoiner(", ");
        for (int i = 1; i < 5; i++) {
            List<Integer> courseGrades = grades.get(i);
            if (courseGrades.size() > 0) {
                double averageGrade = courseGrades.stream()
                        .mapToInt(g -> g)
                        .average()
                        .orElse(0.0);
                if (averageGrade == highestAverageGrade) {
                    easiestCourses.add(Command.values()[i].label);
                } else if (averageGrade == lowestAverageGrade) {
                    hardestCourses.add(Command.values()[i].label);
                }
            }
        }

        System.out.printf("Easiest course: %s%n", easiestCourses.toString().isEmpty() ? "n/a" : easiestCourses);
        System.out.printf("Hardest course: %s%n", hardestCourses.toString().isEmpty() ? "n/a" : hardestCourses);
    }

    /**
     * Calculate overall statistics.
     *
     * @param enrollment Updated with student points enrollment information.
     * @param activity   Updated with student points activity information.
     * @param grades     Updated with student points grade information.
     */
    void calculateOverallStatistics(List<Set<Integer>> enrollment, List<Integer> activity, List<List<Integer>> grades) {
        // Will contain sets of student ids.
        for (int i = 0; i < 5; i++) {
            enrollment.add(new HashSet<>());
        }

        // Will contain activity counts.
        for (int i = 0; i < 5; i++) {
            activity.add(0);
        }

        // Will contain lists of grades
        for (int i = 0; i < 5; i++) {
            grades.add(new ArrayList<>());
        }

        // Summarize student points records.
        List<StudentPointsRecord> studentPointsRecords = studentPointsData.getStudentPoints();
        for (StudentPointsRecord studentPointsRecord : studentPointsRecords) {
            // Update enrollment.
            if (studentPointsRecord.course1Points() > 0) enrollment.get(1).add(studentPointsRecord.studentId());
            if (studentPointsRecord.course2Points() > 0) enrollment.get(2).add(studentPointsRecord.studentId());
            if (studentPointsRecord.course3Points() > 0) enrollment.get(3).add(studentPointsRecord.studentId());
            if (studentPointsRecord.course4Points() > 0) enrollment.get(4).add(studentPointsRecord.studentId());

            // Update activity.
            if (studentPointsRecord.course1Points() > 0) activity.set(1, activity.get(1) + 1);
            if (studentPointsRecord.course2Points() > 0) activity.set(2, activity.get(2) + 1);
            if (studentPointsRecord.course3Points() > 0) activity.set(3, activity.get(3) + 1);
            if (studentPointsRecord.course4Points() > 0) activity.set(4, activity.get(4) + 1);

            // Update grades.
            if (studentPointsRecord.course1Points() > 0) grades.get(1).add(studentPointsRecord.course1Points());
            if (studentPointsRecord.course2Points() > 0) grades.get(2).add(studentPointsRecord.course2Points());
            if (studentPointsRecord.course3Points() > 0) grades.get(3).add(studentPointsRecord.course3Points());
            if (studentPointsRecord.course4Points() > 0) grades.get(4).add(studentPointsRecord.course4Points());
        }
    }

    /**
     * Display course statistics
     *
     * @param command Command indicating course to display.
     */
    void displayCourseStatistics(Command command) {
        TreeSet<CourseStudent> courseStudents = new TreeSet<>();

        // Calculate course statistics.
        calculateCourseStatistics(command, courseStudents);

        System.out.println(command.label);
        System.out.printf("%-7s %-6s %s%n", "id", "points", "completed");
        courseStudents.forEach(s -> System.out.printf("%-7d %-6d %.1f%%%n", s.studentId, s.points, s.completed));
    }

    /**
     * Calculate course statistics.
     *
     * @param command        Command indicating course to process.
     * @param courseStudents Updated with course student information.
     */
    void calculateCourseStatistics(Command command, TreeSet<CourseStudent> courseStudents) {
        // Set total points possible for each course.
        final BigDecimal[] coursePoints = new BigDecimal[5];
        coursePoints[1] = BigDecimal.valueOf(600);
        coursePoints[2] = BigDecimal.valueOf(400);
        coursePoints[3] = BigDecimal.valueOf(480);
        coursePoints[4] = BigDecimal.valueOf(550);

        // Total student points for course.
        // Student id, Total points.
        HashMap<Integer, Integer> studentPoints = new HashMap<>();
        List<StudentPointsRecord> studentPointsRecords = studentPointsData.getStudentPointsByCourseId(command.ordinal());
        for (StudentPointsRecord studentPointsRecord : studentPointsRecords) {
            int pointsToAdd = switch (command.ordinal()) {
                case 1 -> studentPointsRecord.course1Points();
                case 2 -> studentPointsRecord.course2Points();
                case 3 -> studentPointsRecord.course3Points();
                case 4 -> studentPointsRecord.course4Points();
                default -> throw new IllegalStateException("Unexpected value: " + command.ordinal());
            };

            int studentId = studentPointsRecord.studentId();
            if (studentPoints.containsKey(studentId)) {
                pointsToAdd += studentPoints.get(studentId);
            }
            pointsToAdd = Math.min(pointsToAdd, coursePoints[command.ordinal()].intValue());
            studentPoints.put(studentId, pointsToAdd);
        }

        // Calculate student completion percentage and load output set.
        for (Map.Entry<Integer, Integer> entry : studentPoints.entrySet()) {
            var courseStudent = new CourseStudent(
                    entry.getKey(),
                    entry.getValue(),
                    BigDecimal.valueOf(100L * entry.getValue()).divide(coursePoints[command.ordinal()], RoundingMode.HALF_UP)
            );
            courseStudents.add(courseStudent);
        }
    }

    private class CourseStudent implements Comparable<CourseStudent> {
        public final int studentId;
        public final int points;
        public final BigDecimal completed;

        public CourseStudent(int studentId, int points, BigDecimal completed) {
            this.studentId = studentId;
            this.points = points;
            this.completed = completed;
        }

        @Override
        public int compareTo(StatisticsHandler.CourseStudent that) {
            if (this.points > that.points) {
                return -1;
            } else if (this.points == that.points) {
                return (this.studentId > that.studentId) ? 1 : -1;
            }
            return 1;
        }
    }
}
