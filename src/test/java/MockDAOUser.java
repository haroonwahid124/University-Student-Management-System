import java.util.*;
import java.util.Date;

public class MockDAOUser extends DAOUser {
    public MockDAOUser() {
    }

    // Mock Constants
    public static final int MODULE_ID_NO_MATERIALS = 200;
    private static final int USER_FAIL_ID = 9999;
    private static final int STUDENT_ID_NO_ENROLLMENT = 1000;
    private static final int MODULE_ID_FAIL = 999;

    // Authentication

    @Override
    public boolean login(String email, char[] password) {
        return email.equals("active@test.com") && new String(password).equals("pass123");
    }

    @Override
    public boolean signup(String email, char[] password, String firstName, String surname, String gender, Date dob, String user_type) {
        return true;
    }

    @Override
    public boolean updatePasswordInDB(String email, char[] oldPassword, char[] newPassword) {
        return true;
    }

    @Override
    public int getLecturerId(int userId) {
        return userId == 200 ? 50 : -1;
    }
    @Override
    public int getStudentId(int userId) {
        return userId == 100 ? 10 : -1;
    }

    // Student

    @Override
    public List<Map<String, Object>> getActiveEnrollments(int studentId) {
        if (studentId == 10) {
            return List.of(Map.of("enrollment_id", 1, "course_name", "BSc Computer Science"));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getModulesByEnrollmentId(int enrollmentId) {
        if (enrollmentId == 1) {
            List<Map<String, Object>> modules = new ArrayList<>();
            modules.add(Map.of("module_id", 10, "code", "MTH100", "name", "Maths", "credits", 20));
            modules.add(Map.of("module_id", 11, "code", "CS101", "name", "Programming", "credits", 20));
            return modules;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getMarksHistory(int studentId) {
        if (studentId == 10) {
            List<Map<String, Object>> marks = new ArrayList<>();
            marks.add(Map.of("code", "CS101", "overall", 75, "status", "passed", "attempt", 1));
            return marks;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getStudentDecisions(int studentId) {
        if (studentId == 10) {
            List<Map<String, Object>> decisions = new ArrayList<>();
            decisions.add(Map.of("decision_id", 1, "type", "award", "manager_id", 100));
            return decisions;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getMaterialsByModuleId(int moduleId) {
        if (moduleId == 101) {
            return List.of(Map.of("material_id", 1, "file_name", "note.pdf"));
        }
        if (moduleId == MODULE_ID_NO_MATERIALS) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getMaterialFileDetails(int materialId) {
        if (materialId == 1) {
            return Map.of("filename", "note.pdf");
        }
        return new HashMap<>();
    }

    // Lecturer

    @Override
    public List<Map<String, Object>> getModulesByLecturerId(int lecturerId) {
        if (lecturerId == 50) {
            List<Map<String, Object>> modules = new ArrayList<>();
            modules.add(Map.of("module_id", 101, "code", "CS101", "name", "Programming", "credits", 20, "description", ""));
            return modules;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getEnrolledStudentsByModuleId(int moduleId) {
        if (moduleId == 101) {
            List<Map<String, Object>> students = new ArrayList<>();
            students.add(Map.of("student_id", 20, "firstName", "Test", "surname", "Student", "email", "test@email.com", "enrollmentDate", new Date()));
            return students;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getModuleMarksHistory(int moduleId) {
        if (moduleId == 101) {
            List<Map<String, Object>> marks = new ArrayList<>();
            marks.add(Map.of("attempt_id", 100, "student_id", 20, "attempt_number", 1, "firstName", "John", "surname", "Doe"));
            return marks;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateModuleInfo(int moduleId, String code, String name, int credit, String description) {
        return moduleId != MODULE_ID_FAIL;
    }

    @Override
    public boolean uploadTeachingMaterial(int moduleId, int lecturerId, String type, int week, String fileName) {
        return true;
    }

    @Override
    public boolean updateOrCreateModuleAttempt(int attemptId, int studentId, int moduleId, int attemptNumber, Integer examMark, Integer labMark, Integer overallMark, String status) {
        return true;
    }


    // Manager

    @Override
    public List<Map<String, Object>> getUsersForManagement(String roleFilter, Boolean isActive) {
        List<Map<String, Object>> users = new ArrayList<>();
        users.add(Map.of("user_id", 100, "name", "Active Student", "email", "active.s@uni.ac.uk", "role", "student", "active", true));
        users.add(Map.of("user_id", 200, "name", "Inactive Lecturer", "email", "inactive.l@uni.ac.uk", "role", "lecturer", "active", false));
        users.add(Map.of("user_id", 201, "name", "Active Lecturer", "email", "active.l@uni.ac.uk", "role", "lecturer", "active", true));


        if (roleFilter != null) {
            users.removeIf(u -> !roleFilter.equalsIgnoreCase((String) u.get("role")));
        }
        if (isActive != null) {
            users.removeIf(u -> !isActive.equals(u.get("active")));
        }
        return users;
    }

    @Override
    public List<Map<String, Object>> getAllLecturers() {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        courses.add(Map.of("course_id", 1, "code", "CSB", "name", "BSc Computer Science", "description", "BSc"));
        return courses;
    }

    @Override
    public boolean addCourse(String code, String name, String description) {
        return true;
    }

    @Override
    public boolean addModule(String code, String name, int credit) {
        return true;
    }

    @Override
    public boolean updateCourseDescription(int courseId, String description) {
        return courseId == 1;
    }

    @Override
    public boolean updateModuleCredit(int moduleId, int credit) {
        return moduleId == 10;
    }

    @Override
    public boolean assignModuleToCourse(int courseId, int moduleId, String semester) {
        return true;
    }

    @Override
    public boolean assignModuleToLecturer(int moduleId, int lecturerId) {
        return true;
    }

    @Override
    public boolean enrolStudentInCourse(int studentId, int courseId) {
        return true;
    }

    @Override
    public boolean unenrolStudentFromCourse(int studentId, int courseId) {
        return true;
    }

    @Override
    public boolean updateUserAccountStatus(int userId, boolean active) {
        return userId != USER_FAIL_ID;
    }

    @Override
    public boolean addBusinessRule(String ruleType, int ruleValue, String scope, Integer targetId) {
        if (scope.equals("course_specific") && targetId == null) {
            return false;
        }
        if (scope.equals("invalid_scope")) {
            return false;
        }
        return true;
    }


    @Override
    public boolean issueStudentDecision(int studentId, int managerId, String decisionType) {
        return true;
    }

}