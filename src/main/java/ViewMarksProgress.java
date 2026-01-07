import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewMarksProgress {

    private JPanel panelMain;
    private JLabel courseNameLabel;
    private JTable marksTable;
    private JButton backButton;
    private DefaultTableModel marksTableModel;

    public ViewMarksProgress() {
        marksTableModel = new DefaultTableModel(
                new Object[]{"Module Code", "Module Name", "Attempt", "Exam Mark", "Lab Mark", "Overall Mark", "Status", "Attempt Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (marksTable != null) {
            marksTable.setModel(marksTableModel);
            marksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JLabel getCourseNameLabel() {
        return courseNameLabel;
    }

    public JTable getMarksTable() {
        return marksTable;
    }

    public DefaultTableModel getMarksTableModel() {
        return marksTableModel;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setCourseName(String name) {
        courseNameLabel.setText("Current Course: " + name);
    }

    public void clearMarksTable() {
        marksTableModel.setRowCount(0);
    }
}