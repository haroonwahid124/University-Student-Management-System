import javax.swing.*;
import java.awt.*;

public class ViewEnrolStudentsCourse {
    private JPanel panelMain;
    private JComboBox<String> cmbCourse;
    private JList<String> listAvailable;
    private JList<String> listEnrolled;
    private JButton btnEnroll;
    private JButton btnUnenrol;
    private JButton btnBack;

    private final DefaultListModel<String> modelAvailable = new DefaultListModel<>();
    private final DefaultListModel<String> modelEnrolled  = new DefaultListModel<>();

    public ViewEnrolStudentsCourse() {
        listAvailable.setModel(modelAvailable);
        listEnrolled.setModel(modelEnrolled);
        listAvailable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEnrolled.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public JPanel getPanelMain() { return panelMain; }
    public JComboBox<String> getCmbCourse() { return cmbCourse; }
    public JList<String> getListAvailable() { return listAvailable; }
    public JList<String> getListEnrolled() { return listEnrolled; }
    public JButton getBtnEnroll() { return btnEnroll; }
    public JButton getBtnUnenrol() { return btnUnenrol; }
    public JButton getBtnBack() { return btnBack; }

    // helpers
    public DefaultListModel<String> getAvailableModel() { return modelAvailable; }
    public DefaultListModel<String> getEnrolledModel() { return modelEnrolled; }
    public void clearLists() { modelAvailable.clear(); modelEnrolled.clear(); }
}