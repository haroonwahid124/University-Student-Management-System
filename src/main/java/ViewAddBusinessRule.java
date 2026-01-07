import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ViewAddBusinessRule {
    private JPanel panelMain;
    private JComboBox<String> cmbRuleType;
    private JComboBox<String> cmbScope;
    private JTextField txtValue;
    private JButton btnAddRule;
    private JButton btnBack;
    private JTable tblRules;
    private JLabel lblTarget;
    private JComboBox<String> cmbTarget;
    private JButton btnRefresh;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Rule Type", "Value", "Scope", "Target Name/ID"}, 0
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public ViewAddBusinessRule() {
        tblRules.setModel(model);
        tblRules.setAutoCreateRowSorter(true);

        cmbRuleType.addItem("-- Select Type --");
        cmbRuleType.addItem("max_attempts");
        cmbRuleType.addItem("compensation_allowed");

        cmbScope.addItem("global");
        cmbScope.addItem("course_specific");
        cmbScope.addItem("module_specific");

        tblRules.getColumnModel().getColumn(0).setMinWidth(0);
        tblRules.getColumnModel().getColumn(0).setMaxWidth(0);

        lblTarget.setText("Target (N/A):");
        cmbTarget.setEnabled(false);
    }

    public JPanel getPanelMain() { return panelMain; }
    public JComboBox<String> getCmbRuleType() { return cmbRuleType; }
    public JComboBox<String> getCmbScope() { return cmbScope; }
    public JTextField getTxtValue() { return txtValue; }
    public JButton getBtnAddRule() { return btnAddRule; }
    public JButton getBtnBack() { return btnBack; }
    public JTable getTblRules() { return tblRules; }
    public DefaultTableModel getModel() { return model; }
    public JLabel getLblTarget() { return lblTarget; }
    public JComboBox<String> getCmbTarget() { return cmbTarget; }
    public JButton getBtnRefresh() { return btnRefresh; }


    public void addRuleRow(int id, String type, int value, String scope, String targetName) {
        model.addRow(new Object[]{id, type, value, scope, targetName});
    }

    public void clearTable() {
        model.setRowCount(0);
    }

    public void clearInputs() {
        cmbRuleType.setSelectedIndex(0);
        cmbScope.setSelectedIndex(0);
        txtValue.setText("");
        cmbTarget.removeAllItems();
        cmbTarget.setEnabled(false);
        lblTarget.setText("Target (N/A):");
    }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(panelMain, message, title, type);
    }
}