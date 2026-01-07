import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ViewMarksManagement {
    private JPanel panelMain;
    private JComboBox<String> moduleComboBox;
    private JTable studentMarksTable;
    private JButton backButton;
    private JButton updateMarkButton;
    private JLabel selectedStudentLabel;
    private JTextField examMarkTxt;
    private JTextField labMarkTxt;
    private JTextField overallMarkTxt;
    private JComboBox<String> statusComboBox;
    private JSpinner attemptSpinner;
    private DefaultTableModel marksTableModel;

    // Hidden state variables for the Controller
    private int selectedAttemptId = -1;
    private int selectedStudentId = -1;
    private int selectedModuleId = -1;

    public ViewMarksManagement() {
        marksTableModel = new DefaultTableModel(
                new Object[]{"Attempt ID", "Student ID", "First Name", "Surname", "Attempt No.", "Exam Mark", "Lab Mark", "Overall Mark", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table data is read-only
            }
        };

        if (studentMarksTable != null) {
            studentMarksTable.setModel(marksTableModel);
            studentMarksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            studentMarksTable.getColumnModel().getColumn(0).setMaxWidth(0);
            studentMarksTable.getColumnModel().getColumn(0).setMinWidth(0);
            studentMarksTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            studentMarksTable.getColumnModel().getColumn(1).setMaxWidth(0);
            studentMarksTable.getColumnModel().getColumn(1).setMinWidth(0);
            studentMarksTable.getColumnModel().getColumn(1).setPreferredWidth(0);
        }

        if (statusComboBox != null) {
            DefaultComboBoxModel<String> statusModel = new DefaultComboBoxModel<>();
            statusModel.addElement("passed");
            statusModel.addElement("failed");
            statusComboBox.setModel(statusModel);
        }

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 3, 1);
        if (attemptSpinner != null) {
            attemptSpinner.setModel(spinnerModel);
        }
    }

    public JPanel getPanelMain() { return panelMain; }
    public JComboBox<String> getModuleComboBox() { return moduleComboBox; }
    public JTable getStudentMarksTable() { return studentMarksTable; }
    public JButton getBackButton() { return backButton; }
    public JButton getUpdateMarkButton() { return updateMarkButton; }
    public DefaultTableModel getMarksTableModel() { return marksTableModel; }
    public JLabel getSelectedStudentLabel() { return selectedStudentLabel; }
    public JTextField getExamMarkTxt() { return examMarkTxt; }
    public JTextField getLabMarkTxt() { return labMarkTxt; }
    public JTextField getOverallMarkTxt() { return overallMarkTxt; }
    public JComboBox<String> getStatusComboBox() { return statusComboBox; }
    public JSpinner getAttemptSpinner() { return attemptSpinner; }



    public int getSelectedAttemptId() { return selectedAttemptId; }
    public void setSelectedAttemptId(int id) { this.selectedAttemptId = id; }
    public int getSelectedStudentId() { return selectedStudentId; }
    public void setSelectedStudentId(int id) { this.selectedStudentId = id; }
    public int getSelectedModuleId() { return selectedModuleId; }
    public void setSelectedModuleId(int id) { this.selectedModuleId = id; }



    public void clearTable() { marksTableModel.setRowCount(0); }

    public void clearMarkFields() {
        examMarkTxt.setText("");
        labMarkTxt.setText("");
        overallMarkTxt.setText("");
        attemptSpinner.setValue(1);
        statusComboBox.setSelectedIndex(0);
        selectedStudentLabel.setText("Select a student's attempt below to view/edit marks.");
        selectedAttemptId = -1;
        selectedStudentId = -1;
    }
}