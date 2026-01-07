import javax.swing.*;
import java.awt.*;

public class ViewLecturerDashboard extends JFrame {

    private JPanel panelMain;
    private JLabel welcomeLabel;
    private JButton updateModuleButton;
    private JButton uploadMaterialsButton;
    private JButton viewEnrolledStudentsButton;
    private JButton marksManagementButton;
    private JButton logoutButton;

    public ViewLecturerDashboard() {
        setTitle("Lecturer Dashboard");

    }
    public JPanel getPanelMain() {
        return panelMain;
    }

    public JLabel getWelcomeLabel() {
        return welcomeLabel;
    }

    public JButton getUpdateModuleButton() {
        return updateModuleButton;
    }

    public JButton getUploadMaterialsButton() {
        return uploadMaterialsButton;
    }

    public JButton getViewEnrolledStudentsButton() {
        return viewEnrolledStudentsButton;
    }

    public JButton getMarksManagementButton() {
        return marksManagementButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public void setWelcomeMessage(String name) {
        welcomeLabel.setText("Welcome, Dr./Mr./Ms. " + name + "!");
    }
}