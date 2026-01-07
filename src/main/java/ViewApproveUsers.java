import javax.swing.*;
import java.awt.*;

public class ViewApproveUsers {
    private JPanel panelMain;
    private JList<String> userList;
    private JButton approveButton;
    private JButton rejectButton;
    private JButton backButton;

    // Constructor
    public ViewApproveUsers() {
        // Create main panel
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout(10, 10));

        // Title label
        JLabel titleLabel = new JLabel("Approve Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panelMain.add(titleLabel, BorderLayout.NORTH);

        // Create the list of users
        userList = new JList<>(new DefaultListModel<>());
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userList);
        panelMain.add(scrollPane, BorderLayout.CENTER);

        // Approve / Reject / Back
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        approveButton = new JButton("Approve");
        rejectButton = new JButton("Reject");
        backButton = new JButton("Back");

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        panelMain.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JList<String> getUserList() {
        return userList;
    }

    public JButton getApproveButton() {
        return approveButton;
    }

    public JButton getRejectButton() {
        return rejectButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
