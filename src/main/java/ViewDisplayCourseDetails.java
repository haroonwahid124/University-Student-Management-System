import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ViewDisplayCourseDetails {
    private JPanel panelMain;
    private JComboBox<String> cmbCourse;
    private JButton btnDisplay;
    private JButton btnBack;
    private JLabel courseInfoLabel;
    private JTable tblModules;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Module Code", "Module Name", "Credit", "Semester"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) {
            return c == 2 ? Integer.class : String.class;
        }
    };

    public ViewDisplayCourseDetails() {
        tblModules.setModel(model);
        tblModules.setAutoCreateRowSorter(true);
        tblModules.setRowHeight(22);
    }

    public JPanel getPanelMain() { return panelMain; }
    public JComboBox<String> getCmbCourse() { return cmbCourse; }
    public JButton getBtnDisplay() { return btnDisplay; }
    public JButton getBtnBack() { return btnBack; }
    public JLabel getCourseInfoLabel() { return courseInfoLabel; }
    public DefaultTableModel getModel() { return model; }

    public void setCourseInfo(String courseCode, String courseName, String description) {
        // Simple display of course name and description
        String desc = description != null && !description.isEmpty() ? " | Description: " + description : "";
        courseInfoLabel.setText("Details for: " + courseCode + " - " + courseName + desc);
    }

    public void clearTable() { model.setRowCount(0); }

    public void clearView() {
        clearTable();
        courseInfoLabel.setText("Select a course and press 'Display' to view details.");
        if (cmbCourse.getItemCount() > 0) {
            cmbCourse.setSelectedIndex(0);
        }
    }

    public void addModuleRow(String code, String name, int credits, String semester) {
        model.addRow(new Object[]{code, name, credits, semester});
    }
}