import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ViewAddCourse {
    private JPanel panelMain;
    private JTextField txtCode;
    private JTextField txtName;
    private JTextArea txtDescription;

    private JTable tblCourses;
    private JButton btnAdd;
    private JButton btnClear;
    private JButton btnBack;

    private JRadioButton rbAddCourse;
    private JRadioButton rbAddModule;
    private JLabel lblDescription;
    private JLabel lblCredit;
    private JTextField txtCredit;
    private JTable tblModules;

    private JScrollPane courseScrollPane;
    private JScrollPane moduleScrollPane;

    private JPanel cardPanel;

    private final DefaultTableModel courseModel = new DefaultTableModel(
            new Object[]{"CourseID","Code","Name","Description"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) {
            return c==0 ? Integer.class : String.class;
        }
    };

    private final DefaultTableModel moduleModel = new DefaultTableModel(
            new Object[]{"ModuleID","Code","Name","Credit"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c) {
            return c==0 || c==3 ? Integer.class : String.class;
        }
    };

    public ViewAddCourse() {
        tblCourses.setModel(courseModel);
        tblCourses.setAutoCreateRowSorter(true);
        tblCourses.setRowHeight(22);

        tblCourses.getColumnModel().getColumn(0).setMinWidth(0);
        tblCourses.getColumnModel().getColumn(0).setMaxWidth(0);

        tblCourses.getColumnModel().getColumn(1).setMinWidth(0);
        tblCourses.getColumnModel().getColumn(1).setMaxWidth(0);


        if (tblModules != null) {
            tblModules.setModel(moduleModel);
            tblModules.setAutoCreateRowSorter(true);
            tblModules.setRowHeight(22);
            tblModules.getColumnModel().getColumn(0).setMinWidth(0);
            tblModules.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        if (rbAddCourse != null) {
            rbAddCourse.setSelected(true);
            switchMode(true);
        } else {
            if (lblDescription != null) lblDescription.setVisible(true);
            if (txtDescription != null) txtDescription.setVisible(true);
            if (lblCredit != null) lblCredit.setVisible(false);
            if (txtCredit != null) txtCredit.setVisible(false);
        }
    }

    public JPanel getPanelMain() { return panelMain; }
    public JRadioButton getRbAddCourse() { return rbAddCourse; }
    public JRadioButton getRbAddModule() { return rbAddModule; }
    public JTextField getTxtCode() { return txtCode; }
    public JTextField getTxtName() { return txtName; }
    public JTextArea getTxtDescription() { return txtDescription; }
    public JTextField getTxtCredit() { return txtCredit; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnBack() { return btnBack; }
    public DefaultTableModel getCourseModel() { return courseModel; }
    public DefaultTableModel getModuleModel() { return moduleModel; }
    public JLabel getLblDescription() { return lblDescription; }
    public JLabel getLblCredit() { return lblCredit; }

    public void clearInputs() {
        if (txtCode != null) txtCode.setText("");
        if (txtName != null) txtName.setText("");
        if (txtDescription != null) txtDescription.setText("");
        if (txtCredit != null) txtCredit.setText("");
        if (txtCode != null) txtCode.requestFocus();
    }

    public void addCourseRow(int id, String code, String name, String desc) {
        courseModel.addRow(new Object[]{id, code, name, desc});
    }

    public void addModuleRow(int id, String code, String name, int credit) {
        moduleModel.addRow(new Object[]{id, code, name, credit});
    }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(panelMain, message, title, type);
    }

    public void switchMode(boolean isCourseMode) {
        clearInputs();
        clearTable();
        if (cardPanel == null) return;

        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();

        if (isCourseMode) {
            if (lblCredit != null) lblCredit.setVisible(false);
            if (txtCredit != null) txtCredit.setVisible(false);
            if (lblDescription != null) lblDescription.setVisible(true);
            txtDescription.setVisible(true);

            btnAdd.setText("Add Course");
            cardLayout.show(cardPanel, "CourseCard");

        } else {
            // Switch to Module Mode

            if (lblCredit != null) lblCredit.setVisible(true);
            if (txtCredit != null) txtCredit.setVisible(true);
            if (lblDescription != null) lblDescription.setVisible(false);
            txtDescription.setVisible(false);

            // Button Text and Table Switch
            btnAdd.setText("Add Module");
            cardLayout.show(cardPanel, "ModuleCard");
        }
    }

    public void clearTable() {
        courseModel.setRowCount(0);
        moduleModel.setRowCount(0);
    }
}