import java.util.*;
import java.sql.*;
import java.util.Date;

public class ModelUser implements IUserModel {
    private String email;
    private char[] password;
    private DAOUser daoUser;


    public ModelUser() {
        this.daoUser = new DAOUser(); // Initialise the DAOUser instance
    }

    // for tests
    public ModelUser(DAOUser daoUser) {
        this.daoUser = daoUser;
    }

    // Method to check login credentials
    public boolean login(String email, char[] password) {
        // Use the DAOUser to check credentials against the database
        return daoUser.login(email, password);
    }

    // Method to retrieve user type and ID after a successful login
    public Map<String, Object> getUserDetails(String email) {
        return daoUser.getUserDetails(email);
    }

    // Method to sign up a user
    public boolean signup(String firstName, String surname, String email, char[] password, char[] retypePassword, String gender, Date dob, String user_type) {
        if (Arrays.equals(password, retypePassword)) {
            return daoUser.signup(email, password, firstName, surname, gender, dob, user_type);
        }
        return false;
    }

    // Admin-only operations
    public List<String> getPendingUsers(String userTypeFilter){
        return daoUser.getPendingUsers(userTypeFilter);
    }

    public boolean approveUser(String email) {
        return daoUser.approveUser(email);
    }

    public boolean denyUser(String email) {
        return daoUser.denyUser(email);
    }

    // Update password section
    public boolean updatePassword(String email, char[] oldPwd, char[] newPwd, char[] confirmPwd) {

        if (email == null || email.isEmpty()) return false;
        if (!java.util.Arrays.equals(newPwd, confirmPwd)) return false;
        boolean success = daoUser.updatePasswordInDB(email, oldPwd, newPwd);

        return success;
    }

    // Course/Module Methods

    @Override
    public int getStudentId(int userId) {
        return daoUser.getStudentId(userId);
    }

    @Override
    public List<Map<String, Object>> loadActiveEnrollments(int studentId) {
        return daoUser.getActiveEnrollments(studentId);
    }

    @Override
    public List<Map<String, Object>> loadModulesForEnrollment(int enrollmentId) {
        return daoUser.getModulesByEnrollmentId(enrollmentId);
    }

    @Override
    public List<Map<String, Object>> loadStudentModules(int studentId) {
        return daoUser.getStudentModules(studentId);
    }

    @Override
    public List<Map<String, Object>> loadMaterialsByModuleId(int moduleId) {
        return daoUser.getMaterialsByModuleId(moduleId);
    }

    @Override
    public Map<String, Object> getFileDetails(int materialId) {
        return daoUser.getMaterialFileDetails(materialId);
    }

    @Override
    public List<Map<String, Object>> loadStudentDecisions(int studentId) {
        return daoUser.getStudentDecisions(studentId);
    }

    @Override
    public List<Map<String, Object>> loadMarksHistory(int studentId) {
        return daoUser.getMarksHistory(studentId);
    }

    @Override
    public int getLecturerId(int userId) {
        return daoUser.getLecturerId(userId);
    }

    @Override

    public List<Map<String, Object>> loadModulesByLecturer(int lecturerId) {
        return daoUser.getModulesByLecturerId(lecturerId);
    }

    @Override
    public boolean updateModuleDetails(int moduleId, String code, String name, int credit, String description) {
        if (credit <= 0 || code.trim().isEmpty() || name.trim().isEmpty()) {
            return false;
        }
        return daoUser.updateModuleInfo(moduleId, code, name, credit, description);
    }

    @Override
    public boolean uploadMaterial(int moduleId, int lecturerId, String type, int week, String fileName) {
        if (fileName.isEmpty() || week <= 0) {
            return false;
        }
        return daoUser.uploadTeachingMaterial(moduleId, lecturerId, type, week, fileName);
    }

    @Override
    public List<Map<String, Object>> loadEnrolledStudentsByModule(int moduleId) {
        return daoUser.getEnrolledStudentsByModuleId(moduleId);
    }

    @Override
    public List<Map<String, Object>> loadModuleMarks(int moduleId) {
        return daoUser.getModuleMarksHistory(moduleId);
    }


    // Manage Accounts
    @Override
    public List<Map<String, Object>> loadUsersForManagement(String roleFilter, Boolean isActive) {
        return daoUser.getUsersForManagement(roleFilter, isActive);
    }

    @Override
    public boolean updateUserAccountStatus(int userId, boolean active) {
        return daoUser.updateUserAccountStatus(userId, active);
    }

    @Override
    public boolean resetUserPassword(int userId, String newPassword) {
        return daoUser.resetUserPassword(userId, newPassword);
    }


    // Manager: Assign Module to Lecturer
    @Override
    public List<Map<String, Object>> loadAllLecturers() {
        return daoUser.getAllLecturers();
    }

    @Override
    public List<Map<String, Object>> loadAllModules() {
        return daoUser.getAllModules();
    }

    @Override
    public List<Map<String, Object>> loadModuleAssignments() {
        return daoUser.getModuleAssignments();
    }

    @Override
    public boolean assignModuleToLecturer(int moduleId, int lecturerId) {
        return daoUser.assignModuleToLecturer(moduleId, lecturerId);
    }

    @Override
    public List<Map<String, Object>> loadAllStudents() {
        return daoUser.getAllStudents();
    }

    @Override
    public boolean issueStudentDecision(int studentId, int managerId, String decision) {
        if (studentId <= 0) return false;
        if (decision == null) return false;

        String d = decision.trim().toLowerCase();
        if (!d.equals("award") && !d.equals("resit") && !d.equals("withdraw")) {
            return false;
        }

        return daoUser.issueStudentDecision(studentId, managerId, d);
    }


    @Override
    public List<Map<String, Object>> loadAllStudentDecisions() {
        return daoUser.getStudentDecisions();
    }

    // Enrol Students in Course

    @Override
    public List<Map<String, Object>> loadAllCourses() {
        return daoUser.getAllCourses();
    }

    @Override
    public List<Map<String, Object>> loadAvailableStudentsForCourse(int courseId) {
        return daoUser.getAvailableStudentsForCourse(courseId);
    }

    @Override
    public List<Map<String, Object>> loadEnrolledStudentsForCourse(int courseId) {
        return daoUser.getEnrolledStudentsForCourse(courseId);
    }

    @Override
    public boolean enrolStudentInCourse(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) return false;
        return daoUser.enrolStudentInCourse(studentId, courseId);
    }

    @Override
    public boolean unenrolStudentFromCourse(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) return false;
        return daoUser.unenrolStudentFromCourse(studentId, courseId);
    }

    // Manager: Display Module Details
    @Override
    public List<Map<String, Object>> loadAllModuleDetails(String searchTerm) {
        return daoUser.getModuleDetails(searchTerm);
    }

    @Override
    public List<Map<String, Object>> loadModulesByCourse(int courseId) {
        return daoUser.getModulesByCourseId(courseId);
    }

    @Override
    public boolean updateCourseDescription(int courseId, String description) {
        return daoUser.updateCourseDescription(courseId, description);
    }

    @Override
    public boolean updateModuleCredit(int moduleId, int credit) {
        return daoUser.updateModuleCredit(moduleId, credit);
    }

    @Override
    public boolean addCourse(String code, String name, String description) {
        if (code == null || code.trim().isEmpty()) return false;
        if (name == null || name.trim().isEmpty()) return false;

        String cleanCode = code.trim();
        String cleanName = name.trim();
        String desc = (description != null) ? description.trim() : "";

        return daoUser.addCourse(cleanCode, cleanName, desc);
    }

    // Display Course Details
    @Override
    public Map<String, Object> loadCourseDetails(int courseId) {
        if (courseId <= 0) return Collections.emptyMap();
        return daoUser.getCourseDetails(courseId);
    }

    // Assign Module to Course
    @Override
    public List<Map<String, Object>> loadCourseModuleAssignments() {
        return daoUser.getCourseModuleAssignments();
    }

    @Override
    public boolean assignModuleToCourse(int courseId, int moduleId, String semester) {
        if (courseId <= 0 || moduleId <= 0 || semester == null || semester.trim().isEmpty()) {
            return false;
        }
        String cleanSemester = semester.trim();
        if (!cleanSemester.equals("1") && !cleanSemester.equals("2")) {
            return false; // Basic ENUM validation
        }
        return daoUser.assignModuleToCourse(courseId, moduleId, cleanSemester);
    }

    // Business Rules

    @Override
    public List<Map<String, Object>> loadAllRules() {
        return daoUser.getAllRules();
    }

    @Override
    public boolean addBusinessRule(String ruleType, int ruleValue, String scope, Integer targetId) {
        if (ruleType == null || ruleType.trim().isEmpty() || scope == null || scope.trim().isEmpty()) {
            return false;
        }
        if (ruleValue < 0) {
            return false;
        }

        String cleanScope = scope.trim().toLowerCase();

        if (cleanScope.equals("course_specific") || cleanScope.equals("module_specific")) {
            if (targetId == null || targetId <= 0) {
                return false;
            }
        } else if (cleanScope.equals("global")) {
            targetId = null;
        } else {
            return false;
        }

        return daoUser.addBusinessRule(ruleType, ruleValue, cleanScope, targetId);
    }

    @Override
    public boolean addModule(String code, String name, int credit) {
        if (code == null || code.trim().isEmpty()) return false;
        if (name == null || name.trim().isEmpty()) return false;
        if (credit <= 0) return false;

        String cleanCode = code.trim();
        String cleanName = name.trim();

        return daoUser.addModule(cleanCode, cleanName, credit);
    }



    private boolean isMarkValid(Integer mark) {
        return mark == null || (mark >= 0 && mark <= 100);
    }

    @Override
    public boolean saveModuleAttempt(int attemptId, int studentId, int moduleId, int attemptNumber, Integer examMark, Integer labMark, Integer overallMark, String status) {
        if (studentId <= 0 || moduleId <= 0 || attemptNumber <= 0) return false;
        if (!isMarkValid(examMark) || !isMarkValid(labMark) || !isMarkValid(overallMark)) {
            return false;
        }

        return daoUser.updateOrCreateModuleAttempt(attemptId, studentId, moduleId, attemptNumber, examMark, labMark, overallMark, status);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public char[] getPassword() { return password; }
    public void setPassword(char[] password) { this.password = password; }
}