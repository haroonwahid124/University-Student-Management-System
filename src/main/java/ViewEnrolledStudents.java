import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ViewEnrolledStudents {
    private JPanel panelMain;
    private JComboBox<String> moduleComboBox;
    private JTable studentTable;
    private JButton backButton;
    private DefaultTableModel studentTableModel;
    private JLabel infoLabel;

    public ViewEnrolledStudents() {
        studentTableModel = new DefaultTableModel(
                new Object[]{"Student ID", "First Name", "Surname", "Email", "Enrollment Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (studentTable != null) {
            studentTable.setModel(studentTableModel);
            studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            studentTable.getColumnModel().getColumn(0).setMaxWidth(0);
            studentTable.getColumnModel().getColumn(0).setMinWidth(0);
            studentTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }


    public JPanel getPanelMain() {
        return panelMain;
    }

    public JComboBox<String> getModuleComboBox() {
        return moduleComboBox;
    }

    public JTable getStudentTable() {
        return studentTable;
    }

    public DefaultTableModel getStudentTableModel() {
        return studentTableModel;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    public void clearStudentTable() {
        studentTableModel.setRowCount(0);
    }

    public void setInfoLabelText(String text) {
        infoLabel.setText(text);
    }
}