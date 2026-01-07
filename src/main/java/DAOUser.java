import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DAOUser {
    private Connection connection;

    // Hardcoded Admin Credentials for Testing
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private static final String ADMIN_USER_TYPE = "manager";
    private static final int ADMIN_USER_ID = 0;

    public DAOUser() {
        this.connection = MySQLConnect.getMysqlConnection();
    }

    public boolean signup(String email, char[] password, String firstName, String surname, String gender, Date dob, String user_type) {
        String insertUserSql = "INSERT INTO `user` (`email`, `password`, `first_name`, `surname`, `gender`, `dob` , `user_type`, `account_status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int userId = -1;

        try (PreparedStatement userStmt = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            userStmt.setString(1, email);
            userStmt.setString(2, new String(password));
            userStmt.setString(3, firstName);
            userStmt.setString(4, surname);
            userStmt.setString(5, gender.toLowerCase());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            userStmt.setString(6, sdf.format(dob));
            userStmt.setString(7, user_type.toLowerCase());
            userStmt.setString(8, "pending");

            int rowsInserted = userStmt.executeUpdate();

            if (rowsInserted == 0) {
                connection.rollback();
                return false;
            }

            try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // Insert into the corresponding role table
            String roleTableSql = "";
            boolean isLecturer = "lecturer".equalsIgnoreCase(user_type);

            switch (user_type.toLowerCase()) {
                case "student":
                    roleTableSql = "INSERT INTO `student` (`student_id`, `user_id`) VALUES (?, ?)";
                    break;
                case "lecturer":
                    roleTableSql = "INSERT INTO `lecturer` (`lecturer_id`, `user_id`, `department_id`, `qualification`) VALUES (?, ?, ?, ?)";
                    break;
                case "manager":
                    roleTableSql = "INSERT INTO `manager` (`manager_id`, `user_id`) VALUES (?, ?)";
                    break;
                default:
                    connection.rollback();
                    return false;
            }

            try (PreparedStatement roleStmt = connection.prepareStatement(roleTableSql)) {
                roleStmt.setInt(1, userId); // Use user_id for the role_id PK
                roleStmt.setInt(2, userId); // Link to user table FK

                if (isLecturer) {
                    roleStmt.setNull(3, java.sql.Types.INTEGER);
                    roleStmt.setString(4, "Other");
                }

                int roleRowsInserted = roleStmt.executeUpdate();
                if (roleRowsInserted == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit(); // Commit transaction if all steps succeed
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean login(String email, char[] password) {
        if (ADMIN_EMAIL.equalsIgnoreCase(email) && ADMIN_PASSWORD.equals(new String(password))) {
            return true;
        }

        String sql = "SELECT `user_id` FROM `user` WHERE email = ? AND password = ? AND `account_status` = 'active'";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, new String(password));

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getUserDetails(String email) {
        if (ADMIN_EMAIL.equalsIgnoreCase(email)) {
            Map<String, Object> adminDetails = new HashMap<>();
            adminDetails.put("user_id", ADMIN_USER_ID);
            adminDetails.put("first_name", "Admin");
            adminDetails.put("user_type", ADMIN_USER_TYPE);
            adminDetails.put("manager_id", ADMIN_USER_ID);
            return adminDetails;
        }

        String sql = "SELECT u.`user_id`, u.`first_name`, u.`user_type`, " +
                "s.`student_id`, l.`lecturer_id`, m.`manager_id` " +
                "FROM `user` u " +
                "LEFT JOIN `student` s ON u.`user_id` = s.`user_id` " +
                "LEFT JOIN `lecturer` l ON u.`user_id` = l.`user_id` " +
                "LEFT JOIN `manager` m ON u.`user_id` = m.`user_id` " +
                "WHERE u.email = ?";
        Map<String, Object> details = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                details.put("user_id", resultSet.getInt("user_id"));
                details.put("first_name", resultSet.getString("first_name"));
                details.put("user_type", resultSet.getString("user_type"));

                // Collect all role-specific IDs regardless of type
                details.put("student_id", resultSet.getInt("student_id"));
                details.put("lecturer_id", resultSet.getInt("lecturer_id"));
                details.put("manager_id", resultSet.getInt("manager_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }


    public List<String> getPendingUsers(String userTypeFilter) {
        List<String> pending = new ArrayList<>();

        String selectClause = "SELECT email FROM user";
        String whereClause = "WHERE account_status = 'pending'";

        if (userTypeFilter != null && !userTypeFilter.isEmpty()) {
            whereClause += " AND user_type = ?";
        }

        String sql = selectClause + " " + whereClause;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            if (userTypeFilter != null && !userTypeFilter.isEmpty()) {
                statement.setString(paramIndex++, userTypeFilter.toLowerCase());
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    pending.add(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pending;
    }

    public boolean approveUser(String email) {
        String sql = "UPDATE user SET account_status = 'active' WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean denyUser(String email) {
        String selectUserIdSql = "SELECT user_id, user_type FROM user WHERE email = ?";
        String deleteUserSql = "DELETE FROM user WHERE email = ?";
        int userId = -1;
        String userType = null;

        try {
            connection.setAutoCommit(false); // Start transaction
            try (PreparedStatement selectStmt = connection.prepareStatement(selectUserIdSql)) {
                selectStmt.setString(1, email);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        return false; // User not found
                    }
                    userId = rs.getInt("user_id");
                    userType = rs.getString("user_type");
                }
            }
            String deleteRoleSql = null;
            if (userType != null) {
                switch (userType.toLowerCase()) {
                    case "student":
                        deleteRoleSql = "DELETE FROM student WHERE user_id = ?";
                        break;
                    case "lecturer":
                        deleteRoleSql = "DELETE FROM lecturer WHERE user_id = ?";
                        break;
                    case "manager":
                        deleteRoleSql = "DELETE FROM manager WHERE user_id = ?";
                        break;
                }
            }
            if (deleteRoleSql != null) {
                try (PreparedStatement deleteRoleStmt = connection.prepareStatement(deleteRoleSql)) {
                    deleteRoleStmt.setInt(1, userId);
                    deleteRoleStmt.executeUpdate();

                }
            }
            // delete from parent table
            try (PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserSql)) {
                deleteUserStmt.setString(1, email);
                int rowsAffected = deleteUserStmt.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit(); // Commit if parent row is successfully deleted
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restore default
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePasswordInDB(String email, char[] oldPassword, char[] newPassword) {
        // Check if the old password is correct and the user is active
        String checkSql = "SELECT `user_id` FROM `user` WHERE email = ? AND password = ? AND `account_status` = 'active'";
        // update the password
        String updateSql = "UPDATE `user` SET `password` = ? WHERE email = ? AND password = ?";

        try {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                checkStmt.setString(2, new String(oldPassword));

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        // Old password or email incorrect, or account is not active
                        return false;
                    }
                }
            }

            // Update the password
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setString(1, new String(newPassword));
                updateStmt.setString(2, email);
                updateStmt.setString(3, new String(oldPassword)); // Use old password again for verification

                int rowsUpdated = updateStmt.executeUpdate();
                return rowsUpdated > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getStudentId(int userId) {
        String sql = "SELECT student_id FROM student WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("student_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Map<String, Object>> getActiveEnrollments(int studentId) {
        List<Map<String, Object>> enrollments = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, c.course_name " +
                "FROM enrollment e JOIN course c ON e.course_id = c.course_id " +
                "WHERE e.student_id = ? AND e.enrollment_status = 'active' LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("enrollment_id", rs.getInt("enrollment_id"));
                    map.put("course_name", rs.getString("course_name"));
                    enrollments.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public List<Map<String, Object>> getModulesByEnrollmentId(int enrollmentId) {
        List<Map<String, Object>> modules = new ArrayList<>();
        String sql = "SELECT m.module_code, m.module_name, m.credit, m.module_id " +
                "FROM module m " +
                "JOIN course_module cm ON m.module_id = cm.module_id " +
                "JOIN course c ON cm.course_id = c.course_id " +
                "JOIN enrollment e ON e.course_id = c.course_id " +
                "WHERE e.enrollment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("module_id", rs.getInt("module_id"));
                    map.put("code", rs.getString("module_code"));
                    map.put("name", rs.getString("module_name"));
                    map.put("credits", rs.getInt("credit"));
                    modules.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading modules: " + e.getMessage());
            e.printStackTrace();
        }
        return modules;
    }


    public List<Map<String, Object>> getStudentModules(int studentId) {
        List<Map<String, Object>> modules = new ArrayList<>();
        String sql = "SELECT DISTINCT m.module_id, m.module_name, m.module_code " +
                "FROM module m " +
                "JOIN course_module cm ON m.module_id = cm.module_id " +      // Link Module to Course_Module
                "JOIN enrollment e ON cm.course_id = e.course_id " +         // Link Course_Module to Enrollment
                "WHERE e.student_id = ? AND e.enrollment_status = 'active'"; // Filter by Student ID and status

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("module_id", rs.getInt("module_id"));
                    map.put("name", rs.getString("module_name"));
                    map.put("code", rs.getString("module_code"));
                    modules.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading student modules: " + e.getMessage());
            e.printStackTrace();
        }
        return modules;
    }

    public List<Map<String, Object>> getMaterialsByModuleId(int moduleId) {
        List<Map<String, Object>> materials = new ArrayList<>();
        String sql = "SELECT material_id, material_type, week_number, file_name, upload_date " +
                "FROM teaching_material " +
                "WHERE module_id = ? ORDER BY week_number, material_type";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("material_id", rs.getInt("material_id"));
                    map.put("type", rs.getString("material_type"));
                    map.put("week", rs.getInt("week_number"));
                    map.put("filename", rs.getString("file_name"));
                    map.put("date", rs.getDate("upload_date"));
                    materials.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public Map<String, Object> getMaterialFileDetails(int materialId) {
        String sql = "SELECT file_name FROM teaching_material WHERE material_id = ?";
        Map<String, Object> details = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.put("filename", rs.getString("file_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching material file details: " + e.getMessage());
        }
        return details;
    }

    public List<Map<String, Object>> getMarksHistory(int studentId) {
        List<Map<String, Object>> marks = new ArrayList<>();
        String sql = "SELECT ma.attempt_number, ma.exam_mark, ma.lab_mark, ma.overall_mark, ma.status, ma.attempt_date, " +
                "m.module_code, m.module_name " +
                "FROM module_attempt ma JOIN module m ON ma.module_id = m.module_id " +
                "WHERE ma.student_id = ? " +
                "ORDER BY m.module_code, ma.attempt_number DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", rs.getString("module_code"));
                    map.put("name", rs.getString("module_name"));
                    map.put("attempt", rs.getInt("attempt_number"));
                    map.put("exam", rs.getObject("exam_mark"));
                    map.put("lab", rs.getObject("lab_mark"));
                    map.put("overall", rs.getObject("overall_mark"));
                    map.put("status", rs.getString("status"));
                    map.put("date", rs.getDate("attempt_date"));
                    marks.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marks;
    }


    public List<Map<String, Object>> getStudentDecisions(int studentId) {
        List<Map<String, Object>> decisions = new ArrayList<>();
        String sql = "SELECT decision_id, decision_type, decision_date, issued_by " +
                "FROM decision " +
                "WHERE student_id = ? " +
                "ORDER BY decision_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("decision_id", rs.getInt("decision_id"));
                    map.put("type", rs.getString("decision_type"));
                    map.put("date", rs.getDate("decision_date"));
                    map.put("manager_id", rs.getInt("issued_by")); // Manager ID
                    decisions.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading student decisions: " + e.getMessage());
            e.printStackTrace();
        }
        return decisions;
    }

    // Get the Lecturer ID based on the User ID
    public int getLecturerId(int userId) {
        String sql = "SELECT lecturer_id FROM lecturer WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("lecturer_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get all modules assigned to a specific lecturer
    public List<Map<String, Object>> getModulesByLecturerId(int lecturerId) {
        List<Map<String, Object>> modules = new ArrayList<>();
        String sql = "SELECT DISTINCT m.module_id, m.module_code, m.module_name, m.credit " +
                "FROM module m " +
                "JOIN module_assignment ma ON m.module_id = ma.module_id " +
                "WHERE ma.lecturer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("module_id", rs.getInt("module_id"));
                    map.put("code", rs.getString("module_code"));
                    map.put("name", rs.getString("module_name"));
                    map.put("credits", rs.getInt("credit"));
                    // Set description to empty string since it's not in the DB
                    map.put("description", "");
                    modules.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading lecturer modules: " + e.getMessage());
            e.printStackTrace();
        }
        return modules;
    }

    // Update module details
    public boolean updateModuleInfo(int moduleId, String code, String name, int credit, String description) {
        String sql = "UPDATE module SET module_code = ?, module_name = ?, credit = ? WHERE module_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setInt(3, credit);
            stmt.setInt(4, moduleId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Upload teaching material to the database
    public boolean uploadTeachingMaterial(int moduleId, int lecturerId, String type, int week, String fileName) {
        String sql = "INSERT INTO `teaching_material` " +
                "(`module_id`, `lecturer_id`, `material_type`, `week_number`, `file_name`, `upload_date`) " +
                "VALUES (?, ?, ?, ?, ?, CURDATE())"; // Using CURDATE() for upload_date

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, moduleId);
            stmt.setInt(2, lecturerId);
            stmt.setString(3, type);
            stmt.setInt(4, week);
            stmt.setString(5, fileName);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all enrolled students for a specific module
    public List<Map<String, Object>> getEnrolledStudentsByModuleId(int moduleId) {
        List<Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT DISTINCT s.student_id, u.first_name, u.surname, u.email, e.enrollment_date " +
                "FROM module m " +
                "JOIN course_module cm ON m.module_id = cm.module_id " +
                "JOIN enrollment e ON cm.course_id = e.course_id " +
                "JOIN student s ON e.student_id = s.student_id " +
                "JOIN user u ON s.user_id = u.user_id " +
                "WHERE m.module_id = ? AND e.enrollment_status = 'active'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("student_id", rs.getInt("student_id"));
                    map.put("firstName", rs.getString("first_name"));
                    map.put("surname", rs.getString("surname"));
                    map.put("email", rs.getString("email"));
                    map.put("enrollmentDate", rs.getDate("enrollment_date"));
                    students.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading enrolled students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    // Get all student marks/attempts for a specific module
    public List<Map<String, Object>> getModuleMarksHistory(int moduleId) {
        List<Map<String, Object>> marks = new ArrayList<>();
        String sql = "SELECT ma.attempt_id, ma.student_id, ma.attempt_number, ma.exam_mark, ma.lab_mark, ma.overall_mark, ma.status, " +
                "u.first_name, u.surname " +
                "FROM module_attempt ma " +
                "JOIN student s ON ma.student_id = s.student_id " +
                "JOIN user u ON s.user_id = u.user_id " +
                "WHERE ma.module_id = ?";

        String simpleSql = "SELECT ma.attempt_id, ma.student_id, ma.attempt_number, ma.exam_mark, ma.lab_mark, ma.overall_mark, ma.status, " +
                "u.first_name, u.surname " +
                "FROM module_attempt ma " +
                "JOIN student s ON ma.student_id = s.student_id " +
                "JOIN user u ON s.user_id = u.user_id " +
                "WHERE ma.module_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(simpleSql)) {
            stmt.setInt(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("attempt_id", rs.getInt("attempt_id"));
                    map.put("student_id", rs.getInt("student_id"));
                    map.put("attempt_number", rs.getInt("attempt_number"));
                    map.put("exam_mark", rs.getObject("exam_mark"));
                    map.put("lab_mark", rs.getObject("lab_mark"));
                    map.put("overall_mark", rs.getObject("overall_mark"));
                    map.put("status", rs.getString("status"));
                    map.put("firstName", rs.getString("first_name"));
                    map.put("surname", rs.getString("surname"));
                    marks.add(map);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading module marks: " + e.getMessage());
            e.printStackTrace();
        }
        return marks;
    }


    // Update or Insert Module Attempt Mark
    public boolean updateOrCreateModuleAttempt(int attemptId, int studentId, int moduleId, int attemptNumber, Integer examMark, Integer labMark, Integer overallMark, String status) {

        String sql;

        if (attemptId > 0) {
            // UPDATE existing attempt
            sql = "UPDATE `module_attempt` SET exam_mark = ?, lab_mark = ?, overall_mark = ?, status = ?, attempt_date = CURDATE() WHERE attempt_id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setObject(1, examMark, java.sql.Types.INTEGER);
                stmt.setObject(2, labMark, java.sql.Types.INTEGER);
                stmt.setObject(3, overallMark, java.sql.Types.INTEGER);
                stmt.setString(4, status);
                stmt.setInt(5, attemptId);

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            // INSERT new attempt
            sql = "INSERT INTO `module_attempt` (student_id, module_id, attempt_number, exam_mark, lab_mark, overall_mark, status, attempt_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE())";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, studentId);
                stmt.setInt(2, moduleId);
                stmt.setInt(3, attemptNumber);
                stmt.setObject(4, examMark, java.sql.Types.INTEGER);
                stmt.setObject(5, labMark, java.sql.Types.INTEGER);
                stmt.setObject(6, overallMark, java.sql.Types.INTEGER);
                stmt.setString(7, status);

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    // Return active/inactive users for the Manage Accounts screen
    public List<Map<String, Object>> getUsersForManagement(String roleFilter, Boolean isActive) {
        List<Map<String, Object>> users = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT user_id, first_name, surname, email, user_type, account_status " +
                        "FROM user " +
                        "WHERE account_status IN ('active','inactive')"
        );

        List<Object> params = new ArrayList<>();

        if (roleFilter != null && !roleFilter.equalsIgnoreCase("All")) {
            sql.append(" AND user_type = ?");
            params.add(roleFilter.toLowerCase());
        }

        if (isActive != null) {
            sql.append(" AND account_status = ?");
            params.add(isActive ? "active" : "inactive");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("user_id", rs.getInt("user_id"));
                    String firstName = rs.getString("first_name");
                    String surname   = rs.getString("surname");
                    map.put("name", firstName + " " + surname);
                    map.put("email", rs.getString("email"));
                    map.put("role", rs.getString("user_type"));
                    map.put("active", "active".equalsIgnoreCase(rs.getString("account_status")));
                    users.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Activate / deactivate account
    public boolean updateUserAccountStatus(int userId, boolean active) {
        String sql = "UPDATE user SET account_status = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, active ? "active" : "inactive");
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Assign Module to Lecturer

    public List<Map<String, Object>> getAllLecturers() {
        List<Map<String, Object>> lecturers = new ArrayList<>();
        String sql = "SELECT l.lecturer_id, u.first_name, u.surname " +
                "FROM lecturer l " +
                "JOIN user u ON l.user_id = u.user_id " +
                "WHERE u.account_status = 'active' " +
                "ORDER BY u.first_name, u.surname";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("lecturer_id", rs.getInt("lecturer_id"));
                String fullName = rs.getString("first_name") + " " + rs.getString("surname");
                map.put("name", fullName);
                lecturers.add(map);
            }
        } catch (SQLException e) {
            System.err.println("Error loading lecturers: " + e.getMessage());
            e.printStackTrace();
        }
        return lecturers;
    }

    public List<Map<String, Object>> getAllModules() {
        List<Map<String, Object>> modules = new ArrayList<>();
        String sql = "SELECT module_id, module_code, module_name, credit FROM module ORDER BY module_code";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("module_id", rs.getInt("module_id"));
                map.put("code", rs.getString("module_code"));
                map.put("name", rs.getString("module_name"));
                map.put("credit", rs.getInt("credit"));
                modules.add(map);
            }
        } catch (SQLException e) {
            System.err.println("Error loading modules: " + e.getMessage());
            e.printStackTrace();
        }
        return modules;
    }

    public List<Map<String, Object>> getModuleAssignments() {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT m.module_id, m.module_code, m.module_name, " +
                "       l.lecturer_id AS lecturer_id, u.first_name, u.surname " +
                "FROM module m " +
                "LEFT JOIN module_assignment ma ON m.module_id = ma.module_id " +
                "LEFT JOIN lecturer l ON ma.lecturer_id = l.lecturer_id " +
                "LEFT JOIN user u ON l.user_id = u.user_id " +
                "ORDER BY m.module_code";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("module_id", rs.getInt("module_id"));
                map.put("code", rs.getString("module_code"));
                map.put("name", rs.getString("module_name"));

                Integer lecturerId = rs.getInt("lecturer_id");
                if (rs.wasNull()) {
                    lecturerId = null;
                }
                map.put("lecturer_id", lecturerId);

                String firstName = rs.getString("first_name");
                String surname   = rs.getString("surname");
                String lecturerName = null;
                if (firstName != null && surname != null) {
                    lecturerName = firstName + " " + surname;
                }
                map.put("lecturer_name", lecturerName);

                rows.add(map);
            }
        } catch (SQLException e) {
            System.err.println("Error loading module assignments: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    public boolean assignModuleToLecturer(int moduleId, int lecturerId) {
        String deleteSql = "DELETE FROM module_assignment WHERE module_id = ?";
        String insertSql = "INSERT INTO module_assignment (module_id, lecturer_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            // Remove any existing assignment for this module
            try (PreparedStatement del = connection.prepareStatement(deleteSql)) {
                del.setInt(1, moduleId);
                del.executeUpdate();
            }

            // Insert new assignment
            try (PreparedStatement ins = connection.prepareStatement(insertSql)) {
                ins.setInt(1, moduleId);
                ins.setInt(2, lecturerId);
                int rows = ins.executeUpdate();
                if (rows == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map<String, Object>> getAllStudents() {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT s.student_id, u.first_name, u.surname
            FROM student s
            JOIN user u ON s.user_id = u.user_id
            WHERE u.account_status = 'active'
            ORDER BY u.first_name, u.surname
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("student_id", rs.getInt("student_id"));
                row.put("name", rs.getString("first_name") + " " + rs.getString("surname"));
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean issueStudentDecision(int studentId, int managerId, String decisionType) {
        String sql = "INSERT INTO decision (student_id, decision_type, decision_date, issued_by) " +
                "VALUES (?, ?, CURDATE(), ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, decisionType);
            if (managerId > 0) {
                stmt.setInt(3, managerId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error issuing student decision: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<Map<String, Object>> getStudentDecisions() {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
        SELECT d.decision_id,
               CONCAT(uStu.first_name, ' ', uStu.surname) AS student,
               d.decision_type AS decision,
               DATE(d.decision_date) AS date,
               CONCAT(COALESCE(uMan.first_name, 'Admin'), 
                      CASE WHEN uMan.surname IS NULL OR uMan.surname = '' 
                           THEN '' 
                           ELSE CONCAT(' ', uMan.surname) 
                      END) AS manager
        FROM decision d
        JOIN student s ON d.student_id = s.student_id
        JOIN user uStu ON s.user_id = uStu.user_id
        LEFT JOIN manager mg ON d.issued_by = mg.manager_id
        LEFT JOIN user uMan ON mg.user_id = uMan.user_id
        ORDER BY d.decision_date DESC, d.decision_id DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id",       rs.getInt("decision_id"));
                row.put("student",  rs.getString("student"));
                row.put("decision", rs.getString("decision"));
                row.put("date",     rs.getString("date"));
                row.put("manager",  rs.getString("manager"));
                list.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error loading all student decisions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    //Enrol Students in a Course
    // Get all courses
    public List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = "SELECT course_id, course_code, course_name, course_description " +
                "FROM course ORDER BY course_code";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("course_id", rs.getInt("course_id"));
                row.put("code",      rs.getString("course_code"));
                row.put("name",      rs.getString("course_name"));
                row.put("description", rs.getString("course_description")); // may be NULL
                list.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Active students NOT currently active on this course
    public List<Map<String, Object>> getAvailableStudentsForCourse(int courseId) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT s.student_id, u.first_name, u.surname
            FROM student s
            JOIN user u ON s.user_id = u.user_id
            WHERE u.account_status = 'active'
              AND s.student_id NOT IN (
                  SELECT e.student_id
                  FROM enrollment e
                  WHERE e.course_id = ? AND e.enrollment_status = 'active'
              )
            ORDER BY u.first_name, u.surname
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("student_id", rs.getInt("student_id"));
                    row.put("name", rs.getString("first_name") + " " + rs.getString("surname"));
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading available students: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Students currently active on this course
    public List<Map<String, Object>> getEnrolledStudentsForCourse(int courseId) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT s.student_id, u.first_name, u.surname
            FROM enrollment e
            JOIN student s ON e.student_id = s.student_id
            JOIN user u    ON s.user_id = u.user_id
            WHERE e.course_id = ? AND e.enrollment_status = 'active'
            ORDER BY u.first_name, u.surname
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("student_id", rs.getInt("student_id"));
                    row.put("name", rs.getString("first_name") + " " + rs.getString("surname"));
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading enrolled students for course: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Enrol or reactivate a student in a course
    public boolean enrolStudentInCourse(int studentId, int courseId) {
        String checkSql = "SELECT enrollment_id, enrollment_status " +
                "FROM enrollment WHERE student_id = ? AND course_id = ?";

        try (PreparedStatement check = connection.prepareStatement(checkSql)) {
            check.setInt(1, studentId);
            check.setInt(2, courseId);

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    int enrollmentId = rs.getInt("enrollment_id");
                    String status = rs.getString("enrollment_status");

                    if ("active".equalsIgnoreCase(status)) {
                        // already active
                        return true;
                    }

                    String updateSql = "UPDATE enrollment " +
                            "SET enrollment_status = 'active', enrollment_date = CURDATE() " +
                            "WHERE enrollment_id = ?";

                    try (PreparedStatement upd = connection.prepareStatement(updateSql)) {
                        upd.setInt(1, enrollmentId);
                        int rows = upd.executeUpdate();
                        return rows > 0;
                    }

                } else {
                    String insertSql = "INSERT INTO enrollment " +
                            "(student_id, course_id, enrollment_date, enrollment_status) " +
                            "VALUES (?, ?, CURDATE(), 'active')";

                    try (PreparedStatement ins = connection.prepareStatement(insertSql)) {
                        ins.setInt(1, studentId);
                        ins.setInt(2, courseId);
                        int rows = ins.executeUpdate();
                        return rows > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Set enrolment to inactive
    public boolean unenrolStudentFromCourse(int studentId, int courseId) {
        String sql = "UPDATE enrollment " +
                "SET enrollment_status = 'withdrawn' " +
                "WHERE student_id = ? AND course_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            int rows = stmt.executeUpdate();
            System.out.println("unenrolStudentFromCourse: updated rows = " + rows);

            return true;
        } catch (SQLException e) {
            System.err.println("Error unenrolling student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Display Module Details
    public List<Map<String, Object>> getModuleDetails(String searchTerm) {
        List<Map<String, Object>> modules = new ArrayList<>();

        String baseSql = "SELECT module_code, module_name, credit FROM module";
        boolean hasSearch = (searchTerm != null && !searchTerm.trim().isEmpty());
        String sql = baseSql;

        if (hasSearch) {
            sql += " WHERE module_code LIKE ? OR module_name LIKE ?";
        }
        sql += " ORDER BY module_code";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (hasSearch) {
                String like = "%" + searchTerm.trim() + "%";
                stmt.setString(1, like);
                stmt.setString(2, like);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("code", rs.getString("module_code"));
                    row.put("name", rs.getString("module_name"));
                    row.put("credits", rs.getInt("credit"));
                    modules.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading module details: " + e.getMessage());
            e.printStackTrace();
        }
        return modules;
    }

    public List<Map<String, Object>> getModulesByCourseId(int courseId) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
        SELECT m.module_id, m.module_code, m.module_name, m.credit
        FROM course_module cm
        JOIN module m ON cm.module_id = m.module_id
        WHERE cm.course_id = ?
        ORDER BY m.module_code
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("module_id", rs.getInt("module_id"));
                    row.put("code", rs.getString("module_code"));
                    row.put("name", rs.getString("module_name"));
                    row.put("credits", rs.getInt("credit"));
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading modules for course: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateCourseDescription(int courseId, String description) {
        String sql = "UPDATE course SET course_description = ? WHERE course_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setInt(2, courseId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating course description: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateModuleCredit(int moduleId, int credit) {
        String sql = "UPDATE module SET credit = ? WHERE module_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, credit);
            stmt.setInt(2, moduleId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating module credit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Add Course
    public boolean addCourse(String code, String name, String description) {
        String sql = "INSERT INTO course (course_code, course_name, course_description) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setString(3, description);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getCourseDetails(int courseId) {
        Map<String, Object> details = new HashMap<>();
        List<Map<String, Object>> modules = new ArrayList<>();
        String courseSql = "SELECT course_code, course_name, course_description FROM course WHERE course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(courseSql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.put("code", rs.getString("course_code"));
                    details.put("name", rs.getString("course_name"));
                    details.put("description", rs.getString("course_description"));
                } else {
                    return Collections.emptyMap();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching course details: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }

        String modulesSql = """
            SELECT m.module_code, m.module_name, m.credit, cm.semester 
            FROM module m
            JOIN course_module cm ON m.module_id = cm.module_id
            WHERE cm.course_id = ?
            ORDER BY cm.semester, m.module_code
            """;

        try (PreparedStatement stmt = connection.prepareStatement(modulesSql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> module = new HashMap<>();
                    module.put("code", rs.getString("module_code"));
                    module.put("name", rs.getString("module_name"));
                    module.put("credits", rs.getInt("credit"));
                    String semester = rs.getString("semester");
                    module.put("semester", semester);

                    modules.add(module);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching course modules/semesters: " + e.getMessage());
            e.printStackTrace();
        }

        details.put("modules", modules);
        return details;
    }

    // Add Module
    public boolean addModule(String code, String name, int credit) {
        String sql = "INSERT INTO module (module_code, module_name, credit, department_id) " +
                "VALUES (?, ?, ?, NULL)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setInt(3, credit);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding module: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getCourseModuleAssignments() {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = """
            SELECT cm.course_module_id, 
                   c.course_id, c.course_code, c.course_name,
                   m.module_id, m.module_code, m.module_name,
                   cm.semester
            FROM course_module cm
            JOIN course c ON cm.course_id = c.course_id
            JOIN module m ON cm.module_id = m.module_id
            ORDER BY c.course_code, cm.semester, m.module_code
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("course_module_id", rs.getInt("course_module_id"));
                map.put("course_id", rs.getInt("course_id"));
                map.put("course_code", rs.getString("course_code"));
                map.put("course_name", rs.getString("course_name"));
                map.put("module_id", rs.getInt("module_id"));
                map.put("module_code", rs.getString("module_code"));
                map.put("module_name", rs.getString("module_name"));
                map.put("semester", rs.getString("semester"));
                rows.add(map);
            }
        } catch (SQLException e) {
            System.err.println("Error loading course module assignments: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    public boolean assignModuleToCourse(int courseId, int moduleId, String semester) {
        // Check if the assignment already exists (same course and module)
        String checkSql = "SELECT course_module_id FROM course_module WHERE course_id = ? AND module_id = ?";
        String updateSql = "UPDATE course_module SET semester = ? WHERE course_module_id = ?";
        String insertSql = "INSERT INTO course_module (course_id, module_id, semester) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Check for existing assignment
            try (PreparedStatement check = connection.prepareStatement(checkSql)) {
                check.setInt(1, courseId);
                check.setInt(2, moduleId);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        // update the semester
                        int assignmentId = rs.getInt("course_module_id");
                        try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                            update.setString(1, semester);
                            update.setInt(2, assignmentId);
                            update.executeUpdate();
                        }
                    } else {
                        // insert
                        try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                            insert.setInt(1, courseId);
                            insert.setInt(2, moduleId);
                            insert.setString(3, semester);
                            insert.executeUpdate();
                        }
                    }
                }
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Business Rules

    public List<Map<String, Object>> getAllRules() {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = """
            SELECT br.rule_id, br.rule_type, br.rule_value, br.scope, br.target_id,
                   c.course_name, m.module_name
            FROM business_rule br
            LEFT JOIN course c ON br.scope = 'course_specific' AND br.target_id = c.course_id
            LEFT JOIN module m ON br.scope = 'module_specific' AND br.target_id = m.module_id
            ORDER BY br.rule_id DESC
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("rule_id", rs.getInt("rule_id"));
                map.put("rule_type", rs.getString("rule_type"));
                map.put("rule_value", rs.getInt("rule_value"));
                String scope = rs.getString("scope");
                map.put("scope", scope);

                // Determine target name for display
                String targetName = "";
                Integer targetId = rs.getInt("target_id");
                if (rs.wasNull()) {
                    targetId = null;
                }

                if ("course_specific".equals(scope) && rs.getString("course_name") != null) {
                    targetName = rs.getString("course_name") + " (ID: " + targetId + ")";
                } else if ("module_specific".equals(scope) && rs.getString("module_name") != null) {
                    targetName = rs.getString("module_name") + " (ID: " + targetId + ")";
                } else if ("global".equals(scope)) {
                    targetName = "N/A";
                } else if (targetId != null) {
                    targetName = "ID: " + targetId + " (Not Found)";
                }
                map.put("target_name", targetName);

                rows.add(map);
            }
        } catch (SQLException e) {
            System.err.println("Error loading business rules: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    public boolean addBusinessRule(String ruleType, int ruleValue, String scope, Integer targetId) {
        String sql = "INSERT INTO business_rule (rule_type, rule_value, scope, target_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ruleType);
            stmt.setInt(2, ruleValue);
            stmt.setString(3, scope);

            if (targetId != null && targetId > 0) {
                stmt.setInt(4, targetId);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding business rule: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    // Reset password for a user
    public boolean resetUserPassword(int userId, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}