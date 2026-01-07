import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ModelUserTest {

    private ModelUser model;
    private MockDAOUser mockDao;

    // Mock Constants
    private static final String TEST_EMAIL = "test@uni.ac.uk";
    private static final int MANAGER_ID = 500;
    private static final int COURSE_ID = 1;
    private static final int MODULE_ID = 10;
    private static final int STUDENT_ID = 100;

    private static final int STUDENT_AWARD = 101;
    private static final int STUDENT_RESIT = 102;
    private static final int STUDENT_WITHDRAW = 103;
    private static final int STUDENT_ID_NO_ENROLLMENT = 1000;
    private static final int USER_FAIL_ID = 9999;
    private static final int MODULE_ID_FAIL = 999;


    @BeforeEach
    void setUp() {
        mockDao = new MockDAOUser();
        model = new ModelUser(mockDao);
    }

    // Authentication

    @Test
    void testLogin_Success() {
        boolean result = model.login("active@test.com", "pass123".toCharArray());
        assertTrue(result, "Successful login should return true.");
    }

    @Test
    void testSignup_Success() {
        boolean result = model.signup(
                "Test", "User", "valid@test.com",
                "pass".toCharArray(), "pass".toCharArray(), "male", new Date(), "student"
        );
        assertTrue(result, "Successful signup (with matching passwords) should return true.");
    }

    @Test
    void testSignup_PasswordMismatchFails() {
        boolean result = model.signup(
                "Test", "User", "mismatch@test.com",
                "passA".toCharArray(), "passB".toCharArray(), "male", new Date(), "student"
        );
        assertFalse(result, "Signup should fail if password and retypePassword do not match.");
    }

    @Test
    void testUpdatePassword_MismatchedNewPasswordsFails() {
        char[] newPwd1 = "newpasswordA".toCharArray();
        char[] newPwd2 = "newpasswordB".toCharArray();
        boolean result = model.updatePassword(TEST_EMAIL, "old".toCharArray(), newPwd1, newPwd2);
        assertFalse(result, "Password update should fail if new passwords do not match.");
    }

    @Test
    void testUpdatePassword_EmptyEmailFails() {
        boolean result = model.updatePassword("", "old".toCharArray(), "new".toCharArray(), "new".toCharArray());
        assertFalse(result, "Password update should fail if the email is empty.");
    }

    // Student tests

    @Test
    void testLoadActiveEnrollments_Success() {
        List<Map<String, Object>> enrollments = model.loadActiveEnrollments(10);
        assertEquals(1, enrollments.size(), "Should find one active enrollment.");
    }

    @Test
    void testLoadActiveEnrollments_NoActiveEnrollment() {
        List<Map<String, Object>> enrollments = model.loadActiveEnrollments(STUDENT_ID_NO_ENROLLMENT);
        assertTrue(enrollments.isEmpty(), "Should return an empty list if student has no active enrollment.");
    }

    @Test
    void testLoadModulesForEnrollment_Success() {
        List<Map<String, Object>> modules = model.loadModulesForEnrollment(1);
        assertEquals(2, modules.size(), "Should return 2 modules for the enrollment.");
    }

    @Test
    void testLoadMarksHistory_Success() {
        List<Map<String, Object>> marks = model.loadMarksHistory(10);
        assertEquals(1, marks.size(), "Should return 1 mark record.");
    }

    @Test
    void testLoadMarksHistory_NoMarksReturnsEmptyList() {
        List<Map<String, Object>> marks = model.loadMarksHistory(STUDENT_ID_NO_ENROLLMENT);
        assertTrue(marks.isEmpty(), "Should return an empty list if student has no marks history.");
    }

    @Test
    void testLoadStudentDecisions_Success() {
        List<Map<String, Object>> decisions = model.loadStudentDecisions(10);
        assertEquals(1, decisions.size(), "Should return 1 decision record.");
    }

    @Test
    void testLoadStudentDecisions_NoDecisionReturnsEmptyList() {
        List<Map<String, Object>> decisions = model.loadStudentDecisions(STUDENT_ID_NO_ENROLLMENT);
        assertTrue(decisions.isEmpty(), "Should return an empty list if student has no recorded decisions.");
    }

    @Test
    void testLoadMaterialsByModuleId_EmptyList() {
        List<Map<String, Object>> materials = model.loadMaterialsByModuleId(MockDAOUser.MODULE_ID_NO_MATERIALS);
        assertTrue(materials.isEmpty(), "Should return an empty list if no materials exist for the module.");
    }

    // Lecturer tests

    @Test
    void testLoadModulesByLecturer_Success() {
        List<Map<String, Object>> modules = model.loadModulesByLecturer(50);
        assertEquals(1, modules.size(), "Should return 1 assigned module.");
    }

    @Test
    void testLoadModuleMarks_ReturnsStudentAttempt() {
        List<Map<String, Object>> marks = model.loadModuleMarks(101);
        assertEquals(1, marks.size(), "Should return 1 student attempt for the module.");
    }

    @Test
    void testUpdateModuleDetails_ZeroCreditFails() {
        boolean result = model.updateModuleDetails(MODULE_ID, "CS101", "Intro CS", 0, "Desc");
        assertFalse(result, "Module update should fail if credit points are zero or less.");
    }

    @Test
    void testUpdateModuleDetails_EmptyCodeFails() {
        boolean result = model.updateModuleDetails(MODULE_ID, "", "Intro CS", 20, "Desc");
        assertFalse(result, "Module update should fail if the Module Code is empty.");
    }

    @Test
    void testUpdateModuleDetails_DatabaseFailure() {
        boolean result = model.updateModuleDetails(MODULE_ID_FAIL, "CS101", "Intro CS", 20, "Desc");
        assertFalse(result, "Update should fail when DAO mocks a database error.");
    }

    @Test
    void testSaveModuleAttempt_MarkNegativeFails() {
        boolean result = model.saveModuleAttempt(-1, STUDENT_ID, MODULE_ID, 1, 50, -1, 49, "failed");
        assertFalse(result, "Saving marks should fail if any mark is less than 0.");
    }

    @Test
    void testSaveModuleAttempt_MarkOutOfBoundsFails() {
        boolean result = model.saveModuleAttempt(-1, STUDENT_ID, MODULE_ID, 1, 101, 50, 151, "passed");
        assertFalse(result, "Saving marks should fail if any mark is outside the 0-100 range.");
    }

    @Test
    void testUploadMaterial_InvalidWeekFails() {
        boolean result = model.uploadMaterial(MODULE_ID, 50, "lecture_note", 0, "notes.pdf");
        assertFalse(result, "Material upload should fail if the week number is zero or less.");
    }

    // Manager tests


    @Test
    void testLoadUsersForManagement_NoFilter() {
        List<Map<String, Object>> users = model.loadUsersForManagement(null, null);
        assertEquals(3, users.size(), "Should return all 3 mock users without filtering.");
    }

    @Test
    void testLoadUsersForManagement_FilterByRole() {
        List<Map<String, Object>> users = model.loadUsersForManagement("student", null);
        assertEquals(1, users.size(), "Should return exactly one student after filtering.");
    }

    @Test
    void testLoadUsersForManagement_CombinedFilterActiveLecturer() {
        List<Map<String, Object>> users = model.loadUsersForManagement("lecturer", true);
        assertEquals(1, users.size(), "Should return exactly one active lecturer after filtering.");
        assertTrue((Boolean) users.get(0).get("active"));
    }

    @Test
    void testUpdateUserAccountStatus_DeactivateSuccess() {
        boolean result = model.updateUserAccountStatus(100, false);
        assertTrue(result, "Deactivating a user account should succeed.");
    }

    @Test
    void testUpdateUserAccountStatus_Failure() {
        boolean result = model.updateUserAccountStatus(USER_FAIL_ID, true);
        assertFalse(result, "Update should fail when DAO mocks a database error (USER_FAIL_ID).");
    }

    @Test
    void testLoadAllLecturers_ReturnsEmptyList() {
        List<Map<String, Object>> lecturers = model.loadAllLecturers();
        assertTrue(lecturers.isEmpty(), "Should return an empty list when the DAO finds no lecturers.");
    }

    @Test
    void testAddCourse_MissingCodeFails() {
        boolean result = model.addCourse("", "New Course Name", "Description");
        assertFalse(result, "Adding a course with an empty code should fail Model validation.");
    }

    @Test
    void testAddModule_NegativeCreditFails() {
        boolean result = model.addModule("MOD202", "Test Module", 0);
        assertFalse(result, "Adding a module with non-positive credit should fail Model validation.");
    }

    @Test
    void testUpdateCourseDescription_Success() {
        boolean result = model.updateCourseDescription(COURSE_ID, "New and improved description");
        assertTrue(result, "Updating course description should succeed.");
    }

    @Test
    void testUpdateModuleCredit_Success() {
        boolean result = model.updateModuleCredit(MODULE_ID, 30);
        assertTrue(result, "Updating module credit should succeed.");
    }

    @Test
    void testAssignModuleToCourse_Success() {
        boolean result = model.assignModuleToCourse(COURSE_ID, MODULE_ID, "1");
        assertTrue(result, "Assigning a module to a course should succeed.");
    }

    @Test
    void testAssignModuleToCourse_UpdateSemesterSuccess() {
        boolean result = model.assignModuleToCourse(COURSE_ID, MODULE_ID, "2");
        assertTrue(result, "Updating the semester for an existing module assignment should succeed.");
    }

    @Test
    void testAssignModuleToCourse_InvalidSemesterFails() {
        boolean result = model.assignModuleToCourse(COURSE_ID, MODULE_ID, "3");
        assertFalse(result, "Assigning a module should fail if the semester is not '1' or '2'.");
    }

    @Test
    void testAssignModuleToLecturer_Success() {
        boolean result = model.assignModuleToLecturer(MODULE_ID, 50);
        assertTrue(result, "Assigning a module to a lecturer should succeed.");
    }

    @Test
    void testEnrolStudentInCourse_Success() {
        boolean result = model.enrolStudentInCourse(STUDENT_ID, COURSE_ID);
        assertTrue(result, "Enrolling a student in a course should succeed.");
    }

    @Test
    void testUnenrolStudentFromCourse_Success() {
        boolean result = model.unenrolStudentFromCourse(STUDENT_ID, COURSE_ID);
        assertTrue(result, "Unenrolling a student from a course (setting status to withdrawn) should succeed.");
    }

    // Business Rule tests

    @Test
    void testAddBusinessRule_GlobalSuccess() {
        boolean result = model.addBusinessRule("MAX_ATTEMPTS", 3, "global", null);
        assertTrue(result, "Adding a global business rule should succeed.");
    }

    @Test
    void testAddBusinessRule_TargetedFailureMissingTargetID() {
        boolean result = model.addBusinessRule("COMP_MODULES", 2, "course_specific", null);
        assertFalse(result, "Adding a targeted rule without a valid target ID should fail Model validation.");
    }

    @Test
    void testAddBusinessRule_NegativeValueFails() {
        boolean result = model.addBusinessRule("MAX_ATTEMPTS", -1, "global", null);
        assertFalse(result, "Adding a rule with a negative value should fail Model validation.");
    }

    @Test
    void testAddBusinessRule_InvalidScopeFails() {
        boolean result = model.addBusinessRule("MAX_ATTEMPTS", 2, "invalid_scope", null);
        assertFalse(result, "Adding a rule with an invalid scope should fail Model validation.");
    }
}