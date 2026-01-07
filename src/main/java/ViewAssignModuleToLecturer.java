import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewAssignModuleToLecturer {
    private JPanel panelMain;
    private JComboBox<String> cmbLecturer;
    private JComboBox<String> cmbModule;
    private JButton btnAssign;
    private JTable tblAssignments;
    private JButton btnBack;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Module ID","Code","Module Name","Lecturer ID","Lecturer Name"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) {
            return switch (c) {
                case 0, 3 -> Integer.class;
                default -> String.class;
            };
        }
    };

    public ViewAssignModuleToLecturer() {
        tblAssignments.setModel(model);
        tblAssignments.setAutoCreateRowSorter(true);
        tblAssignments.setRowHeight(22);
    }

    public JPanel getPanelMain()          { return panelMain; }
    public JComboBox<String> getCmbLecturer() { return cmbLecturer; }
    public JComboBox<String> getCmbModule()   { return cmbModule; }
    public JButton getBtnAssign()         { return btnAssign; }
    public JButton getBtnBack()           { return btnBack; }
    public DefaultTableModel getModel()   { return model; }

    public void clearTable()              { model.setRowCount(0); }
    public void addRow(int mid, String code, String name, Integer lid, String lname) {
        model.addRow(new Object[]{ mid, code, name, lid, lname });
    }
}

