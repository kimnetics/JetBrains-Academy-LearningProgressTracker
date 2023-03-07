package tracker.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import tracker.data.StudentData;
import tracker.data.StudentRecord;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddStudentHandlerTest {
    private static final AddStudentHandler addStudentHandler = AddStudentHandler.getInstance();
    private static final StudentData studentData = Mockito.mock(StudentData.class);

    @BeforeAll
    static void beforeAll() {
        addStudentHandler.setStudentData(studentData);
    }

    @BeforeEach
    void beforeEach() {
        when(studentData.getStudentByEmail("bob@test.com")).thenReturn(Optional.empty());
    }

    @Test
    void buildStudentRecord_BuildsRecord() {
        var elements = new String[]{"Bob", "Jones", "bob@test.com"};
        Optional<StudentRecord> result = addStudentHandler.buildStudentRecord(elements);
        assertTrue(result.isPresent());

        assertEquals("Bob", result.get().firstName());
        assertEquals("Jones", result.get().lastName());
        assertEquals("bob@test.com", result.get().email());
    }

    @Test
    void buildStudentRecord_RejectsInsufficientElements() {
        var elements = new String[]{"Bob"};
        Optional<StudentRecord> result = addStudentHandler.buildStudentRecord(elements);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildStudentRecord_ProperlySplitsFullName() {
        var elements = new String[]{"Bob", "Jones", "III", "Esquire", "bob@test.com"};
        Optional<StudentRecord> result = addStudentHandler.buildStudentRecord(elements);
        assertTrue(result.isPresent());

        assertEquals("Bob", result.get().firstName());
        assertEquals("Jones III Esquire", result.get().lastName());
        assertEquals("bob@test.com", result.get().email());
    }

    @Test
    void validateStudentRecord_AcceptsValidRecord() {
        var studentRecord = new StudentRecord("Bob", "Jones", "bob@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertTrue(result.isEmpty());
    }

    @Test
    void validateStudentRecord_AcceptsMultipartLastName() {
        var studentRecord = new StudentRecord("Bob", "Jones III", "bob@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertTrue(result.isEmpty());
    }

    @Test
    void validateStudentRecord_RejectsInvalidFirstName() {
        var studentRecord = new StudentRecord("Bob~", "Jones", "bob@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertFalse(result.isEmpty());
    }

    @Test
    void validateStudentRecord_RejectsInvalidLastName() {
        var studentRecord = new StudentRecord("Bob", "Jones~", "bob@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertFalse(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jones~ III", "Jones III~"})
    void validateStudentRecord_RejectsInvalidMultipartLastName(String lastName) {
        var studentRecord = new StudentRecord("Bob", lastName, "bob@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertFalse(result.isEmpty());
    }

    @Test
    void validateStudentRecord_RejectsInvalidEmail() {
        var studentRecord = new StudentRecord("Bob", "Jones", "@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertFalse(result.isEmpty());
    }

    @Test
    void validateStudentRecord_RejectsSameEmail() {
        when(studentData.getStudentByEmail("bob@test.com")).thenReturn(Optional.of(new StudentRecord("Bob", "Jones", "bob@test.com")));
        var studentRecord = new StudentRecord("Robert", "Jones", "bob@test.com");
        List<String> result = addStudentHandler.validateStudentRecord(studentRecord);
        assertFalse(result.isEmpty());
    }

    @Test
    void isNameValid_AcceptsValidCharacters() {
        boolean result = addStudentHandler.isNameValid("AZ-c'az");
        assertTrue(result);
    }

    @Test
    void isNameValid_RejectsInvalidCharacters() {
        boolean result = addStudentHandler.isNameValid("Bob~");
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"'Bob", "-Bob"})
    void isNameValid_RejectsInvalidLeadingCharacters(String name) {
        boolean result = addStudentHandler.isNameValid(name);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bob'", "Bob-"})
    void isNameValid_RejectsInvalidTrailingCharacters(String name) {
        boolean result = addStudentHandler.isNameValid(name);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bo''b", "Bo--b", "Bo'-b", "Bo-'b"})
    void isNameValid_RejectsAdjacentApostrophesAndHyphens(String name) {
        boolean result = addStudentHandler.isNameValid(name);
        assertFalse(result);
    }

    @Test
    void isEmailValid_AcceptsValidEmail() {
        boolean result = addStudentHandler.isEmailValid("bob@test.com");
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"@test.com", "bob@.com", "bob@test."})
    void isEmailValid_RejectsMissingElements(String email) {
        boolean result = addStudentHandler.isEmailValid(email);
        assertFalse(result);
    }
}
