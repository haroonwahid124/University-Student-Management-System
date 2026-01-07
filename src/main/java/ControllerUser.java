import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class ControllerUser {
    private ViewSignup vSignup;
    private ViewLogin vLogin;
    private IUserModel mUser;
    private ViewApproveUsers vApprove;
    private ViewUpdatePassword vUpdate;
    private IValidator validator;

    // Student GUIS
    private ViewStudentDashboard vDashboard;
    private ViewCourseModuleInformation vCourseModuleInformation;
    private ViewDownloadMaterials vDownloadMaterials;
    private ViewMarksProgress vMarksProgress;
    private ViewStudentDecisions vStudentDecisions;

    // Lecturer GUIS
    private ViewLecturerDashboard vLecturerDashboard;
    private ViewUpdateModuleInfo vUpdateModuleInfo;
    private ViewUploadMaterials vUploadMaterials;
    private ViewEnrolledStudents vEnrolledStudents;
    private ViewMarksManagement vMarksManagement;

    // Manager GUIS
    private ViewManagerDashboard vManagerDashboard;
    private ViewManageAccounts vManageAccounts;
    private ViewAssignModuleToLecturer vAssignModuleToLecturer;
    private ViewIssueStudentDecision vIssueStudentDecision;
    private ViewEnrolStudentsCourse vEnrolStudentsCourse;
    private ViewDisplayModuleDetails vDisplayModuleDetails;
    private ViewUpdateCourseInfo vUpdateCourseInfo;
    private ViewAddCourse vAddCourse;
    private ViewDisplayCourseDetails vDisplayCourseDetails;
    private ViewAssignModuleToCourse vAssignModuleToCourse;
    private ViewAddBusinessRule vAddBusinessRule;


    // State Variables
    private String activeUserFirstName = "";
    private int activeUserId = -1;
    private int activeStudentId = -1; // store the specific student ID
    private int activeEnrollmentId = -1; // store the student's active enrollment
    private int selectedModuleIdForDownload = -1; // store the ID of the module selected from the table
    private int activeManagerId = -1; // stores Manager ID
    private int activeLecturerId = -1; // Lecturer ID


    // Temp state variable for marks assignment
    private int lastUpdatedAttemptId = -1;
    private boolean isTableRefreshing = false;

    public ControllerUser(ViewSignup vSignup, ViewLogin vLogin, IUserModel mUser, ViewApproveUsers vApprove, ViewUpdatePassword vUpdate, ViewStudentDashboard vDashboard, ViewCourseModuleInformation vCourseModuleInformation, ViewDownloadMaterials vDownloadMaterials, ViewMarksProgress vMarksProgress, ViewStudentDecisions vStudentDecisions, ViewLecturerDashboard vLecturerDashboard, ViewUpdateModuleInfo vUpdateModuleInfo, ViewUploadMaterials vUploadMaterials, ViewEnrolledStudents vEnrolledStudents, ViewMarksManagement vMarksManagement, ViewManagerDashboard vManagerDashboard, ViewManageAccounts vManageAccounts,  ViewAssignModuleToLecturer vAssignModuleToLecturer, ViewIssueStudentDecision vIssueStudentDecision, ViewEnrolStudentsCourse vEnrolStudentsCourse, ViewDisplayModuleDetails vDisplayModuleDetails, ViewUpdateCourseInfo vUpdateCourseInfo, ViewAddCourse vAddCourse, ViewDisplayCourseDetails vDisplayCourseDetails, ViewAssignModuleToCourse vAssignModuleToCourse, ViewAddBusinessRule vAddBusinessRule) {        this.vLogin = vLogin;
        this.vSignup = vSignup;
        this.vLogin = vLogin;
        this.mUser = mUser;
        this.vApprove = vApprove;
        this.vUpdate = vUpdate;
        this.validator = new IValidatorImpl();
        this.vDashboard = vDashboard;
        this.vCourseModuleInformation = vCourseModuleInformation;
        this.vDownloadMaterials = vDownloadMaterials;
        this.vMarksProgress = vMarksProgress;
        this.vStudentDecisions = vStudentDecisions;

        // Lecturer GUIS
        this.vLecturerDashboard = vLecturerDashboard;
        this.vUpdateModuleInfo = vUpdateModuleInfo;
        this.vUploadMaterials = vUploadMaterials;
        this.vEnrolledStudents = vEnrolledStudents;
        this.vMarksManagement = vMarksManagement;

        // Manager GUIS
        this.vManagerDashboard = vManagerDashboard;
        this.vManageAccounts   = vManageAccounts;
        this.vAssignModuleToLecturer = vAssignModuleToLecturer;
        this.vIssueStudentDecision = vIssueStudentDecision;
        this.vEnrolStudentsCourse = vEnrolStudentsCourse;
        this.vDisplayModuleDetails = vDisplayModuleDetails;
        this.vUpdateCourseInfo = vUpdateCourseInfo;
        this.vAddCourse = vAddCourse;
        this.vDisplayCourseDetails = vDisplayCourseDetails;
        this.vAssignModuleToCourse = vAssignModuleToCourse;
        this.vAddBusinessRule = vAddBusinessRule;

        // Initialize views
        vSignup.getPanelMain().setVisible(true);
        vLogin.getPanelMain().setVisible(false);
        vUpdate.getPanelMain().setVisible(false);
        vDashboard.getPanelMain().setVisible(false);
        vCourseModuleInformation.getPanelMain().setVisible(false);
        vDownloadMaterials.getPanelMain().setVisible(false);
        vMarksProgress.getPanelMain().setVisible(false);

        vLecturerDashboard.getPanelMain().setVisible(false);
        vUpdateModuleInfo.getPanelMain().setVisible(false);
        vUploadMaterials.getPanelMain().setVisible(false);
        vEnrolledStudents.getPanelMain().setVisible(false);

        vStudentDecisions.getPanelMain().setVisible(false);
        vManagerDashboard.getPanelMain().setVisible(false);
        vManageAccounts.getPanelMain().setVisible(false);
        vAssignModuleToLecturer.getPanelMain().setVisible(false);
        vIssueStudentDecision.getPanelMain().setVisible(false);
        vEnrolStudentsCourse.getPanelMain().setVisible(false);
        vDisplayModuleDetails.getPanelMain().setVisible(false);
        vAddCourse.getPanelMain().setVisible(false);
        vDisplayCourseDetails.getPanelMain().setVisible(false);
        vAssignModuleToCourse.getPanelMain().setVisible(false);
        vAddBusinessRule.getPanelMain().setVisible(false);

        // Add action listeners for ViewSignup
        vSignup.getSignupButton().addActionListener(e -> handleSignup());
        vSignup.getClearButton().addActionListener(e -> vSignup.clearTxts());
        vSignup.getShowLoginButton().addActionListener(e -> showLoginGUI());

        // ViewLogin Listeners
        this.vLogin.getLoginButton().addActionListener(e -> handleLogin());
        vLogin.getClearButton().addActionListener(e -> vLogin.clearTxts());
        vLogin.getShowSignupButton().addActionListener(e -> showSignupGUI());
        vLogin.getShowUpdatePasswordButton().addActionListener(e -> showUpdatePasswordGUI());

        // ViewApproveUsers Listeners
        vApprove.getBackButton().addActionListener(e -> showManagerDashboardGUI());
        vApprove.getApproveButton().addActionListener(e -> handleApproveUser());
        vApprove.getRejectButton().addActionListener(e -> handleRejectUser());

        // ViewUpdatePassword Listeners
        vUpdate.getUpdateButton().addActionListener(e -> handleUpdatePassword());
        vUpdate.getClearButton().addActionListener(e -> vUpdate.clearTxts());
        vUpdate.getShowLoginButton().addActionListener(e -> showLoginGUI());


        // Dashboard Listeners
        vDashboard.getViewModulesButton().addActionListener(e -> showCourseModuleInformationGUI());
        vDashboard.getDownloadMaterialsButton().addActionListener(e -> showDownloadMaterialsGUI());
        vDashboard.getViewDecisionsButton().addActionListener(e -> showStudentDecisionsGUI());
        vDashboard.getViewMarksButton().addActionListener(e -> showMarksProgressGUI());
        vDashboard.getLogoutButton().addActionListener(e -> {
            activeUserFirstName = "";
            activeUserId = -1;
            activeStudentId = -1;
            showLoginGUI();
        });

        // Inner View Listeners
        vStudentDecisions.getBackButton().addActionListener(e -> showStudentDashboardGUI());
        vCourseModuleInformation.getBackButton().addActionListener(e -> showStudentDashboardGUI());
        vCourseModuleInformation.getViewMaterialsButton().addActionListener(e -> handleViewMaterials());

        vDownloadMaterials.getBackButton().addActionListener(e -> showStudentDashboardGUI());
        vDownloadMaterials.getModuleComboBox().addActionListener(e -> handleModuleSelectionForMaterials());
        vDownloadMaterials.getDownloadButton().addActionListener(e -> handleDownloadFile());

        vMarksProgress.getBackButton().addActionListener(e -> showStudentDashboardGUI());

        // Lecturer Dashboard Listeners
        vLecturerDashboard.getUpdateModuleButton().addActionListener(e -> showUpdateModuleInfoGUI());
        vLecturerDashboard.getUploadMaterialsButton().addActionListener(e -> showUploadMaterialsGUI());
        vLecturerDashboard.getViewEnrolledStudentsButton().addActionListener(e -> showEnrolledStudentsGUI());
        vLecturerDashboard.getMarksManagementButton().addActionListener(e -> showMarksManagementGUI());
        vLecturerDashboard.getLogoutButton().addActionListener(e -> {
            activeUserFirstName = "";
            activeUserId = -1;
            activeLecturerId = -1;
            showLoginGUI();
        });

        // Lecturer Inner View Listeners
        vUpdateModuleInfo.getBackButton().addActionListener(e -> showLecturerDashboardGUI());

        // Listener to load selected module details into fields
        vUpdateModuleInfo.getCurrentModulesTable().getSelectionModel().addListSelectionListener(e -> handleModuleSelectionForUpdate());
        vUpdateModuleInfo.getUpdateButton().addActionListener(e -> handleUpdateModuleInfo());

        // Upload Materials Listeners
        vUploadMaterials.getBackButton().addActionListener(e -> showLecturerDashboardGUI());
        vUploadMaterials.getUploadButton().addActionListener(e -> handleMaterialUpload());

        // Enrolled Students Listeners
        vEnrolledStudents.getBackButton().addActionListener(e -> showLecturerDashboardGUI());
        vEnrolledStudents.getModuleComboBox().addActionListener(e -> handleModuleSelectionForStudents());

        // Marks Management Listeners
        vMarksManagement.getBackButton().addActionListener(e -> showLecturerDashboardGUI());
        vMarksManagement.getModuleComboBox().addActionListener(e -> handleModuleSelectionForMarksManagement());
        vMarksManagement.getStudentMarksTable().getSelectionModel().addListSelectionListener(e -> handleMarkAttemptSelection());
        vMarksManagement.getUpdateMarkButton().addActionListener(e -> handleMarkAttemptUpdate());

        // Manager Dashboard Listeners
        vManagerDashboard.getApproveRejectUsersButton().addActionListener(e -> showApproveUsersGUI());
        vManagerDashboard.getManageUserAccountsButton().addActionListener(e -> showManageAccountsGUI());
        vManagerDashboard.getAddCourseModuleButton().addActionListener(e -> showAddCourseGUI());
        vManagerDashboard.getAssignModuleToCourseButton().addActionListener(e -> showAssignModuleToCourseGUI());
        vManagerDashboard.getUpdateCourseInfoButton().addActionListener(e -> showUpdateCourseInfoGUI());
        vManagerDashboard.getEnrolStudentsButton().addActionListener(e -> showEnrolStudentsCourseGUI());
        vManagerDashboard.getAssignModuleToLecturerButton().addActionListener(e -> showAssignModuleToLecturerGUI());
        vManagerDashboard.getIssueStudentDecisionButton().addActionListener(e -> showIssueStudentDecisionGUI());
        vManagerDashboard.getAddBusinessRuleButton().addActionListener(e -> showAddBusinessRuleGUI());
        vManagerDashboard.getDisplayCourseDetailsButton().addActionListener(e -> showDisplayCourseDetailsGUI());
        vManagerDashboard.getDisplayModuleDetailsButton().addActionListener(e -> showDisplayModuleDetailsGUI());
        vManagerDashboard.getLogoutButton().addActionListener(e -> {
            activeUserFirstName = "";
            activeUserId = -1;
            activeManagerId = -1;
            showLoginGUI();
        });


        // Manage Accounts Listeners
        vManageAccounts.getBtnSearch().addActionListener(e -> loadManageAccountsTable());
        vManageAccounts.getBtnActivate().addActionListener(e -> handleActivateUser());
        vManageAccounts.getBtnDeactivate().addActionListener(e -> handleDeactivateUser());
        vManageAccounts.getBtnResetPassword().addActionListener(e -> handleResetPasswordForUser());
        vManageAccounts.getBtnBack().addActionListener(e -> showManagerDashboardGUI());

        // Assign Module to Lecturer View Listeners
        vAssignModuleToLecturer.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vAssignModuleToLecturer.getBtnAssign().addActionListener(e -> handleAssignModuleToLecturer());

        vIssueStudentDecision.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vIssueStudentDecision.getBtnIssue().addActionListener(e -> handleIssueDecision());

        // Enrol Students in Course view
        vEnrolStudentsCourse.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vEnrolStudentsCourse.getCmbCourse().addActionListener(e -> handleCourseSelectionForEnrolment());
        vEnrolStudentsCourse.getBtnEnroll().addActionListener(e -> handleEnrollStudentToCourse());
        vEnrolStudentsCourse.getBtnUnenrol().addActionListener(e -> handleUnenrolStudentFromCourse());

        // Display Module Details View listeners
        vDisplayModuleDetails.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vDisplayModuleDetails.getBtnSearch().addActionListener(e -> handleSearchModuleDetails());
        vDisplayModuleDetails.getBtnRefresh().addActionListener(e -> {
            vDisplayModuleDetails.getTxtSearch().setText("");
            loadModuleDetails(null);
        });

        // ViewUpdateCourseInfo listeners
        vUpdateCourseInfo.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vUpdateCourseInfo.getCmbCourse().addActionListener(e -> handleCourseSelectionForUpdateCourseInfo());
        vUpdateCourseInfo.getBtnSaveDesc().addActionListener(e -> handleSaveCourseDescription());
        vUpdateCourseInfo.getBtnSaveCredits().addActionListener(e -> handleSaveModuleCredits());


        // Add Course view listeners
        vAddCourse.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vAddCourse.getBtnClear().addActionListener(e -> vAddCourse.clearInputs());
        vAddCourse.getBtnAdd().addActionListener(e -> handleAddCourseOrModule());
        vAddCourse.getRbAddCourse().addActionListener(e -> handleModeToggle(true));
        vAddCourse.getRbAddModule().addActionListener(e -> handleModeToggle(false));

        // Display Course Details Listeners
        vDisplayCourseDetails.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vDisplayCourseDetails.getBtnDisplay().addActionListener(e -> handleDisplayCourseDetails());
        vDisplayCourseDetails.getCmbCourse().addActionListener(e -> vDisplayCourseDetails.clearTable()); // Clear table when course changes

        // Assign Module to Course View Listeners (add after vAssignModuleToLecturer listeners)
        vAssignModuleToCourse.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vAssignModuleToCourse.getBtnAssign().addActionListener(e -> handleAssignModuleToCourse());

        // Add Business Rule View Listeners
        vAddBusinessRule.getBtnBack().addActionListener(e -> showManagerDashboardGUI());
        vAddBusinessRule.getBtnRefresh().addActionListener(e -> loadAllRules());
        vAddBusinessRule.getBtnAddRule().addActionListener(e -> handleAddBusinessRule());
        vAddBusinessRule.getCmbScope().addActionListener(e -> handleRuleScopeChange());
    }






    private void handleSignup() {
        // Read all input values
        String firstName = vSignup.getFirstNameTxt().getText();
        String surname = vSignup.getSurname().getText();
        String email = vSignup.getEmailTxt().getText();
        String dobString = vSignup.getDobTxt().getText();

        char[] password = vSignup.getPasswordField1().getPassword();
        char[] retypePassword = vSignup.getPasswordField2().getPassword();
        String gender = (String) vSignup.getGender_value().getSelectedItem();
        String user_type = (String) vSignup.getUserType().getSelectedItem();

        // Basic Name and Email validation
        if (!validator.validateName(firstName) || !validator.validateName(surname) || !validator.validateEmail(email)) {
            JOptionPane.showMessageDialog(null, "Input Error: Invalid name or email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Password Match and Basic Strength Check
        if (!Arrays.equals(password, retypePassword)) {
            JOptionPane.showMessageDialog(null, "Input Error: Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length < 6) {
            JOptionPane.showMessageDialog(null, "Input Error: Password must be at least 6 characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Date Handling Logic
        Date dob = null;
        if (!validator.validateDate(dobString)) {
            JOptionPane.showMessageDialog(null, "Error: Please enter a valid Date of Birth in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobString);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Error: Date parsing failed.", "Internal Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean signupSuccess = mUser.signup(firstName, surname, email, password, retypePassword, gender, dob, user_type);

        if (signupSuccess) {
            // show feedback to the user
            JOptionPane.showMessageDialog(null, "User " + email +
                    " signed up successfully! Account is pending approval.", "Info.", JOptionPane.INFORMATION_MESSAGE);
            // clear text fields for the next sing-up operation
            vSignup.clearTxts();
        } else {
            JOptionPane.showMessageDialog(null, "User " + email +
                    " Error: signing up was unsuccessful. The email may already be registered or an internal error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogin() {
        String email = vLogin.getEmailTxt().getText();
        char[] password = vLogin.getPasswordField1().getPassword();
        boolean loginSuccess = mUser.login(email, password);

        if (loginSuccess) {
            Map<String, Object> userDetails = mUser.getUserDetails(email);
            String userType = (String) userDetails.get("user_type");
            String firstName = (String) userDetails.getOrDefault("first_name", "User");
            Integer userId = (Integer) userDetails.getOrDefault("user_id", -1);

            activeUserFirstName = firstName;
            activeUserId = userId;
            vLogin.clearTxts();

            if ("manager".equalsIgnoreCase(userType)) {
                // Manager/Admin Logic
                activeManagerId = (Integer) userDetails.getOrDefault("manager_id", -1);
                showManagerDashboardGUI();
            } else if ("student".equalsIgnoreCase(userType)) {
                // Student Logic
                activeStudentId = (Integer) userDetails.getOrDefault("student_id", -1);
                if (activeStudentId > 0) { // Check against 0/null/error
                    showStudentDashboardGUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Student record incomplete.", "Error", JOptionPane.ERROR_MESSAGE);
                    showLoginGUI();
                }
            } else if ("lecturer".equalsIgnoreCase(userType)) {
                // Lecturer Logic
                activeLecturerId = (Integer) userDetails.getOrDefault("lecturer_id", -1);

                if (activeLecturerId > 0) { // Check against 0/null/error
                    showLecturerDashboardGUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Lecturer record incomplete or not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    showLoginGUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Welcome, " + userType + "!", "Info.", JOptionPane.INFORMATION_MESSAGE);
                showLoginGUI();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: logging in was unsuccessful. Check credentials or account status (pending/inactive).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void refreshPendingUsersList() {
        // Get pending users from the model
        List<String> pendingEmails = mUser.getPendingUsers(null);
        // Update the JList in the View
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String email : pendingEmails) {
            listModel.addElement(email);
        }
        vApprove.getUserList().setModel(listModel);
    }


    // To approve newly signed up users awaiting approval
    private void handleApproveUser() {
        String selectedUsername = vApprove.getUserList().getSelectedValue();
        if (selectedUsername == null) {
            vApprove.showMessage("Please select a user to approve.", "Warning");
            return;
        }
        // Call the Model method to approve the user
        boolean success = mUser.approveUser(selectedUsername);

        if (success) {
            vApprove.showMessage("User '" + selectedUsername + "' has been approved! Status set to 'active'.", "Success");
            refreshPendingUsersList(); // Update the list immediately
        } else {
            vApprove.showMessage("Error approving user: '" + selectedUsername + "'.", "Error");
        }
    }


    // To reject newly signed up users awaiting approval
    private void handleRejectUser() {
        String selectedUsername = vApprove.getUserList().getSelectedValue();
        if (selectedUsername == null) {
            vApprove.showMessage("select a user to reject.", "Warning");
            return;
        }

        boolean success = mUser.denyUser(selectedUsername);

        if (success) {
            vApprove.showMessage("User '" + selectedUsername + "' has been rejected and removed.", "Success");
            refreshPendingUsersList(); // Update the list immediately
        } else {
            vApprove.showMessage("Error rejecting user: '" + selectedUsername + "'.", "Error");
        }
    }

    private void handleUpdatePassword() {
        String username = vUpdate.getUsernameTxt().getText();
        char[] oldPwd = vUpdate.getOldPasswordField().getPassword();
        char[] newPwd = vUpdate.getNewPasswordField1().getPassword();
        char[] confirm = vUpdate.getNewPasswordField2().getPassword();

        boolean ok = mUser.updatePassword(username, oldPwd, newPwd, confirm);
        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Password updated for user: " + username,
                    "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            vUpdate.clearTxts();
            showLoginGUI(); // back to login after success
        } else {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Update failed. Check username/old password or confirmation.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // Course Module Functionality
    private void loadCourseAndModules() {
        if (activeStudentId == -1) {
            vCourseModuleInformation.setCourseName("Error: Student ID not found.");
            return;
        }

        List<Map<String, Object>> enrollments = mUser.loadActiveEnrollments(activeStudentId);

        if (enrollments.isEmpty()) {
            vCourseModuleInformation.setCourseName("No Active Enrollment Found.");
            vCourseModuleInformation.clearModuleTable();
            return;
        }

        // assume only one active enrollment
        Map<String, Object> activeEnrollment = enrollments.get(0);
        String courseName = (String) activeEnrollment.get("course_name");
        activeEnrollmentId = (int) activeEnrollment.get("enrollment_id");

        // Update Course Label
        vCourseModuleInformation.setCourseName(courseName);

        // Load Modules for this Enrollment
        loadModules(activeEnrollmentId);
    }

    private void loadModules(int enrollmentId) {
        List<Map<String, Object>> modules = mUser.loadModulesForEnrollment(enrollmentId);
        DefaultTableModel model = vCourseModuleInformation.getModuleTableModel();
        model.setRowCount(0); // Clear old data

        for (Map<String, Object> m : modules) {
            // Data order must match table columns: Code, Name, Credits, ID
            model.addRow(new Object[]{
                    m.get("code"),
                    m.get("name"),
                    m.get("credits"),
                    m.get("module_id")
            });
        }
    }

    private void handleViewMaterials() {
        int selectedRow = vCourseModuleInformation.getModuleTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a module first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Retrieve the hidden Module ID from the table model
        Integer moduleId = (Integer) vCourseModuleInformation.getModuleTableModel().getValueAt(selectedRow, 3);

        if (moduleId == null || moduleId == -1) {
            JOptionPane.showMessageDialog(null, "Error: Could not retrieve module ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.selectedModuleIdForDownload = moduleId;
        showDownloadMaterialsGUI();
    }

    private void loadDownloadModules() {
        if (activeStudentId == -1) return;

        List<Map<String, Object>> modules = mUser.loadStudentModules(activeStudentId);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Map<String, Integer> moduleMap = new HashMap<>();

        model.addElement("-- Select Module --");

        for (Map<String, Object> m : modules) {
            String moduleNameCode = m.get("code") + " - " + m.get("name");
            model.addElement(moduleNameCode);
            moduleMap.put(moduleNameCode, (Integer) m.get("module_id"));
        }

        vDownloadMaterials.getModuleComboBox().setModel(model);
        vDownloadMaterials.getModuleComboBox().putClientProperty("moduleMap", moduleMap);
        vDownloadMaterials.clearMaterialsTable();
    }

    private void handleModuleSelectionForMaterials() {
        String selectedModule = (String) vDownloadMaterials.getModuleComboBox().getSelectedItem();
        vDownloadMaterials.clearMaterialsTable();

        if (selectedModule == null || selectedModule.startsWith("--")) return;

        Map<String, Integer> moduleMap = (Map<String, Integer>) vDownloadMaterials.getModuleComboBox().getClientProperty("moduleMap");
        Integer moduleId = moduleMap.get(selectedModule);

        if (moduleId != null) {
            loadMaterials(moduleId);
        }
    }

    private void loadMaterials(int moduleId) {
        List<Map<String, Object>> materials = mUser.loadMaterialsByModuleId(moduleId);
        DefaultTableModel model = vDownloadMaterials.getMaterialsTableModel();

        for (Map<String, Object> m : materials) {
            model.addRow(new Object[]{
                    m.get("filename"),
                    m.get("type"),
                    m.get("week"),
                    m.get("date"),
                    m.get("material_id")
            });
        }
    }


    private void loadMarksHistory() {
        if (activeStudentId == -1) return;

        // Fetch the student's single course name for the header
        List<Map<String, Object>> enrollments = mUser.loadActiveEnrollments(activeStudentId);
        if (!enrollments.isEmpty()) {
            vMarksProgress.setCourseName((String) enrollments.get(0).get("course_name"));
        } else {
            vMarksProgress.setCourseName("N/A (No active enrollment)");
        }

        List<Map<String, Object>> marksHistory = mUser.loadMarksHistory(activeStudentId);
        DefaultTableModel model = vMarksProgress.getMarksTableModel();
        model.setRowCount(0);

        for (Map<String, Object> m : marksHistory) {
            // Data order matches table columns
            model.addRow(new Object[]{
                    m.get("code"),
                    m.get("name"),
                    m.get("attempt"),
                    m.get("exam"),
                    m.get("lab"),
                    m.get("overall"),
                    m.get("status"),
                    m.get("date")
            });
        }
    }

    private void handleDownloadFile() {
        int selectedRow = vDownloadMaterials.getMaterialsTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a file to download.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve the hidden Material ID
        Integer materialId = (Integer) vDownloadMaterials.getMaterialsTableModel().getValueAt(selectedRow, 4);

        // Fetch file details from the Model
        Map<String, Object> fileDetails = mUser.getFileDetails(materialId);
        String filename = (String) fileDetails.get("filename");

        if (filename == null || filename.isEmpty()) {
            JOptionPane.showMessageDialog(null, "File details not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Use JFileChooser to determine the save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(filename));
        int userSelection = fileChooser.showSaveDialog(vDownloadMaterials.getPanelMain());

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Simulate saving the file
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write("--- Teaching Material Content ---\n");
                writer.write("Module: " + vDownloadMaterials.getModuleComboBox().getSelectedItem() + "\n");
                writer.write("Material ID: " + materialId + "\n");
                writer.write("Filename: " + filename + "\n");
                writer.write("File content simulation successful.");

                JOptionPane.showMessageDialog(null,
                        "Successfully downloaded and saved file:\n" + fileToSave.getAbsolutePath(),
                        "Download Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void preSelectModuleForDownload() {
        if (selectedModuleIdForDownload == -1) return;

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) vDownloadMaterials.getModuleComboBox().getModel();
        Map<String, Integer> moduleMap = (Map<String, Integer>) vDownloadMaterials.getModuleComboBox().getClientProperty("moduleMap");

        if (moduleMap == null) return;

        String targetModuleKey = null;

        // Find the module name/code string that matches the stored ID
        for (Map.Entry<String, Integer> entry : moduleMap.entrySet()) {
            if (entry.getValue().equals(selectedModuleIdForDownload)) {
                targetModuleKey = entry.getKey();
                break;
            }
        }

        if (targetModuleKey != null) {
            model.setSelectedItem(targetModuleKey);
            selectedModuleIdForDownload = -1;
        }
    }

    private void loadStudentDecisions() {
        if (activeStudentId == -1) return;

        // Fetch the student's single course name for the header
        List<Map<String, Object>> enrollments = mUser.loadActiveEnrollments(activeStudentId);
        String courseName = enrollments.isEmpty() ? "N/A" : (String) enrollments.get(0).get("course_name");

        vStudentDecisions.setCourseInfo(courseName);

        // Fetch the decision history from the model
        List<Map<String, Object>> decisions = mUser.loadStudentDecisions(activeStudentId);
        DefaultTableModel model = vStudentDecisions.getDecisionTableModel();
        model.setRowCount(0); // Clear old data

        for (Map<String, Object> d : decisions) {
            model.addRow(new Object[]{
                    d.get("decision_id"),
                    d.get("type"),
                    d.get("date"),
                    d.get("manager_id")
            });
        }
    }
    // Lecturer Module Update Logic

    private void loadLecturerModules() {
        if (activeLecturerId == -1) return;

        List<Map<String, Object>> modules = mUser.loadModulesByLecturer(activeLecturerId);

        DefaultTableModel model = vUpdateModuleInfo.getModuleTableModel();
        model.setRowCount(0);
        vUpdateModuleInfo.clearFields();

        for (Map<String, Object> m : modules) {
            model.addRow(new Object[]{
                    m.get("module_id"),
                    m.get("code"),
                    m.get("name"),
                    m.get("credits"),
                    m.get("description")
            });
        }
    }

    private void handleModuleSelectionForUpdate() {
        int selectedRow = vUpdateModuleInfo.getCurrentModulesTable().getSelectedRow();
        if (selectedRow == -1) {
            vUpdateModuleInfo.clearFields();
            return;
        }

        // Get data from the selected row
        Integer moduleId = (Integer) vUpdateModuleInfo.getModuleTableModel().getValueAt(selectedRow, 0);
        String code = (String) vUpdateModuleInfo.getModuleTableModel().getValueAt(selectedRow, 1);
        String name = (String) vUpdateModuleInfo.getModuleTableModel().getValueAt(selectedRow, 2);
        Integer credits = (Integer) vUpdateModuleInfo.getModuleTableModel().getValueAt(selectedRow, 3);
        String description = (String) vUpdateModuleInfo.getModuleTableModel().getValueAt(selectedRow, 4);


        // Populate the edit fields
        vUpdateModuleInfo.getModuleCodeTxt().setText(code);
        vUpdateModuleInfo.getModuleNameTxt().setText(name);
        vUpdateModuleInfo.getCreditTxt().setText(String.valueOf(credits));
        vUpdateModuleInfo.getDescriptionTextArea().setText(description != null ? description : "");
        vUpdateModuleInfo.setSelectedModuleLabel("Editing: " + code + " - " + name + " (ID: " + moduleId + ")");

        vUpdateModuleInfo.getUpdateButton().putClientProperty("moduleId", moduleId);
    }

    private void handleUpdateModuleInfo() {
        Integer moduleId = (Integer) vUpdateModuleInfo.getUpdateButton().getClientProperty("moduleId");
        if (moduleId == null || moduleId == -1) {
            JOptionPane.showMessageDialog(null, "Please select a module to update first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String code = vUpdateModuleInfo.getModuleCodeTxt().getText().trim();
        String name = vUpdateModuleInfo.getModuleNameTxt().getText().trim();
        String creditStr = vUpdateModuleInfo.getCreditTxt().getText().trim();
        String description = vUpdateModuleInfo.getDescriptionTextArea().getText().trim();
        int credit;

        // Validation
        if (code.isEmpty() || name.isEmpty() || creditStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Module Code, Name, and Credit are mandatory fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            credit = Integer.parseInt(creditStr);
            if (credit <= 0) {
                JOptionPane.showMessageDialog(null, "Credit points must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Credit points must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = mUser.updateModuleDetails(moduleId, code, name, credit, description);
        if (success) {
            JOptionPane.showMessageDialog(null, "Module " + code + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadLecturerModules(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update module details. Check the database connection or key constraints.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Lecturer Module Load Helper
    private Map<String, Integer> getLecturerModulesMap() {
        Map<String, Integer> moduleMap = new HashMap<>();
        if (activeLecturerId == -1) return moduleMap;

        List<Map<String, Object>> modules = mUser.loadModulesByLecturer(activeLecturerId);

        for (Map<String, Object> m : modules) {
            String moduleNameCode = m.get("code") + " - " + m.get("name");
            moduleMap.put(moduleNameCode, (Integer) m.get("module_id"));
        }
        return moduleMap;
    }

    private void loadModulesForUpload() {
        Map<String, Integer> moduleMap = getLecturerModulesMap();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        model.addElement("-- Select Module --");

        for (String name : moduleMap.keySet()) {
            model.addElement(name);
        }

        vUploadMaterials.getModuleComboBox().setModel(model);
        vUploadMaterials.getModuleComboBox().putClientProperty("moduleMap", moduleMap);
        vUploadMaterials.clearFields();
    }

    // Handle the upload button click
    private void handleMaterialUpload() {
        String selectedModuleKey = (String) vUploadMaterials.getModuleComboBox().getSelectedItem();
        Map<String, Integer> moduleMap = (Map<String, Integer>) vUploadMaterials.getModuleComboBox().getClientProperty("moduleMap");

        String type = (String) vUploadMaterials.getTypeComboBox().getSelectedItem();
        int week = (Integer) vUploadMaterials.getWeekSpinner().getValue();
        String fileName = vUploadMaterials.getFileNameOnly();

        if (selectedModuleKey == null || selectedModuleKey.startsWith("--")) {
            JOptionPane.showMessageDialog(null, "Please select a module first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fileName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please browse and select a file to upload.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer moduleId = moduleMap.get(selectedModuleKey);

        if (moduleId == null || activeLecturerId == -1) {
            JOptionPane.showMessageDialog(null, "Internal Error: Module or Lecturer ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call Model to insert record
        boolean success = mUser.uploadMaterial(moduleId, activeLecturerId, type, week, fileName);

        if (success) {
            JOptionPane.showMessageDialog(null, "Material '" + fileName + "' uploaded successfully for " + selectedModuleKey + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            vUploadMaterials.clearFields();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to upload material. Database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load modules into the ComboBox for the Enrolled Students View
    private void loadModulesForStudentsView() {
        Map<String, Integer> moduleMap = getLecturerModulesMap();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        model.addElement("-- Select Module --");

        for (String name : moduleMap.keySet()) {
            model.addElement(name);
        }

        vEnrolledStudents.getModuleComboBox().setModel(model);
        vEnrolledStudents.getModuleComboBox().putClientProperty("moduleMap", moduleMap);
        vEnrolledStudents.clearStudentTable();
        vEnrolledStudents.setInfoLabelText("Students currently enrolled in the selected module:");
    }

    // Handle selection in the ComboBox for student list
    private void handleModuleSelectionForStudents() {
        String selectedModule = (String) vEnrolledStudents.getModuleComboBox().getSelectedItem();
        vEnrolledStudents.clearStudentTable();

        if (selectedModule == null || selectedModule.startsWith("--")) return;

        Map<String, Integer> moduleMap = (Map<String, Integer>) vEnrolledStudents.getModuleComboBox().getClientProperty("moduleMap");
        Integer moduleId = moduleMap.get(selectedModule);

        if (moduleId != null) {
            loadEnrolledStudents(moduleId, selectedModule);
        }
    }

    // Load students into the JTable
    private void loadEnrolledStudents(int moduleId, String moduleName) {
        List<Map<String, Object>> students = mUser.loadEnrolledStudentsByModule(moduleId);
        DefaultTableModel model = vEnrolledStudents.getStudentTableModel();
        model.setRowCount(0);

        for (Map<String, Object> s : students) {
            model.addRow(new Object[]{
                    s.get("student_id"), // Hidden ID
                    s.get("firstName"),
                    s.get("surname"),
                    s.get("email"),
                    s.get("enrollmentDate")
            });
        }
        vEnrolledStudents.setInfoLabelText(students.size() + " students found for " + moduleName + ".");
    }

    // Load modules into the ComboBox for the Marks Management View
    private void loadModulesForMarksManagementView() {
        Map<String, Integer> moduleMap = getLecturerModulesMap();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        model.addElement("-- Select Module --");

        for (String name : moduleMap.keySet()) {
            model.addElement(name);
        }

        vMarksManagement.getModuleComboBox().setModel(model);
        vMarksManagement.getModuleComboBox().putClientProperty("moduleMap", moduleMap);
        vMarksManagement.clearTable();
        vMarksManagement.clearMarkFields();
    }

    // Handle module selection in Marks Management
    private void handleModuleSelectionForMarksManagement() {
        String selectedModule = (String) vMarksManagement.getModuleComboBox().getSelectedItem();
        vMarksManagement.clearTable();
        vMarksManagement.clearMarkFields();

        if (selectedModule == null || selectedModule.startsWith("--")) return;

        @SuppressWarnings("unchecked")
        Map<String, Integer> moduleMap = (Map<String, Integer>) vMarksManagement.getModuleComboBox().getClientProperty("moduleMap");
        Integer moduleId = moduleMap.get(selectedModule);

        if (moduleId != null) {
            vMarksManagement.setSelectedModuleId(moduleId);
            vMarksManagement.setSelectedAttemptId(-1);
            vMarksManagement.setSelectedStudentId(-1);

            loadModuleMarksTable(moduleId, selectedModule);
        }
    }

    // Load marks into the JTable
    private void loadModuleMarksTable(int moduleId, String moduleName) {
        List<Map<String, Object>> marks = mUser.loadModuleMarks(moduleId);
        DefaultTableModel model = vMarksManagement.getMarksTableModel();

        this.isTableRefreshing = true;
        model.setRowCount(0);

        int rowToSelect = -1;

        for (int i = 0; i < marks.size(); i++) {
            Map<String, Object> m = marks.get(i);
            model.addRow(new Object[]{
                    m.get("attempt_id"),
                    m.get("student_id"),
                    m.get("firstName"),
                    m.get("surname"),
                    m.get("attempt_number"),
                    m.get("exam_mark"),
                    m.get("lab_mark"),
                    m.get("overall_mark"),
                    m.get("status")
            });
            if (this.lastUpdatedAttemptId != -1 &&
                    m.get("attempt_id") instanceof Integer &&
                    ((Integer) m.get("attempt_id")).intValue() == this.lastUpdatedAttemptId) {

                rowToSelect = i;
            }
        }

        // Reset state and restore selection if found
        JTable table = vMarksManagement.getStudentMarksTable();

        if (rowToSelect != -1) {
            table.setRowSelectionInterval(rowToSelect, rowToSelect);
            handleMarkAttemptSelection();
        } else {
            vMarksManagement.clearMarkFields();
        }

        this.lastUpdatedAttemptId = -1; // Reset temporary state
        this.isTableRefreshing = false;
    }

    // Handle row selection to populate edit fields
    private void handleMarkAttemptSelection() {
        int selectedRow = vMarksManagement.getStudentMarksTable().getSelectedRow();
        if (this.isTableRefreshing) return;
        if (selectedRow == -1) {
            vMarksManagement.clearMarkFields();
            return;
        }
        DefaultTableModel model = vMarksManagement.getMarksTableModel();

        // Retrieve values using safe casting
        Object attemptIdObj = model.getValueAt(selectedRow, 0);
        Object studentIdObj = model.getValueAt(selectedRow, 1);

        // Retrieve non-ID fields
        String firstName = (String) model.getValueAt(selectedRow, 2);
        String surname = (String) model.getValueAt(selectedRow, 3);
        Object attemptNumberObj = model.getValueAt(selectedRow, 4);
        String status = (String) model.getValueAt(selectedRow, 8);

        Integer attemptId = null;
        Integer studentId = null;
        Integer attemptNumber = null;

        try {
            if (attemptIdObj != null && !attemptIdObj.toString().isEmpty()) {
                attemptId = Integer.parseInt(attemptIdObj.toString());
            }
            if (studentIdObj != null && !studentIdObj.toString().isEmpty()) {
                studentId = Integer.parseInt(studentIdObj.toString());
            }
            if (attemptNumberObj != null) {
                attemptNumber = Integer.parseInt(attemptNumberObj.toString());
            }

        } catch (NumberFormatException ex) {
            // Log or show error
            vMarksManagement.clearMarkFields();
            JOptionPane.showMessageDialog(null, "Internal error: Malformed ID in table row.", "Data Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Populate view fields
        vMarksManagement.getExamMarkTxt().setText(model.getValueAt(selectedRow, 5) != null ? model.getValueAt(selectedRow, 5).toString() : "");
        vMarksManagement.getLabMarkTxt().setText(model.getValueAt(selectedRow, 6) != null ? model.getValueAt(selectedRow, 6).toString() : "");
        vMarksManagement.getOverallMarkTxt().setText(model.getValueAt(selectedRow, 7) != null ? model.getValueAt(selectedRow, 7).toString() : "");

        // Set Spinner and Status
        vMarksManagement.getAttemptSpinner().setValue(attemptNumber != null ? attemptNumber : 1);
        vMarksManagement.getStatusComboBox().setSelectedItem(status);

        vMarksManagement.getSelectedStudentLabel().setText("Editing: " + firstName + " " + surname + " (Attempt " + attemptNumber + ")");

        vMarksManagement.setSelectedAttemptId(attemptId != null ? attemptId : -1);
        vMarksManagement.setSelectedStudentId(studentId != null ? studentId : -1);
    }

    // Handle the Update Button
    private void handleMarkAttemptUpdate() {
        int attemptId = vMarksManagement.getSelectedAttemptId();
        int studentId = vMarksManagement.getSelectedStudentId();
        int moduleId = vMarksManagement.getSelectedModuleId();
        JTable table = vMarksManagement.getStudentMarksTable();

        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        if (studentId == -1 || moduleId == -1) {
            JOptionPane.showMessageDialog(null, "Please select a student attempt from the table.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int attemptNumber = (Integer) vMarksManagement.getAttemptSpinner().getValue();
        if (attemptNumber <= 0) {
            JOptionPane.showMessageDialog(null, "The attempt number must be 1 or greater.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer examMark = 0; Integer labMark = 0; Integer overallMark = 0;
        try {
            String examStr = vMarksManagement.getExamMarkTxt().getText().trim();
            if (!examStr.isEmpty()) examMark = Integer.parseInt(examStr);
            String labStr = vMarksManagement.getLabMarkTxt().getText().trim();
            if (!labStr.isEmpty()) labMark = Integer.parseInt(labStr);
            String overallStr = vMarksManagement.getOverallMarkTxt().getText().trim();
            if (!overallStr.isEmpty()) overallMark = Integer.parseInt(overallStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Marks must be valid integers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ((examMark != null && (examMark < 0 || examMark > 100)) ||
                (labMark != null && (labMark < 0 || labMark > 100)) ||
                (overallMark != null && (overallMark < 0 || overallMark > 100))) {
            JOptionPane.showMessageDialog(null, "Marks must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String status = (String) vMarksManagement.getStatusComboBox().getSelectedItem();

        // Call the Model to save the data
        boolean success = mUser.saveModuleAttempt(attemptId, studentId, moduleId, attemptNumber, examMark, labMark, overallMark, status);

        if (success) {
            JOptionPane.showMessageDialog(null, "Marks successfully saved/updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.lastUpdatedAttemptId = (attemptId > 0) ? attemptId : vMarksManagement.getMarksTableModel().getRowCount();
            loadModuleMarksTable(moduleId, (String) vMarksManagement.getModuleComboBox().getSelectedItem());

        } else {
            JOptionPane.showMessageDialog(null, "Failed to save marks. Check database connection or constraints.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadManageAccountsTable() {
        String roleFilter = (String) vManageAccounts.getCmbRole().getSelectedItem();
        String activeFilter = (String) vManageAccounts.getCmbActive().getSelectedItem();

        if (roleFilter != null && roleFilter.equalsIgnoreCase("All")) {
            roleFilter = null;
        }

        Boolean isActive = null;
        if ("Yes".equalsIgnoreCase(activeFilter)) {
            isActive = true;
        } else if ("No".equalsIgnoreCase(activeFilter)) {
            isActive = false;
        }

        List<Map<String, Object>> users = mUser.loadUsersForManagement(roleFilter, isActive);

        DefaultTableModel model = vManageAccounts.getTableModel();
        model.setRowCount(0);

        for (Map<String, Object> u : users) {
            int id = (Integer) u.get("user_id");
            String name = (String) u.get("name");
            String email = (String) u.get("email");
            String role = (String) u.get("role");
            boolean active = (Boolean) u.get("active");

            vManageAccounts.addUserRow(id, name, email, role, active);
        }
    }

    private Integer getSelectedUserIdFromManageAccounts() {
        int viewRow = vManageAccounts.getTblUsers().getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a user first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int modelRow = vManageAccounts.getTblUsers().convertRowIndexToModel(viewRow);
        DefaultTableModel model = vManageAccounts.getTableModel();
        Object value = model.getValueAt(modelRow, 0); // ID column

        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Internal error: invalid user ID in table.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    private void handleActivateUser() {
        Integer userId = getSelectedUserIdFromManageAccounts();
        if (userId == null) return;

        boolean ok = mUser.updateUserAccountStatus(userId, true);
        if (ok) {
            JOptionPane.showMessageDialog(null, "User activated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadManageAccountsTable();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to activate user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeactivateUser() {
        Integer userId = getSelectedUserIdFromManageAccounts();
        if (userId == null) return;

        boolean ok = mUser.updateUserAccountStatus(userId, false);
        if (ok) {
            JOptionPane.showMessageDialog(null, "User deactivated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadManageAccountsTable();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to deactivate user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleResetPasswordForUser() {
        Integer userId = getSelectedUserIdFromManageAccounts();
        if (userId == null) return;
        String newPassword = "password";

        boolean ok = mUser.resetUserPassword(userId, newPassword);
        if (ok) {
            JOptionPane.showMessageDialog(
                    null,
                    "Password reset successfully.\nNew password: " + newPassword,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(null, "Failed to reset password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLecturersAndModulesForAssignment() {
        List<Map<String, Object>> lecturers = mUser.loadAllLecturers();
        DefaultComboBoxModel<String> lectModel = new DefaultComboBoxModel<>();
        Map<String, Integer> lectMap = new HashMap<>();

        lectModel.addElement("-- Select Lecturer --");
        for (Map<String, Object> l : lecturers) {
            Integer id = (Integer) l.get("lecturer_id");
            String name = (String) l.get("name");
            String label = id + " - " + name;
            lectModel.addElement(label);
            lectMap.put(label, id);
        }
        vAssignModuleToLecturer.getCmbLecturer().setModel(lectModel);
        vAssignModuleToLecturer.getCmbLecturer().putClientProperty("lecturerMap", lectMap);

        List<Map<String, Object>> modules = mUser.loadAllModules();
        DefaultComboBoxModel<String> modModel = new DefaultComboBoxModel<>();
        Map<String, Integer> modMap = new HashMap<>();

        modModel.addElement("-- Select Module --");
        for (Map<String, Object> m : modules) {
            Integer id = (Integer) m.get("module_id");
            String code = (String) m.get("code");
            String name = (String) m.get("name");
            String label = code + " - " + name;
            modModel.addElement(label);
            modMap.put(label, id);
        }
        vAssignModuleToLecturer.getCmbModule().setModel(modModel);
        vAssignModuleToLecturer.getCmbModule().putClientProperty("moduleMap", modMap);
    }

    private void loadModuleAssignmentsTable() {
        List<Map<String, Object>> rows = mUser.loadModuleAssignments();
        DefaultTableModel model = vAssignModuleToLecturer.getModel();
        model.setRowCount(0);

        for (Map<String, Object> r : rows) {
            Integer moduleId = (Integer) r.get("module_id");
            String code      = (String) r.get("code");
            String name      = (String) r.get("name");
            Integer lecturerId = (Integer) r.get("lecturer_id");
            String lecturerName = (String) r.get("lecturer_name");

            vAssignModuleToLecturer.addRow(
                    moduleId,
                    code,
                    name,
                    lecturerId,
                    lecturerName
            );
        }
    }

    private void handleAssignModuleToLecturer() {
        JComboBox<String> cmbLecturer = vAssignModuleToLecturer.getCmbLecturer();
        JComboBox<String> cmbModule   = vAssignModuleToLecturer.getCmbModule();

        String lecturerKey = (String) cmbLecturer.getSelectedItem();
        String moduleKey   = (String) cmbModule.getSelectedItem();

        if (lecturerKey == null || lecturerKey.startsWith("--")
                || moduleKey == null || moduleKey.startsWith("--")) {
            JOptionPane.showMessageDialog(null,
                    "Please select both a lecturer and a module.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Integer> lectMap = (Map<String, Integer>) cmbLecturer.getClientProperty("lecturerMap");
        Map<String, Integer> modMap  = (Map<String, Integer>) cmbModule.getClientProperty("moduleMap");

        if (lectMap == null || modMap == null) {
            JOptionPane.showMessageDialog(null,
                    "Internal error: mapping not found. Please reopen this screen.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer lecturerId = lectMap.get(lecturerKey);
        Integer moduleId   = modMap.get(moduleKey);

        if (lecturerId == null || moduleId == null) {
            JOptionPane.showMessageDialog(null,
                    "Internal error: could not resolve selected IDs.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = mUser.assignModuleToLecturer(moduleId, lecturerId);
        if (success) {
            JOptionPane.showMessageDialog(null,
                    "Module assigned successfully:\n" +
                            moduleKey + " -> " + lecturerKey,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadModuleAssignmentsTable();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Failed to assign module. Check database connection / constraints.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudentDecisionDropdowns() {
        // Load Students
        List<Map<String, Object>> students = mUser.loadAllStudents();
        DefaultComboBoxModel<String> studModel = new DefaultComboBoxModel<>();
        Map<String, Integer> studentMap = new HashMap<>();

        studModel.addElement("-- Select Student --");

        for (Map<String, Object> s : students) {
            int id = (Integer) s.get("student_id");
            String name = (String) s.get("name");
            String label = id + " - " + name;

            studModel.addElement(label);
            studentMap.put(label, id);
        }

        vIssueStudentDecision.getCmbStudent().setModel(studModel);
        vIssueStudentDecision.getCmbStudent().putClientProperty("studentMap", studentMap);


        // Static decisions list
        DefaultComboBoxModel<String> decisionModel = new DefaultComboBoxModel<>();
        decisionModel.addElement("-- Select Decision --");
        decisionModel.addElement("award");
        decisionModel.addElement("resit");
        decisionModel.addElement("withdraw");

        vIssueStudentDecision.getCmbDecision().setModel(decisionModel);
    }

    private void loadStudentDecisionTable() {
        vIssueStudentDecision.clearTable();

        List<Map<String, Object>> list = mUser.loadAllStudentDecisions();

        for (Map<String, Object> row : list) {
            vIssueStudentDecision.addRow(
                    (Integer) row.get("id"),
                    (String) row.get("student"),
                    (String) row.get("decision"),
                    (String) row.get("date"),
                    (String) row.get("manager")
            );
        }
    }

    private void loadIssueDecisionTable() {
        DefaultTableModel model = vIssueStudentDecision.getTableModel();
        model.setRowCount(0);

        List<Map<String, Object>> decisions = mUser.loadAllStudentDecisions();

        for (Map<String, Object> d : decisions) {
            vIssueStudentDecision.addRow(
                    (Integer) d.get("id"),
                    (String)  d.get("student"),
                    (String)  d.get("decision"),
                    (String)  d.get("date"),
                    (String)  d.get("manager")
            );
        }
    }



    private void loadCoursesForEnrolment() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Map<String, Integer> courseMap = new HashMap<>();

        model.addElement("-- Select Course --");

        List<Map<String, Object>> courses = mUser.loadAllCourses();
        for (Map<String, Object> c : courses) {
            String label = c.get("code") + " - " + c.get("name");
            model.addElement(label);
            courseMap.put(label, (Integer) c.get("course_id"));
        }

        vEnrolStudentsCourse.getCmbCourse().setModel(model);
        vEnrolStudentsCourse.getCmbCourse().putClientProperty("courseMap", courseMap);
        vEnrolStudentsCourse.clearLists();
    }

    private void handleCourseSelectionForEnrolment() {
        String selected = (String) vEnrolStudentsCourse.getCmbCourse().getSelectedItem();
        vEnrolStudentsCourse.clearLists();

        if (selected == null || selected.startsWith("--")) return;

        @SuppressWarnings("unchecked")
        Map<String, Integer> courseMap =
                (Map<String, Integer>) vEnrolStudentsCourse.getCmbCourse().getClientProperty("courseMap");
        if (courseMap == null) return;

        Integer courseId = courseMap.get(selected);
        if (courseId == null) return;

        loadStudentsForCourse(courseId);
    }

    private void loadStudentsForCourse(int courseId) {
        DefaultListModel<String> availModel = vEnrolStudentsCourse.getAvailableModel();
        DefaultListModel<String> enModel    = vEnrolStudentsCourse.getEnrolledModel();
        availModel.clear();
        enModel.clear();

        List<Map<String, Object>> available = mUser.loadAvailableStudentsForCourse(courseId);
        List<Map<String, Object>> enrolled  = mUser.loadEnrolledStudentsForCourse(courseId);

        Map<String, Integer> availableMap = new HashMap<>();
        Map<String, Integer> enrolledMap  = new HashMap<>();

        for (Map<String, Object> row : available) {
            Integer sid = (Integer) row.get("student_id");
            String name = (String) row.get("name");
            String label = name + " (ID: " + sid + ")";
            availModel.addElement(label);
            availableMap.put(label, sid);
        }

        for (Map<String, Object> row : enrolled) {
            Integer sid = (Integer) row.get("student_id");
            String name = (String) row.get("name");
            String label = name + " (ID: " + sid + ")";
            enModel.addElement(label);
            enrolledMap.put(label, sid);
        }

        vEnrolStudentsCourse.getListAvailable().putClientProperty("studentMap", availableMap);
        vEnrolStudentsCourse.getListEnrolled().putClientProperty("studentMap", enrolledMap);
    }

    private void handleEnrollStudentToCourse() {
        String selectedCourse = (String) vEnrolStudentsCourse.getCmbCourse().getSelectedItem();
        if (selectedCourse == null || selectedCourse.startsWith("--")) {
            JOptionPane.showMessageDialog(null, "Please select a course first.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> courseMap =
                (Map<String, Integer>) vEnrolStudentsCourse.getCmbCourse().getClientProperty("courseMap");
        if (courseMap == null) return;

        Integer courseId = courseMap.get(selectedCourse);
        if (courseId == null) return;

        String studentLabel = vEnrolStudentsCourse.getListAvailable().getSelectedValue();
        if (studentLabel == null) {
            JOptionPane.showMessageDialog(null, "Please select a student to enrol.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> availableMap =
                (Map<String, Integer>) vEnrolStudentsCourse.getListAvailable().getClientProperty("studentMap");
        if (availableMap == null) return;

        Integer studentId = availableMap.get(studentLabel);
        if (studentId == null) return;

        boolean ok = mUser.enrolStudentInCourse(studentId, courseId);
        if (ok) {
            JOptionPane.showMessageDialog(null, "Student enrolled successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudentsForCourse(courseId);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to enrol student (database error).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUnenrolStudentFromCourse() {
        String selectedCourse = (String) vEnrolStudentsCourse.getCmbCourse().getSelectedItem();
        if (selectedCourse == null || selectedCourse.startsWith("--")) {
            JOptionPane.showMessageDialog(null, "Please select a course first.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> courseMap =
                (Map<String, Integer>) vEnrolStudentsCourse.getCmbCourse().getClientProperty("courseMap");
        if (courseMap == null) return;

        Integer courseId = courseMap.get(selectedCourse);
        if (courseId == null) return;

        String studentLabel = vEnrolStudentsCourse.getListEnrolled().getSelectedValue();
        if (studentLabel == null) {
            JOptionPane.showMessageDialog(null, "Please select a student to unenrol.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> enrolledMap =
                (Map<String, Integer>) vEnrolStudentsCourse.getListEnrolled().getClientProperty("studentMap");
        if (enrolledMap == null) return;

        Integer studentId = enrolledMap.get(studentLabel);
        if (studentId == null) return;

        boolean ok = mUser.unenrolStudentFromCourse(studentId, courseId);
        if (ok) {
            JOptionPane.showMessageDialog(null, "Student unenrolled successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudentsForCourse(courseId);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to unenrol student (database error).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Display Module Details

    private void loadModuleDetails(String searchTerm) {
        List<Map<String, Object>> modules = mUser.loadAllModuleDetails(searchTerm);
        DefaultTableModel model = vDisplayModuleDetails.getModel();
        model.setRowCount(0);

        for (Map<String, Object> m : modules) {
            String code = (String) m.get("code");
            String name = (String) m.get("name");
            int credits = (Integer) m.get("credits");
            model.addRow(new Object[]{code, name, credits});
        }
    }

    private void handleSearchModuleDetails() {
        String term = vDisplayModuleDetails.getTxtSearch().getText();
        if (term != null) {
            term = term.trim();
        }

        if (term == null || term.isEmpty()) {
            loadModuleDetails(null);
        } else {
            loadModuleDetails(term);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCoursesForUpdateCourseInfo() {
        List<Map<String, Object>> courses = mUser.loadAllCourses();

        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
        comboModel.addElement("-- Select Course --");

        Map<String, Integer> courseMap = new HashMap<>();
        Map<Integer, String> descMap = new HashMap<>();

        for (Map<String, Object> c : courses) {
            Integer courseId = (Integer) c.get("course_id");
            String code = (String) c.get("code");
            String name = (String) c.get("name");
            String desc = (String) c.get("description");

            String label = code + " - " + name;
            comboModel.addElement(label);
            courseMap.put(label, courseId);
            descMap.put(courseId, desc != null ? desc : "");
        }

        JComboBox<String> cmb = vUpdateCourseInfo.getCmbCourse();
        cmb.setModel(comboModel);
        cmb.putClientProperty("courseMap", courseMap);
        cmb.putClientProperty("courseDescMap", descMap);

        vUpdateCourseInfo.getTxtDescription().setText("");
        vUpdateCourseInfo.clearTable();
    }

    @SuppressWarnings("unchecked")
    private void handleCourseSelectionForUpdateCourseInfo() {
        JComboBox<String> cmb = vUpdateCourseInfo.getCmbCourse();
        String selected = (String) cmb.getSelectedItem();

        if (selected == null || selected.startsWith("--")) {
            vUpdateCourseInfo.getTxtDescription().setText("");
            vUpdateCourseInfo.clearTable();
            return;
        }

        Map<String, Integer> courseMap = (Map<String, Integer>) cmb.getClientProperty("courseMap");
        Map<Integer, String> descMap = (Map<Integer, String>) cmb.getClientProperty("courseDescMap");

        Integer courseId = courseMap.get(selected);
        if (courseId == null) {
            vUpdateCourseInfo.getTxtDescription().setText("");
            vUpdateCourseInfo.clearTable();
            return;
        }

        // Set description
        String desc = descMap.getOrDefault(courseId, "");
        vUpdateCourseInfo.getTxtDescription().setText(desc);

        // Load modules for this course into the table
        List<Map<String, Object>> modules = mUser.loadModulesByCourse(courseId);
        vUpdateCourseInfo.clearTable();

        for (Map<String, Object> m : modules) {
            int moduleId = (Integer) m.get("module_id");
            String code = (String) m.get("code");
            String name = (String) m.get("name");
            int credits = (Integer) m.get("credits");
            vUpdateCourseInfo.addRow(moduleId, code, name, credits);
        }

        // Keep selected courseId on the combo for later use
        cmb.putClientProperty("selectedCourseId", courseId);
    }

    @SuppressWarnings("unchecked")
    private void handleSaveCourseDescription() {
        JComboBox<String> cmb = vUpdateCourseInfo.getCmbCourse();
        Integer courseId = (Integer) cmb.getClientProperty("selectedCourseId");

        if (courseId == null || courseId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Please select a course first.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String desc = vUpdateCourseInfo.getTxtDescription().getText().trim();

        boolean ok = mUser.updateCourseDescription(courseId, desc);
        if (ok) {
            JOptionPane.showMessageDialog(null,
                    "Course description updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Also update the cached description map
            Map<Integer, String> descMap =
                    (Map<Integer, String>) cmb.getClientProperty("courseDescMap");
            if (descMap != null) {
                descMap.put(courseId, desc);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Failed to update course description (database error).",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleSaveModuleCredits() {
        JComboBox<String> cmb = vUpdateCourseInfo.getCmbCourse();
        Integer courseId = (Integer) cmb.getClientProperty("selectedCourseId");
        JTable table = vUpdateCourseInfo.getTblModules();

        if (courseId == null || courseId <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Please select a course first.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = vUpdateCourseInfo.getModel();
        int rowCount = model.getRowCount();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(null,
                    "No modules to update for this course.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        boolean allOk = true;

        for (int r = 0; r < rowCount; r++) {
            Integer moduleId = (Integer) model.getValueAt(r, 0);
            Object creditsObj = model.getValueAt(r, 3);
            int credits;

            // Safely parse the value
            try {
                if (creditsObj instanceof Integer) {
                    credits = (Integer) creditsObj;
                } else {
                    credits = Integer.parseInt(creditsObj.toString().trim());
                }
            } catch (NumberFormatException ex) {
                allOk = false;
                continue;
            }

            if (moduleId == null) continue;

            boolean ok = mUser.updateModuleCredit(moduleId, credits);
            if (!ok) {
                allOk = false;
            }
        }

        if (allOk) {
            JOptionPane.showMessageDialog(null,
                    "Module credits updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            handleCourseSelectionForUpdateCourseInfo();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Some module credits could not be updated (database error).",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add Course
    private void loadCoursesForAddCourse() {
        java.util.List<java.util.Map<String, Object>> courses = mUser.loadAllCourses(); // Fetches Course data
        javax.swing.table.DefaultTableModel model = vAddCourse.getCourseModel();
        model.setRowCount(0);

        for (Map<String, Object> c : courses) {
            Integer id = (Integer) c.get("course_id");
            String code = (String) c.get("code");
            String name = (String) c.get("name");
            String desc = (String) c.get("description");
            vAddCourse.addCourseRow(id != null ? id : -1,
                    code != null ? code : "",
                    name != null ? name : "",
                    desc != null ? desc : "");
        }
    }


    private void loadModulesForAddModule() {
        List<Map<String, Object>> modules = mUser.loadAllModules();
        DefaultTableModel model = vAddCourse.getModuleModel();
        model.setRowCount(0);

        for (Map<String, Object> m : modules) {
            Integer id = (Integer) m.get("module_id");
            String code = (String) m.get("code");
            String name = (String) m.get("name");
            Integer credit = (Integer) m.get("credit");

            vAddCourse.addModuleRow(id != null ? id : -1,
                    code != null ? code : "",
                    name != null ? name : "",
                    credit != null ? credit : 0);
        }
    }



    private void loadCoursesForDisplayDetails() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Map<String, Integer> courseMap = new HashMap<>();

        model.addElement("-- Select Course --");

        List<Map<String, Object>> courses = mUser.loadAllCourses();
        for (Map<String, Object> c : courses) {
            String label = c.get("code") + " - " + c.get("name");
            model.addElement(label);
            courseMap.put(label, (Integer) c.get("course_id"));
        }

        vDisplayCourseDetails.getCmbCourse().setModel(model);
        vDisplayCourseDetails.getCmbCourse().putClientProperty("courseMap", courseMap);
    }

    private void handleDisplayCourseDetails() {
        String selected = (String) vDisplayCourseDetails.getCmbCourse().getSelectedItem();
        vDisplayCourseDetails.clearTable();

        if (selected == null || selected.startsWith("--")) {
            JOptionPane.showMessageDialog(null, "Please select a course to display.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> courseMap = (Map<String, Integer>) vDisplayCourseDetails.getCmbCourse().getClientProperty("courseMap");
        Integer courseId = courseMap.get(selected);

        if (courseId == null) {
            JOptionPane.showMessageDialog(null, "Internal error: Course ID not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Object> courseDetails = mUser.loadCourseDetails(courseId);

        if (courseDetails.isEmpty()) {
            vDisplayCourseDetails.getCourseInfoLabel().setText("Course not found in database.");
            return;
        }

        // Update Header Info
        String courseCode = (String) courseDetails.get("code");
        String courseName = (String) courseDetails.get("name");
        String description = (String) courseDetails.get("description");
        vDisplayCourseDetails.setCourseInfo(courseCode, courseName, description);

        //Populate Modules Table
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> modules = (List<Map<String, Object>>) courseDetails.get("modules");

        if (modules != null && !modules.isEmpty()) {
            for (Map<String, Object> m : modules) {
                vDisplayCourseDetails.addModuleRow(
                        (String) m.get("code"),
                        (String) m.get("name"),
                        (Integer) m.get("credits"),
                        (String) m.get("semester")
                );
            }
        } else {
            JOptionPane.showMessageDialog(null, "No modules found for this course.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadCoursesAndModulesForCourseAssignment() {
        List<Map<String, Object>> courses = mUser.loadAllCourses();
        DefaultComboBoxModel<String> courseModel = new DefaultComboBoxModel<>();
        Map<String, Integer> courseMap = new HashMap<>();

        courseModel.addElement("-- Select Course --");
        for (Map<String, Object> c : courses) {
            Integer id = (Integer) c.get("course_id");
            String code = (String) c.get("code");
            String name = (String) c.get("name");
            String label = code + " - " + name;
            courseModel.addElement(label);
            courseMap.put(label, id);
        }
        vAssignModuleToCourse.getCmbCourse().setModel(courseModel);
        vAssignModuleToCourse.getCmbCourse().putClientProperty("courseMap", courseMap);

        List<Map<String, Object>> modules = mUser.loadAllModules();
        DefaultComboBoxModel<String> moduleModel = new DefaultComboBoxModel<>();
        Map<String, Integer> moduleMap = new HashMap<>();

        moduleModel.addElement("-- Select Module --");
        for (Map<String, Object> m : modules) {
            Integer id = (Integer) m.get("module_id");
            String code = (String) m.get("code");
            String name = (String) m.get("name");
            String label = code + " - " + name;
            moduleModel.addElement(label);
            moduleMap.put(label, id);
        }
        vAssignModuleToCourse.getCmbModule().setModel(moduleModel);
        vAssignModuleToCourse.getCmbModule().putClientProperty("moduleMap", moduleMap);
    }

    private void loadCourseModuleAssignmentsTable() {
        List<Map<String, Object>> rows = mUser.loadCourseModuleAssignments();
        DefaultTableModel model = vAssignModuleToCourse.getModel();
        model.setRowCount(0);

        for (Map<String, Object> r : rows) {
            vAssignModuleToCourse.addRow(
                    (String) r.get("course_code"),
                    (String) r.get("course_name"),
                    (String) r.get("module_code"),
                    (String) r.get("module_name"),
                    (String) r.get("semester")
            );
        }
    }

    private void handleAssignModuleToCourse() {
        JComboBox<String> cmbCourse = vAssignModuleToCourse.getCmbCourse();
        JComboBox<String> cmbModule = vAssignModuleToCourse.getCmbModule();
        String semester = (String) vAssignModuleToCourse.getCmbSemester().getSelectedItem();

        String courseKey = (String) cmbCourse.getSelectedItem();
        String moduleKey = (String) cmbModule.getSelectedItem();

        if (courseKey == null || courseKey.startsWith("--")
                || moduleKey == null || moduleKey.startsWith("--")) {
            vAssignModuleToCourse.showMessage("Please select both a course and a module.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> courseMap = (Map<String, Integer>) cmbCourse.getClientProperty("courseMap");
        @SuppressWarnings("unchecked")
        Map<String, Integer> moduleMap = (Map<String, Integer>) cmbModule.getClientProperty("moduleMap");

        if (courseMap == null || moduleMap == null) {
            vAssignModuleToCourse.showMessage("Internal error: mapping not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer courseId = courseMap.get(courseKey);
        Integer moduleId = moduleMap.get(moduleKey);

        if (courseId == null || moduleId == null) {
            vAssignModuleToCourse.showMessage("Internal error: could not resolve selected IDs.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = mUser.assignModuleToCourse(courseId, moduleId, semester);

        if (success) {
            vAssignModuleToCourse.showMessage("Module assigned successfully to course (Semester " + semester + ").",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCourseModuleAssignmentsTable(); // Refresh the table
        } else {
            vAssignModuleToCourse.showMessage("Failed to assign module. Check database connection / constraints.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRuleTargets(String scope) {
        JComboBox<String> cmbTarget = vAddBusinessRule.getCmbTarget();
        cmbTarget.removeAllItems();
        cmbTarget.setEnabled(false);
        vAddBusinessRule.getLblTarget().setText("Target (N/A):");

        if (scope == null || scope.equalsIgnoreCase("global")) {
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Map<String, Integer> map = new HashMap<>();

        if (scope.equalsIgnoreCase("course_specific")) {
            List<Map<String, Object>> courses = mUser.loadAllCourses();
            model.addElement("-- Select Course --");
            vAddBusinessRule.getLblTarget().setText("Target (Course):");
            for (Map<String, Object> c : courses) {
                String label = c.get("code") + " - " + c.get("name");
                model.addElement(label);
                map.put(label, (Integer) c.get("course_id"));
            }
        } else if (scope.equalsIgnoreCase("module_specific")) {
            List<Map<String, Object>> modules = mUser.loadAllModules();
            model.addElement("-- Select Module --");
            vAddBusinessRule.getLblTarget().setText("Target (Module):");
            for (Map<String, Object> m : modules) {
                String label = m.get("code") + " - " + m.get("name");
                model.addElement(label);
                map.put(label, (Integer) m.get("module_id"));
            }
        }

        cmbTarget.setModel(model);
        cmbTarget.putClientProperty("targetMap", map);
        cmbTarget.setEnabled(true);
    }

    private void handleRuleScopeChange() {
        String scope = (String) vAddBusinessRule.getCmbScope().getSelectedItem();
        loadRuleTargets(scope);
    }

    private void loadAllRules() {
        vAddBusinessRule.clearTable();
        List<Map<String, Object>> rules = mUser.loadAllRules();

        for (Map<String, Object> r : rules) {
            vAddBusinessRule.addRuleRow(
                    (Integer) r.get("rule_id"),
                    (String) r.get("rule_type"),
                    (Integer) r.get("rule_value"),
                    (String) r.get("scope"),
                    (String) r.get("target_name")
            );
        }
    }


    private void handleAddBusinessRule() {
        String ruleType = (String) vAddBusinessRule.getCmbRuleType().getSelectedItem();
        String scope = (String) vAddBusinessRule.getCmbScope().getSelectedItem();
        String valueStr = vAddBusinessRule.getTxtValue().getText().trim();
        Integer ruleValue;
        Integer targetId = null;

        if (ruleType == null || ruleType.startsWith("--") || valueStr.isEmpty()) {
            vAddBusinessRule.showMessage("Please select a rule type and enter a value.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ruleValue = Integer.parseInt(valueStr);
            if (ruleValue < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            vAddBusinessRule.showMessage("Rule Value must be a non-negative integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (scope.equalsIgnoreCase("course_specific") || scope.equalsIgnoreCase("module_specific")) {
            String targetKey = (String) vAddBusinessRule.getCmbTarget().getSelectedItem();

            if (targetKey == null || targetKey.startsWith("--")) {
                vAddBusinessRule.showMessage("Please select a valid target for the specified scope.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            @SuppressWarnings("unchecked")
            Map<String, Integer> targetMap = (Map<String, Integer>) vAddBusinessRule.getCmbTarget().getClientProperty("targetMap");
            targetId = targetMap.get(targetKey);

            if (targetId == null) {
                vAddBusinessRule.showMessage("Internal error: Could not resolve target ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        boolean success = mUser.addBusinessRule(ruleType, ruleValue, scope, targetId);

        if (success) {
            vAddBusinessRule.showMessage("Business Rule added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            vAddBusinessRule.clearInputs();
            loadAllRules();
        } else {
            vAddBusinessRule.showMessage("Failed to add rule (Database or Validation Error).", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleAddCourseOrModule() {
        if (vAddCourse.getRbAddCourse().isSelected()) {
            handleAddCourse();
        } else {
            handleAddModule();
        }
    }

    private void handleAddModule() {
        String code = vAddCourse.getTxtCode().getText().trim();
        String name = vAddCourse.getTxtName().getText().trim();
        String creditStr = vAddCourse.getTxtCredit().getText().trim();
        int credit;

        if (code.isEmpty() || name.isEmpty() || creditStr.isEmpty()) {
            vAddCourse.showMessage("Module code, name, and credit are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            credit = Integer.parseInt(creditStr);
            if (credit <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            vAddCourse.showMessage("Credit must be a positive integer.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = mUser.addModule(code, name, credit);
        if (ok) {
            vAddCourse.showMessage("Module added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            vAddCourse.clearInputs();
            loadModulesForAddModule(); // Refresh table
        } else {
            vAddCourse.showMessage("Failed to add module (maybe duplicate code or database error).",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddCourse() {
        String code = vAddCourse.getTxtCode().getText().trim();
        String name = vAddCourse.getTxtName().getText().trim();
        String desc = vAddCourse.getTxtDescription().getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            vAddCourse.showMessage(
                    "Course code and name are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = mUser.addCourse(code, name, desc);
        if (ok) {
            vAddCourse.showMessage(
                    "Course added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            vAddCourse.clearInputs();
            loadCoursesForAddCourse();
        } else {
            vAddCourse.showMessage(
                    "Failed to add course (maybe duplicate code or database error).",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleIssueDecision() {
        JComboBox<String> cmbStudent  = vIssueStudentDecision.getCmbStudent();
        JComboBox<String> cmbDecision = vIssueStudentDecision.getCmbDecision();

        String studentItem = (String) cmbStudent.getSelectedItem();
        String decision    = (String) cmbDecision.getSelectedItem();

        if (studentItem == null || studentItem.startsWith("--")
                || decision == null || decision.startsWith("--")) { // Check decision selection
            JOptionPane.showMessageDialog(null, "Please select both a student and a decision.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> studentMap =
                (Map<String, Integer>) vIssueStudentDecision.getCmbStudent().getClientProperty("studentMap");

        Integer studentId;

        // Extract Student ID
        if (studentMap != null) {
            studentId = studentMap.get(studentItem);
        } else {
            studentId = Integer.parseInt(studentItem.split(" - ")[0]);
        }

        if (studentId == null) {
            JOptionPane.showMessageDialog(null, "Internal error: Could not resolve student ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = mUser.issueStudentDecision(studentId, activeManagerId, decision);

        if (success) {
            JOptionPane.showMessageDialog(null, "Decision '" + decision + "' issued successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadIssueDecisionTable(); // reload history
        } else {
            JOptionPane.showMessageDialog(null, "Failed to issue decision (DB error).",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Toggles the GUI mode and loads data
    private void handleModeToggle(boolean isCourseMode) {
        vAddCourse.switchMode(isCourseMode);
        if (isCourseMode) {
            loadCoursesForAddCourse();
        } else {
            loadModulesForAddModule();
        }
    }

    private void showLoginGUI() {
        setAllViewsInvisible();
        vLogin.getPanelMain().setVisible(true);
        forceRedraw();
    }

    // Switch to the signup view
    private void showSignupGUI() {
        setAllViewsInvisible();
        vSignup.getPanelMain().setVisible(true);
        forceRedraw();
    }

    private void showApproveUsersGUI() {
        setAllViewsInvisible();
        refreshPendingUsersList();
        vApprove.getPanelMain().setVisible(true);
        forceRedraw();
    }

    private void showUpdatePasswordGUI() {
        setAllViewsInvisible();
        vUpdate.getPanelMain().setVisible(true);
        forceRedraw();
    }


    // Student Dashboard Navigation Methods

    private void showStudentDashboardGUI() {
        setAllViewsInvisible();
        vDashboard.setWelcomeMessage(activeUserFirstName);
        vDashboard.getPanelMain().setVisible(true);
        forceRedraw();
    }

    private void showCourseModuleInformationGUI() {
        setAllViewsInvisible();
        vCourseModuleInformation.getPanelMain().setVisible(true);
        loadCourseAndModules();
        forceRedraw();
    }

    private void showDownloadMaterialsGUI() {
        setAllViewsInvisible();
        vDownloadMaterials.getPanelMain().setVisible(true);
        loadDownloadModules();
        preSelectModuleForDownload();
        forceRedraw();
    }

    private void showMarksProgressGUI() {
        setAllViewsInvisible();
        vMarksProgress.getPanelMain().setVisible(true);
        loadMarksHistory();
        forceRedraw();
    }

    private void showStudentDecisionsGUI() {
        setAllViewsInvisible();
        vStudentDecisions.getPanelMain().setVisible(true);
        loadStudentDecisions();
        forceRedraw();
    }

    // Lecturer GUIS

    private void showLecturerDashboardGUI() {
        setAllViewsInvisible();
        vLecturerDashboard.setWelcomeMessage(activeUserFirstName);
        vLecturerDashboard.getPanelMain().setVisible(true);
        forceRedraw();
    }
    private void showUpdateModuleInfoGUI() {
        setAllViewsInvisible();
        vUpdateModuleInfo.getPanelMain().setVisible(true);
        loadLecturerModules();
        forceRedraw();
    }

    private void showUploadMaterialsGUI() {
        setAllViewsInvisible();
        vUploadMaterials.getPanelMain().setVisible(true);
        loadModulesForUpload();
        forceRedraw();
    }

    private void showEnrolledStudentsGUI() {
        setAllViewsInvisible();
        vEnrolledStudents.getPanelMain().setVisible(true);
        loadModulesForStudentsView();
        forceRedraw();
    }

    private void showMarksManagementGUI() {
        setAllViewsInvisible();
        vMarksManagement.getPanelMain().setVisible(true);
        loadModulesForMarksManagementView();
        forceRedraw();
    }

    // Manager GUIS

    private void showManagerDashboardGUI() {
        setAllViewsInvisible();
        vManagerDashboard.setWelcomeMessage(activeUserFirstName);
        vManagerDashboard.getPanelMain().setVisible(true);
        forceRedraw();
    }


    private void showManageAccountsGUI() {
        setAllViewsInvisible();
        vManageAccounts.getPanelMain().setVisible(true);
        loadManageAccountsTable();
        forceRedraw();
    }

    private void showAssignModuleToLecturerGUI() {
        setAllViewsInvisible();
        vAssignModuleToLecturer.getPanelMain().setVisible(true);
        loadLecturersAndModulesForAssignment();
        loadModuleAssignmentsTable();
        forceRedraw();
    }

    private void showIssueStudentDecisionGUI() {
        setAllViewsInvisible();
        vIssueStudentDecision.getPanelMain().setVisible(true);
        loadStudentDecisionDropdowns();
        loadStudentDecisionTable();
        forceRedraw();
    }

    private void showEnrolStudentsCourseGUI() {
        setAllViewsInvisible();
        vEnrolStudentsCourse.getPanelMain().setVisible(true);
        loadCoursesForEnrolment();
        forceRedraw();
    }

    private void showDisplayCourseDetailsGUI() {
        setAllViewsInvisible();
        vDisplayCourseDetails.getPanelMain().setVisible(true);
        loadCoursesForDisplayDetails();
        vDisplayCourseDetails.clearView();
        forceRedraw();
    }

    private void showUpdateCourseInfoGUI() {
        setAllViewsInvisible();
        vUpdateCourseInfo.getPanelMain().setVisible(true);
        loadCoursesForUpdateCourseInfo();
        forceRedraw();
    }

    private void showAddCourseGUI() {
        setAllViewsInvisible();
        vAddCourse.getPanelMain().setVisible(true);
        vAddCourse.getRbAddCourse().setSelected(true);
        handleModeToggle(true);
        forceRedraw();
    }

    private void showAssignModuleToCourseGUI() {
        setAllViewsInvisible();
        vAssignModuleToCourse.getPanelMain().setVisible(true);
        loadCoursesAndModulesForCourseAssignment();
        loadCourseModuleAssignmentsTable();
        forceRedraw();
    }



    private void forceRedraw() {
        Component ancestor = vLogin.getPanelMain().getTopLevelAncestor();
        if (ancestor instanceof JFrame) {
            ancestor.revalidate();
            ancestor.repaint();
        }
    }

    private void showDisplayModuleDetailsGUI() {
        setAllViewsInvisible();
        vDisplayModuleDetails.getPanelMain().setVisible(true);
        vDisplayModuleDetails.getTxtSearch().setText("");
        loadModuleDetails(null);
        forceRedraw();
    }

    private void showAddBusinessRuleGUI() {
        setAllViewsInvisible();
        vAddBusinessRule.getPanelMain().setVisible(true);
        vAddBusinessRule.clearInputs();
        loadAllRules();
        loadRuleTargets("global");
        forceRedraw();
    }

    private void setAllViewsInvisible() {
        vSignup.getPanelMain().setVisible(false);
        vLogin.getPanelMain().setVisible(false);
        vApprove.getPanelMain().setVisible(false);
        vUpdate.getPanelMain().setVisible(false);
        vDashboard.getPanelMain().setVisible(false);
        vCourseModuleInformation.getPanelMain().setVisible(false);
        vDownloadMaterials.getPanelMain().setVisible(false);
        vMarksProgress.getPanelMain().setVisible(false);

        vLecturerDashboard.getPanelMain().setVisible(false);
        vUpdateModuleInfo.getPanelMain().setVisible(false);
        vUploadMaterials.getPanelMain().setVisible(false);
        vEnrolledStudents.getPanelMain().setVisible(false);
        vMarksManagement.getPanelMain().setVisible(false);

        vStudentDecisions.getPanelMain().setVisible(false);
        vManagerDashboard.getPanelMain().setVisible(false);
        vManageAccounts.getPanelMain().setVisible(false);
        vAssignModuleToLecturer.getPanelMain().setVisible(false);
        vIssueStudentDecision.getPanelMain().setVisible(false);
        vEnrolStudentsCourse.getPanelMain().setVisible(false);
        vDisplayModuleDetails.getPanelMain().setVisible(false);
        vUpdateCourseInfo.getPanelMain().setVisible(false);
        vAddCourse.getPanelMain().setVisible(false);
        vDisplayCourseDetails.getPanelMain().setVisible(false);
        vAssignModuleToCourse.getPanelMain().setVisible(false);
        vAddBusinessRule.getPanelMain().setVisible(false);
    }

    private void showPlaceholder(String featureName) {
        JOptionPane.showMessageDialog(null,
                "Navigating to: " + featureName + " screen. (GUI not yet built)",
                "Navigation Placeholder", JOptionPane.INFORMATION_MESSAGE);
    }
}