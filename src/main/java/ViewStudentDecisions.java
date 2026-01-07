import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewStudentDecisions {

    private JPanel panelMain;
    private JLabel courseInfoLabel;
    private JTable decisionTable;
    private JButton backButton;
    private DefaultTableModel decisionTableModel;

    public ViewStudentDecisions() {
        // Initialize the table model structure
        decisionTableModel = new DefaultTableModel(
                new Object[]{"Decision ID", "Decision Type", "Decision Date", "Issued By (Manager ID)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table data is read-only
            }
        };

        if (decisionTable != null) {
            decisionTable.setModel(decisionTableModel);
            decisionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JLabel getCourseInfoLabel() {
        return courseInfoLabel;
    }

    public JTable getDecisionTable() {
        return decisionTable;
    }

    public DefaultTableModel getDecisionTableModel() {
        return decisionTableModel;
    }

    public JButton getBackButton() {
        return backButton;
    }

    // --- Utility Methods ---
    public void setCourseInfo(String info) {
        courseInfoLabel.setText("Decisions for: " + info);
    }

    public void clearDecisionTable() {
        decisionTableModel.setRowCount(0);
    }
}
