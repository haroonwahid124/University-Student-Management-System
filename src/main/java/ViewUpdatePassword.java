import javax.swing.*;

public class ViewUpdatePassword {
    private JPanel panelMain;
    private JTextField usernameTxt;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField1;
    private JPasswordField newPasswordField2;
    private JButton updateButton;
    private JButton clearButton;
    private JButton showLoginButton;
    

    public void clearTxts() {
        usernameTxt.setText("");
        oldPasswordField.setText("");
        newPasswordField1.setText("");
        newPasswordField2.setText("");
        usernameTxt.grabFocus();
    }

    public JPanel getPanelMain() { return panelMain; }
    public JTextField getUsernameTxt() { return usernameTxt; }
    public JPasswordField getOldPasswordField() { return oldPasswordField; }
    public JPasswordField getNewPasswordField1() { return newPasswordField1; }
    public JPasswordField getNewPasswordField2() { return newPasswordField2; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getClearButton() { return clearButton; }
    public JButton getShowLoginButton() { return showLoginButton; }
}


