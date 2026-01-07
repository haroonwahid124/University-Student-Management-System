import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewAssignModuleToCourse {
    private JPanel panelMain;
    private JComboBox<String> cmbCourse;
    private JComboBox<String> cmbModule;
    private JComboBox<String> cmbSemester;
    private JButton btnAssign;
    private JTable tblAssignments;
    private JButton btnBack;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Course Code", "Course Name", "Module Code", "Module Name", "Semester", "CourseID", "ModuleID"}, 0 // Hidden IDs
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public ViewAssignModuleToCourse() {
        tblAssignments.setModel(model);
        tblAssignments.setAutoCreateRowSorter(true);
        tblAssignments.getColumnModel().getColumn(5).setMinWidth(0);
        tblAssignments.getColumnModel().getColumn(5).setMaxWidth(0);
        tblAssignments.getColumnModel().getColumn(6).setMinWidth(0);
        tblAssignments.getColumnModel().getColumn(6).setMaxWidth(0);

       try {
            if (cmbSemester.getItemCount() == 0) {
                cmbSemester.addItem("1");
                cmbSemester.addItem("2");
            }
        } catch (NullPointerException e) {
        }
    }


    public JPanel getPanelMain() { return panelMain; }
    public JComboBox<String> getCmbCourse() { return cmbCourse; }
    public JComboBox<String> getCmbModule() { return cmbModule; }
    public JComboBox<String> getCmbSemester() { return cmbSemester; }
    public JButton getBtnAssign() { return btnAssign; }
    public JButton getBtnBack() { return btnBack; }
    public DefaultTableModel getModel() { return model; }

    public void addRow(String courseCode, String courseName, String moduleCode, String moduleName, String semester) {
        model.addRow(new Object[]{courseCode, courseName, moduleCode, moduleName, semester, null, null});
    }

    public void clearTable() {
        model.setRowCount(0);
    }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(panelMain, message, title, type);
    }
}