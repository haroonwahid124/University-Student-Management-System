import javax.swing.*;

public class ViewLogin extends JFrame{
    private JTextField emailTxt;
    private JPasswordField passwordTxt;
    private JButton loginButton;
    private JButton clearButton; // Add this
    private JButton showSignupButton; // Add this
    private JPanel panelMain;
    private JButton showUpdatePasswordButton;

    public ViewLogin(){
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panelMain);
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getShowSignupButton() {
        return showSignupButton;
    }

    public JPasswordField getPasswordField1() {
        return passwordTxt;
    }

    public JTextField getEmailTxt() {
        return emailTxt;
    }

    public void setEmailTxt(JTextField emailTxt) {
        this.emailTxt = emailTxt;
    }

    public JPasswordField getPasswordTxt() {
        return passwordTxt;
    }

    public void setPasswordTxt(JPasswordField passwordTxt) {
        this.passwordTxt = passwordTxt;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }

    public JButton getShowUpdatePasswordButton() {
        return showUpdatePasswordButton;
    }


    public void clearTxts(){
        emailTxt.setText("");
        passwordTxt.setText("");
    }
}