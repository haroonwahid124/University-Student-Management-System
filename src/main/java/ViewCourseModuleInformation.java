import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewCourseModuleInformation {

    private JPanel panelMain;
    private JLabel courseNameLabel;
    private JTable moduleTable;
    private JButton viewMaterialsButton;
    private JButton backButton;
    private DefaultTableModel moduleTableModel;

    public ViewCourseModuleInformation() {
        moduleTableModel = new DefaultTableModel(
                new Object[]{"Module Code", "Module Name", "Credits", "Module ID"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (moduleTable != null) {
            moduleTable.setModel(moduleTableModel);
            moduleTable.getColumnModel().getColumn(3).setMaxWidth(0);
            moduleTable.getColumnModel().getColumn(3).setMinWidth(0);
            moduleTable.getColumnModel().getColumn(3).setPreferredWidth(0);
            moduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JLabel getCourseNameLabel() {
        return courseNameLabel;
    }

    public JTable getModuleTable() {
        return moduleTable;
    }

    public DefaultTableModel getModuleTableModel() {
        return moduleTableModel;
    }

    public JButton getViewMaterialsButton() {
        return viewMaterialsButton;
    }

    public JButton getBackButton() {
        return backButton;
    }


    public void setCourseName(String courseName) {
        courseNameLabel.setText(courseName);
    }

    public void clearModuleTable() {
        moduleTableModel.setRowCount(0);
    }
}