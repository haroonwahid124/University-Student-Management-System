import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewDownloadMaterials {

    private JPanel panelMain;
    private JComboBox<String> moduleComboBox;
    private JTable materialsTable;
    private JButton downloadButton;
    private JButton backButton;
    private DefaultTableModel materialsTableModel;

    public ViewDownloadMaterials() {
        materialsTableModel = new DefaultTableModel(
                new Object[]{"File Name", "Type", "Week", "Upload Date", "Material ID"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (materialsTable != null) {
            materialsTable.setModel(materialsTableModel);
            materialsTable.getColumnModel().getColumn(4).setMaxWidth(0);
            materialsTable.getColumnModel().getColumn(4).setMinWidth(0);
            materialsTable.getColumnModel().getColumn(4).setPreferredWidth(0);
            materialsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JComboBox<String> getModuleComboBox() {
        return moduleComboBox;
    }

    public JTable getMaterialsTable() {
        return materialsTable;
    }

    public DefaultTableModel getMaterialsTableModel() {
        return materialsTableModel;
    }

    public JButton getDownloadButton() {
        return downloadButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void clearMaterialsTable() {
        materialsTableModel.setRowCount(0);
    }
}