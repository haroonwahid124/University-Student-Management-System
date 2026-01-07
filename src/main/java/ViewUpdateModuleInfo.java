import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ViewUpdateModuleInfo {
    private JPanel panelMain;
    private JComboBox<String> moduleComboBox;
    private JTextField moduleCodeTxt;
    private JTextField moduleNameTxt;
    private JTextField creditTxt;
    private JTextArea descriptionTextArea;
    private JButton updateButton;
    private JButton backButton;
    private JTable currentModulesTable;
    private JLabel selectedModuleLabel;
    private DefaultTableModel moduleTableModel;

    public ViewUpdateModuleInfo() {
        // Initialize the table model to show the lecturer's modules
        moduleTableModel = new DefaultTableModel(
                new Object[]{"Module ID", "Code", "Name", "Credits", "Description"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (currentModulesTable != null) {
            currentModulesTable.setModel(moduleTableModel);
            currentModulesTable.getColumnModel().getColumn(0).setMaxWidth(0);
            currentModulesTable.getColumnModel().getColumn(0).setMinWidth(0);
            currentModulesTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            currentModulesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public JPanel getPanelMain() {
        return panelMain;
    }
    public JComboBox<String> getModuleComboBox() {
        return moduleComboBox;
    }
    public JTable getCurrentModulesTable() {
        return currentModulesTable;
    }
    public JTextField getModuleCodeTxt() {
        return moduleCodeTxt;
    }
    public JTextField getModuleNameTxt() {
        return moduleNameTxt;
    }
    public JTextField getCreditTxt() {
        return creditTxt;
    }
    public JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }
    public JButton getUpdateButton() {
        return updateButton;
    }
    public JButton getBackButton() {
        return backButton;
    }
    public DefaultTableModel getModuleTableModel() {
        return moduleTableModel;
    }


    public void setSelectedModuleLabel(String text) {
        selectedModuleLabel.setText(text);
    }

    public void clearFields() {
        moduleCodeTxt.setText("");
        moduleNameTxt.setText("");
        creditTxt.setText("");
        descriptionTextArea.setText("");
        selectedModuleLabel.setText("Select a module above to edit details.");
    }

    public void clearModuleTable() {
        moduleTableModel.setRowCount(0);
    }
}