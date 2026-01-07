import javax.swing.*;

public class ViewStudentDashboard extends JFrame {

    private JPanel panelMain;
    private JLabel welcomeLabel;
    private JButton viewModulesButton;
    private JButton downloadMaterialsButton;
    private JButton viewMarksButton;
    private JButton viewDecisionsButton;
    private JButton logoutButton;

    public ViewStudentDashboard() {
        setTitle("Student Dashboard");
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JLabel getWelcomeLabel() {
        return welcomeLabel;
    }

    public JButton getViewModulesButton() {
        return viewModulesButton;
    }

    public JButton getDownloadMaterialsButton() {
        return downloadMaterialsButton;
    }

    public JButton getViewMarksButton() {
        return viewMarksButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JButton getViewDecisionsButton() {
        return viewDecisionsButton;
    }

    public void setWelcomeMessage(String name) {
        welcomeLabel.setText("Welcome, " + name + "!");
    }
}
