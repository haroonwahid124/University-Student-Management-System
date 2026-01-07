import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewUpdateCourseInfo {
    private JPanel panelMain;
    private JComboBox<String> cmbCourse;
    private JButton btnRefresh;
    private JTextArea txtDescription;
    private JTable tblModules;
    private JButton btnSaveDesc;
    private JButton btnSaveCredits;
    private JButton btnBack;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ModuleID","Code","Name","Credits"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return c == 3; } // only Credits
        @Override public Class<?> getColumnClass(int c) {
            return switch (c) { case 0 -> Integer.class; case 3 -> Integer.class; default -> String.class; };
        }
    };

    public ViewUpdateCourseInfo() {
        tblModules.setModel(model);
        tblModules.setAutoCreateRowSorter(true);
        tblModules.setRowHeight(22);
        tblModules.getColumnModel().getColumn(0).setMinWidth(0);
        tblModules.getColumnModel().getColumn(0).setMaxWidth(0);
        tblModules.getColumnModel().getColumn(0).setWidth(0);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
    }

    public JPanel getPanelMain() { return panelMain; }
    public JComboBox<String> getCmbCourse() { return cmbCourse; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JTextArea getTxtDescription() { return txtDescription; }
    public JTable getTblModules() { return tblModules; }
    public JButton getBtnSaveDesc() { return btnSaveDesc; }
    public JButton getBtnSaveCredits() { return btnSaveCredits; }
    public JButton getBtnBack() { return btnBack; }
    public DefaultTableModel getModel() { return model; }

    // Helpers
    public void clearTable(){ model.setRowCount(0); }
    public void addRow(int moduleId, String code, String name, int credits){
        model.addRow(new Object[]{moduleId, code, name, credits});
    }
}

