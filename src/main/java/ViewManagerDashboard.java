import javax.swing.*;
import java.awt.*;

public class ViewManagerDashboard extends JFrame {

    private JPanel panelMain;
    private JLabel welcomeLabel;

    // User Management
    private JButton manageUserAccountsButton;
    private JButton approveRejectUsersButton;

    // Curriculum Management
    private JButton addCourseModuleButton;
    private JButton assignModuleToCourseButton;
    private JButton updateCourseInfoButton;

    // Academic/Enrollment
    private JButton enrolStudentsButton;
    private JButton assignModuleToLecturerButton;
    private JButton issueStudentDecisionButton;

    // System/Reporting
    private JButton addBusinessRuleButton;
    private JButton displayCourseDetailsButton;
    private JButton displayModuleDetailsButton;

    private JButton logoutButton;

    public ViewManagerDashboard() {
        setTitle("Manager Dashboard");
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JLabel getWelcomeLabel() {
        return welcomeLabel;
    }

    // User Management
    public JButton getManageUserAccountsButton() {
        return manageUserAccountsButton;
    }
    public JButton getApproveRejectUsersButton() { // NEW GETTER
        return approveRejectUsersButton;
    }

    // Curriculum Management
    public JButton getAddCourseModuleButton() {
        return addCourseModuleButton;
    }
    public JButton getAssignModuleToCourseButton() {
        return assignModuleToCourseButton;
    }
    public JButton getUpdateCourseInfoButton() {
        return updateCourseInfoButton;
    }

    // Academic/Enrollment
    public JButton getEnrolStudentsButton() {
        return enrolStudentsButton;
    }
    public JButton getAssignModuleToLecturerButton() {
        return assignModuleToLecturerButton;
    }
    public JButton getIssueStudentDecisionButton() {
        return issueStudentDecisionButton;
    }

    // System/Reporting
    public JButton getAddBusinessRuleButton() {
        return addBusinessRuleButton;
    }
    public JButton getDisplayCourseDetailsButton() {
        return displayCourseDetailsButton;
    }
    public JButton getDisplayModuleDetailsButton() {
        return displayModuleDetailsButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public void setWelcomeMessage(String name) {
        welcomeLabel.setText("Welcome, Manager " + name + " (Admin)!");
    }
}