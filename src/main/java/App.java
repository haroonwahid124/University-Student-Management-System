import javax.swing.*;

public class App {

    // Helper method to calculate the centered X coordinate
    private static int getCenteredX(int panelWidth, int frameWidth) {
        return (frameWidth - panelWidth) / 2;
    }

    public static void main(String[] args) {

        // Create the main application frame
        JFrame mainFrame = new JFrame("My Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create instances of views and model
        IUserModel mUser = new ModelUser();
        ViewSignup vSignup = new ViewSignup();
        ViewLogin vLogin = new ViewLogin();
        ViewApproveUsers vApprove = new ViewApproveUsers();
        ViewUpdatePassword vUpdate = new ViewUpdatePassword();

        // Student GUIS
        ViewStudentDashboard vDashboard = new ViewStudentDashboard();
        ViewCourseModuleInformation vCourseModuleInformation = new ViewCourseModuleInformation();
        ViewDownloadMaterials vDownloadMaterials = new ViewDownloadMaterials();
        ViewMarksProgress vMarksProgress = new ViewMarksProgress();
        ViewStudentDecisions vStudentDecisions = new ViewStudentDecisions();

        // Lecturer GUIS
        ViewLecturerDashboard vLecturerDashboard = new ViewLecturerDashboard();
        ViewUpdateModuleInfo vUpdateModuleInfo = new ViewUpdateModuleInfo();
        ViewUploadMaterials vUploadMaterials = new ViewUploadMaterials();
        ViewEnrolledStudents vEnrolledStudents = new ViewEnrolledStudents();
        ViewMarksManagement vMarksManagement = new ViewMarksManagement();

        // Manager Dashboard GUIS
        ViewManagerDashboard vManagerDashboard = new ViewManagerDashboard();
        ViewManageAccounts vManageAccounts = new ViewManageAccounts();
        ViewAssignModuleToLecturer vAssignModuleToLecturer = new ViewAssignModuleToLecturer();
        ViewIssueStudentDecision vIssueStudentDecision = new ViewIssueStudentDecision();
        ViewEnrolStudentsCourse vEnrolStudentsCourse = new ViewEnrolStudentsCourse();
        ViewDisplayModuleDetails vDisplayModuleDetails = new ViewDisplayModuleDetails();
        ViewUpdateCourseInfo vUpdateCourseInfo = new ViewUpdateCourseInfo();
        ViewAddCourse vAddCourse = new ViewAddCourse();
        ViewDisplayCourseDetails vDisplayCourseDetails = new ViewDisplayCourseDetails();
        ViewAssignModuleToCourse vAssignModuleToCourse = new ViewAssignModuleToCourse();
        ViewAddBusinessRule vAddBusinessRule = new ViewAddBusinessRule();

        // Create the controller and pass the views and model
        ControllerUser controller = new ControllerUser(
                vSignup,
                vLogin,
                mUser,
                vApprove,
                vUpdate,
                vDashboard,
                vCourseModuleInformation,
                vDownloadMaterials,
                vMarksProgress,
                vStudentDecisions,
                vLecturerDashboard,
                vUpdateModuleInfo,
                vUploadMaterials,
                vEnrolledStudents,
                vMarksManagement,
                vManagerDashboard,
                vManageAccounts,
                vAssignModuleToLecturer,
                vIssueStudentDecision,
                vEnrolStudentsCourse,
                vDisplayModuleDetails,
                vUpdateCourseInfo,
                vAddCourse,
                vDisplayCourseDetails,
                vAssignModuleToCourse,
                vAddBusinessRule
        );

        // Define bounds based on the LARGEST expected panel size:
        final int MAX_PANEL_WIDTH = 850;
        final int MAX_PANEL_HEIGHT = 600;
        final int FRAME_WIDTH = MAX_PANEL_WIDTH + 30; // 880
        final int FRAME_HEIGHT = MAX_PANEL_HEIGHT + 50; // 650

        // Calculate Centered X Coordinates
        final int X_450 = getCenteredX(460, FRAME_WIDTH); // Used for Login/Signup/Approve
        final int X_550 = getCenteredX(550, FRAME_WIDTH); // Used for Dashboards
        final int X_700 = getCenteredX(700, FRAME_WIDTH); // Used for Course/Manager Views
        final int X_750 = getCenteredX(750, FRAME_WIDTH); // Used for Marks Progress/Enrolled Students
        final int X_850 = getCenteredX(850, FRAME_WIDTH); // Used for Marks Management


        // Login/Signup Views
        vLogin.getPanelMain().setBounds(X_450, 0, 460, 300);
        vSignup.getPanelMain().setBounds(X_450, 0, 420, 300);
        vApprove.getPanelMain().setBounds(X_450, 0, 400, 300);
        vUpdate.getPanelMain().setBounds(X_450, 0, 450, 320);

        // Student GUIS
        vDashboard.getPanelMain().setBounds(X_550, 0, 500, 400);
        vCourseModuleInformation.getPanelMain().setBounds(X_700, 0, 700, 450);
        vDownloadMaterials.getPanelMain().setBounds(X_700, 0, 650, 400);
        vMarksProgress.getPanelMain().setBounds(X_750, 0, 750, 450);
        vStudentDecisions.getPanelMain().setBounds(X_700, 0, 700, 400);

        // Lecturer GUIS
        vLecturerDashboard.getPanelMain().setBounds(X_550, 0, 550, 450);
        vUpdateModuleInfo.getPanelMain().setBounds(X_700, 0, 600, 500);
        vUploadMaterials.getPanelMain().setBounds(X_550, 0, 550, 400);
        vEnrolledStudents.getPanelMain().setBounds(X_750, 0, 750, 500);
        vMarksManagement.getPanelMain().setBounds(X_850, 0, 850, 600);

        // Manager GUIS
        vManagerDashboard.getPanelMain().setBounds(X_700, 0, 700, 600);
        vManageAccounts.getPanelMain().setBounds(X_700, 0, 700, 600);
        vAssignModuleToLecturer.getPanelMain().setBounds(X_700, 0, 700, 600);
        vIssueStudentDecision.getPanelMain().setBounds(X_700, 0, 700, 600);
        vEnrolStudentsCourse.getPanelMain().setBounds(X_700, 0, 700, 600);
        vDisplayModuleDetails.getPanelMain().setBounds(X_700, 0, 700, 600);
        vUpdateCourseInfo.getPanelMain().setBounds(X_700, 0, 700, 600);
        vAddCourse.getPanelMain().setBounds(X_700, 0, 700, 600);
        vDisplayCourseDetails.getPanelMain().setBounds(X_700, 0, 700, 600);
        vAssignModuleToCourse.getPanelMain().setBounds(X_700, 0, 700, 600);
        vAddBusinessRule.getPanelMain().setBounds(X_700, 0, 700, 600);

        // Ensure all views are handled in the initialization block
        vSignup.getPanelMain().setVisible(true); // Show signup view first
        vLogin.getPanelMain().setVisible(false);
        vApprove.getPanelMain().setVisible(false);
        vUpdate.getPanelMain().setVisible(false);
        vDashboard.getPanelMain().setVisible(false);
        vCourseModuleInformation.getPanelMain().setVisible(false);
        vDownloadMaterials.getPanelMain().setVisible(false);
        vMarksProgress.getPanelMain().setVisible(false);
        vStudentDecisions.getPanelMain().setVisible(false);

        vLecturerDashboard.getPanelMain().setVisible(false);
        vUpdateModuleInfo.getPanelMain().setVisible(false);
        vUploadMaterials.getPanelMain().setVisible(false);
        vEnrolledStudents.getPanelMain().setVisible(false);
        vMarksManagement.getPanelMain().setVisible(false);

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


        // Add all views to the mainFrame.
        mainFrame.setLayout(null); // Using null layout for manual positioning
        mainFrame.add(vLogin.getPanelMain());
        mainFrame.add(vSignup.getPanelMain());
        mainFrame.add(vApprove.getPanelMain());
        mainFrame.add(vUpdate.getPanelMain());
        mainFrame.add(vDashboard.getPanelMain());
        mainFrame.add(vCourseModuleInformation.getPanelMain());
        mainFrame.add(vDownloadMaterials.getPanelMain());
        mainFrame.add(vMarksProgress.getPanelMain());
        mainFrame.add(vStudentDecisions.getPanelMain());

        mainFrame.add(vLecturerDashboard.getPanelMain());
        mainFrame.add(vUpdateModuleInfo.getPanelMain());
        mainFrame.add(vUploadMaterials.getPanelMain());
        mainFrame.add(vEnrolledStudents.getPanelMain());
        mainFrame.add(vMarksManagement.getPanelMain());

        mainFrame.add(vManagerDashboard.getPanelMain());
        mainFrame.add(vManageAccounts.getPanelMain());
        mainFrame.add(vAssignModuleToLecturer.getPanelMain());
        mainFrame.add(vIssueStudentDecision.getPanelMain());
        mainFrame.add(vEnrolStudentsCourse.getPanelMain());
        mainFrame.add(vDisplayModuleDetails.getPanelMain());
        mainFrame.add(vUpdateCourseInfo.getPanelMain());
        mainFrame.add(vAddCourse.getPanelMain());
        mainFrame.add(vDisplayCourseDetails.getPanelMain());
        mainFrame.add(vAssignModuleToCourse.getPanelMain());
        mainFrame.add(vAddBusinessRule.getPanelMain());

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setVisible(true); // Show the main frame
    }
}