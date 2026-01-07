import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewDisplayModuleDetails {
    private JPanel panelMain;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnRefresh;
    private JTable tblModules;
    private JButton btnBack;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Code", "Name", "Credits"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) {
            return c == 2 ? Integer.class : String.class;
        }
    };

    public ViewDisplayModuleDetails() {
        tblModules.setModel(model);
        tblModules.setAutoCreateRowSorter(true);
        tblModules.setRowHeight(22);
    }


    public JPanel getPanelMain() { return panelMain; }
    public JTextField getTxtSearch() { return txtSearch; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JTable getTblModules() { return tblModules; }
    public JButton getBtnBack() { return btnBack; }
    public DefaultTableModel getModel() { return model; }

    // Helpers
    public void clearTable() { model.setRowCount(0); }
    public void addRow(String code, String name, int credits) {
        model.addRow(new Object[]{code, name, credits});
    }
}
