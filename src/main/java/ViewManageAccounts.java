import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ViewManageAccounts {
    // === Bound to the .form (must match bindings) ===
    private JPanel panelMain;
    private JComboBox<String> cmbRole;
    private JComboBox<String> cmbActive;
    private JButton btnSearch;
    private JTable tblUsers;
    private JButton btnActivate;
    private JButton btnDeactivate;
    private JButton btnResetPassword;
    private JButton btnBack;

    private DefaultTableModel model;

    public ViewManageAccounts() {
        cmbActive.removeAllItems();
        cmbActive.addItem("All");
        cmbActive.addItem("Yes");
        cmbActive.addItem("No");

        if (cmbRole.getItemCount() == 0) {
            cmbRole.addItem("All");
            cmbRole.addItem("student");
            cmbRole.addItem("lecturer");
            cmbRole.addItem("manager");
        }

        model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Role", "Active"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int col) {
                return switch (col) {
                    case 0 -> Integer.class;
                    case 4 -> Boolean.class;
                    default -> String.class;
                };
            }
        };

        tblUsers.setModel(model);
        tblUsers.setAutoCreateRowSorter(true);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.setRowHeight(24);

        TableColumnModel cols = tblUsers.getColumnModel();
        if (cols.getColumnCount() >= 5) {
            cols.getColumn(0).setPreferredWidth(60);   // ID
            cols.getColumn(1).setPreferredWidth(160);  // Name
            cols.getColumn(2).setPreferredWidth(220);  // Email
            cols.getColumn(3).setPreferredWidth(120);  // Role
            cols.getColumn(4).setPreferredWidth(80);   // Active
        }
    }

    public JPanel getPanelMain() { return panelMain; }
    public JTable getTblUsers() { return tblUsers; }
    public JComboBox<String> getCmbRole() { return cmbRole; }
    public JComboBox<String> getCmbActive() { return cmbActive; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnActivate() { return btnActivate; }
    public JButton getBtnDeactivate() { return btnDeactivate; }
    public JButton getBtnResetPassword() { return btnResetPassword; }
    public JButton getBtnBack(){ return btnBack; }
    public DefaultTableModel getTableModel() { return model; }

    public void clearTable() { model.setRowCount(0); }
    public void addUserRow(int id, String name, String email, String role, boolean active) {
        model.addRow(new Object[]{ id, name, email, role, active });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ViewManageAccounts v = new ViewManageAccounts();
            v.addUserRow(1, "Alice Brown", "alice@example.com", "Student", true);
            v.addUserRow(2, "Bob Chen", "bob@example.com", "Lecturer", false);
            v.addUserRow(3, "Cara Diaz", "cara@example.com", "Manager", true);

            JFrame f = new JFrame("Manage Accounts — Preview");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setContentPane(v.getPanelMain());
            f.setSize(900, 520);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

