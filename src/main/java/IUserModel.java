import java.util.*;

public interface IUserModel {
    boolean login(String email, char[] password);
    Map<String, Object> getUserDetails(String email);
    boolean signup(String firstName, String surname, String email, char[] password, char[] retypePassword, String gender, Date dob, String user_type);
    List<String> getPendingUsers(String userTypeFilter);
    boolean approveUser(String email);
    boolean denyUser(String email);
    boolean updatePassword(String email, char[] oldPwd, char[] newPwd, char[] confirmPwd);
    int getStudentId(int userId);
    List<Map<String, Object>> loadActiveEnrollments(int studentId);
    List<Map<String, Object>> loadModulesForEnrollment(int enrollmentId);
    // Retrieves list of modules linked to a student (for the ComboBox)
    List<Map<String, Object>> loadStudentModules(int studentId);
    // Retrieves materials for a selected module
    List<Map<String, Object>> loadMaterialsByModuleId(int moduleId);
    // View Marks Data Access
    List<Map<String, Object>> loadMarksHistory(int studentId);
    // View Student Decisions Data Access
    List<Map<String, Object>> loadStudentDecisions(int studentId);
    Map<String, Object> getFileDetails(int materialId);
    // Lecturer specific methods
    int getLecturerId(int userId);
    List<Map<String, Object>> loadModulesByLecturer(int lecturerId);
    boolean updateModuleDetails(int moduleId, String code, String name, int credit, String description);
    // Lecturer Upload Material Method
    boolean uploadMaterial(int moduleId, int lecturerId, String type, int week, String fileName);
    // Lecturer View Enrolled Students Method
    List<Map<String, Object>> loadEnrolledStudentsByModule(int moduleId);
    // Marks Management Methods
    List<Map<String, Object>> loadModuleMarks(int moduleId);
    boolean saveModuleAttempt(int attemptId, int studentId, int moduleId, int attemptNumber, Integer examMark, Integer labMark, Integer overallMark, String status);
    // Manager: Manage Accounts
    List<Map<String, Object>> loadUsersForManagement(String roleFilter, Boolean isActive);
    boolean updateUserAccountStatus(int userId, boolean active);
    boolean resetUserPassword(int userId, String newPassword);
    // Manager: Assign Module to Lecturer
    List<Map<String, Object>> loadAllLecturers();
    List<Map<String, Object>> loadAllModules();
    List<Map<String, Object>> loadModuleAssignments();
    boolean assignModuleToLecturer(int moduleId, int lecturerId);
    // Student Decision
    List<Map<String, Object>> loadAllStudents();
    boolean issueStudentDecision(int studentId, int managerId, String decision);
    List<Map<String, Object>> loadAllStudentDecisions();

    // Manager: Enrol Students in Course
    List<Map<String, Object>> loadAllCourses();
    List<Map<String, Object>> loadAvailableStudentsForCourse(int courseId);
    List<Map<String, Object>> loadEnrolledStudentsForCourse(int courseId);
    boolean enrolStudentInCourse(int studentId, int courseId);
    boolean unenrolStudentFromCourse(int studentId, int courseId);
    // Manager: Display Module Details
    List<Map<String, Object>> loadAllModuleDetails(String searchTerm);
    // Manager: Update Course Info (description + module credits)
    List<Map<String, Object>> loadModulesByCourse(int courseId);
    boolean updateCourseDescription(int courseId, String description);
    boolean updateModuleCredit(int moduleId, int credit);
    // Manager: Assign Module to Course
    List<Map<String, Object>> loadCourseModuleAssignments();
    boolean assignModuleToCourse(int courseId, int moduleId, String semester);
    // Manager: Add Course
    boolean addCourse(String code, String name, String description);
    boolean addModule(String code, String name, int credit);
    // Manager: Display Course Details
    Map<String, Object> loadCourseDetails(int courseId);
    // Manager: Business Rules
    List<Map<String, Object>> loadAllRules();
    boolean addBusinessRule(String ruleType, int ruleValue, String scope, Integer targetId);
}
