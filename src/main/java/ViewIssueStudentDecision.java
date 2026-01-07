import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewIssueStudentDecision {
    private JPanel panelMain;
    private JComboBox<String> cmbStudent;
    private JComboBox<String> cmbDecision;
    private JTable tblDecisions;
    private JButton btnIssue;
    private JButton btnBack;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Student","Decision","Date","Manager"}, 0
    ) {
        @Override public boolean isCellEditable(int r,int c){ return false; }
        @Override public Class<?> getColumnClass(int c){
            return switch(c){ case 0 -> Integer.class; default -> String.class; };
        }
    };

    public ViewIssueStudentDecision() {
        tblDecisions.setModel(model);
        tblDecisions.setAutoCreateRowSorter(true);
        tblDecisions.setRowHeight(22);
    }

    public JPanel getPanelMain(){ return panelMain; }
    public JComboBox<String> getCmbStudent(){ return cmbStudent; }
    public JComboBox<String> getCmbDecision(){ return cmbDecision; }
    public JTable getTblDecisions(){ return tblDecisions; }
    public JButton getBtnIssue(){ return btnIssue; }
    public JButton getBtnBack(){ return btnBack; }
    public DefaultTableModel getTableModel(){ return model; }

    // helpers
    public void clearTable(){ model.setRowCount(0); }
    public void addRow(int id, String student, String decision, String date, String manager){
        model.addRow(new Object[]{id, student, decision, date, manager});
    }
}

