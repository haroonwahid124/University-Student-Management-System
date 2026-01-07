import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewUploadMaterials {
    private JPanel panelMain;
    private JComboBox<String> moduleComboBox;
    private JComboBox<String> typeComboBox;
    private JSpinner weekSpinner;
    private JTextField filePathTxt;
    private JButton browseButton;
    private JButton uploadButton;
    private JButton backButton;
    private JFileChooser fileChooser;

    public ViewUploadMaterials() {
        fileChooser = new JFileChooser();
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 15, 1);
        if (weekSpinner != null) {
            weekSpinner.setModel(spinnerModel);
        }

        // Setup Type ComboBox Model
        if (typeComboBox != null) {
            DefaultComboBoxModel<String> typeModel = new DefaultComboBoxModel<>();
            typeModel.addElement("lecture_note");
            typeModel.addElement("lab_note");
            typeComboBox.setModel(typeModel);
        }

        // Add listener for Browse Button
        if (browseButton != null) {
            browseButton.addActionListener(e -> handleBrowseFile());
        }
    }

    private void handleBrowseFile() {
        int result = fileChooser.showOpenDialog(panelMain);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathTxt.setText(selectedFile.getAbsolutePath());
        }
    }


    public JPanel getPanelMain() {
        return panelMain;
    }
    public JComboBox<String> getModuleComboBox() {
        return moduleComboBox;
    }
    public JComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }
    public JSpinner getWeekSpinner() {
        return weekSpinner;
    }
    public JTextField getFilePathTxt() {
        return filePathTxt;
    }
    public JButton getBrowseButton() {
        return browseButton;
    }
    public JButton getUploadButton() {
        return uploadButton;
    }
    public JButton getBackButton() {
        return backButton;
    }


    public void clearFields() {
        moduleComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
        weekSpinner.setValue(1);
        filePathTxt.setText("");
    }

    public String getFileNameOnly() {
        String fullPath = filePathTxt.getText();
        if (fullPath == null || fullPath.isEmpty()) return "";
        return new File(fullPath).getName();
    }
}