package tracker.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import tracker.data.StudentData;
import tracker.data.StudentPointsData;
import tracker.data.StudentPointsRecord;
import tracker.data.StudentRecord;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddPointsHandlerTest {
    private static final AddPointsHandler addPointsHandler = AddPointsHandler.getInstance();
    private static final StudentData studentData = Mockito.mock(StudentData.class);
    private static final StudentPointsData studentPointsData = Mockito.mock(StudentPointsData.class);

    @BeforeAll
    static void beforeAll() {
        addPointsHandler.setStudentData(studentData);
        addPointsHandler.setStudentPointsData(studentPointsData);
    }

    @Test
    void buildStudentPointsRecord_BuildsRecord() {
        var elements = new String[]{"10000", "1", "2", "3", "4"};
        Optional<StudentPointsRecord> result = addPointsHandler.buildStudentPointsRecord(elements);
        assertTrue(result.isPresent());

        assertEquals(10000, result.get().studentId());
        assertEquals(1, result.get().course1Points());
        assertEquals(2, result.get().course2Points());
        assertEquals(3, result.get().course3Points());
        assertEquals(4, result.get().course4Points());
    }

    @ParameterizedTest
    @MethodSource("rejectsIncorrectNumberOfElementsProvider")
    void buildStudentRecord_RejectsIncorrectNumberOfElements(String[] elements) {
        Optional<StudentPointsRecord> result = addPointsHandler.buildStudentPointsRecord(elements);
        assertTrue(result.isEmpty());
    }

    static Stream<Arguments> rejectsIncorrectNumberOfElementsProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"10000", "1", "2", "3"}),
                Arguments.of((Object) new String[]{"10000", "1", "2", "3", "4", "5"})
        );
    }

    @Test
    void buildStudentRecord_RejectsNonNumericPoints() {
        var elements = new String[]{"10000", "1", "2", "3", "Four"};
        Optional<StudentPointsRecord> result = addPointsHandler.buildStudentPointsRecord(elements);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildStudentRecord_RejectsNegativePoints() {
        var elements = new String[]{"10000", "1", "2", "3", "-4"};
        Optional<StudentPointsRecord> result = addPointsHandler.buildStudentPointsRecord(elements);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildStudentRecord_RejectsTooLargePoints() {
        var elements = new String[]{"10000", "1", "2", "3", "4000"};
        Optional<StudentPointsRecord> result = addPointsHandler.buildStudentPointsRecord(elements);
        assertTrue(result.isEmpty());
    }

    @Test
    void validateStudentPointsRecord_AcceptsValidRecord() {
        when(studentData.getStudent(10000)).thenReturn(Optional.of(new StudentRecord(10000, "Bob", "Jones", "bob@test.com")));
        var studentPointsRecord = new StudentPointsRecord(10000, 1, 2, 3, 4);
        List<String> result = addPointsHandler.validateStudentPointsRecord(studentPointsRecord);
        assertTrue(result.isEmpty());
    }

    @Test
    void validateStudentPointsRecord_RejectsStudentIdNotFound() {
        when(studentData.getStudent(10000)).thenReturn(Optional.empty());
        var studentPointsRecord = new StudentPointsRecord(10000, 1, 2, 3, 4);
        List<String> result = addPointsHandler.validateStudentPointsRecord(studentPointsRecord);
        assertFalse(result.isEmpty());
    }
}
