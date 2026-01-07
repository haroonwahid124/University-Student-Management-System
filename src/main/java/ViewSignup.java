import javax.swing.*;

public class ViewSignup extends JFrame{

    private JPanel panelMain;
    private JTextField emailTxt;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton signupButton;
    private JButton clearButton;
    private JButton showLoginButton;
    private JComboBox gender_value;
    private JLabel FirstName;
    private JLabel Surname;
    private JTextField firstName;
    private JTextField surname;
    private JTextField dobTxt;
    private JComboBox userType;

    public ViewSignup(){
        setTitle("Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panelMain);
    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    public void setPanelMain(JPanel panelMain) {
        this.panelMain = panelMain;
    }

    public JTextField getEmailTxt() {
        return emailTxt;
    }

    public void setEmailTxt(JTextField emailTxt) {
        this.emailTxt = emailTxt;
    }

    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public void setPasswordField1(JPasswordField passwordField1) {
        this.passwordField1 = passwordField1;
    }

    public JPasswordField getPasswordField2() {
        return passwordField2;
    }

    public void setPasswordField2(JPasswordField passwordField2) {
        this.passwordField2 = passwordField2;
    }


    public JButton getSignupButton() {
        return signupButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getShowLoginButton() {
        return showLoginButton;
    }


    public void setSignupButton(JButton signupButton) {
        this.signupButton = signupButton;
    }

    public void setClearButton(JButton clearButton) {
        this.clearButton = clearButton;
    }

    public void setShowLoginButton(JButton showLoginButton) {
        this.showLoginButton = showLoginButton;
    }

    public JComboBox getGender_value() {
        return gender_value;
    }

    public void setGender_value(JComboBox gender_value) {
        this.gender_value = gender_value;
    }

    public JLabel getFirstName() {
        return FirstName;
    }

    public JTextField getFirstNameTxt() {
        return firstName;
    }

    public void setFirstName(JTextField firstName) {
        this.firstName = firstName;
    }

    public JLabel getSurnameLabel() {
        return Surname;
    }

    public void setSurnameLabel(JLabel Surname) {
        this.Surname = Surname;
    }

    public JTextField getSurname() {
        return surname;
    }

    public void setSurname(JTextField surname) {
        this.surname = surname;
    }

    public void setFirstName(JLabel firstName) {
        FirstName = firstName;
    }

    public JTextField getDobTxt() {
        return dobTxt;
    }

    public void setDobTxt(JTextField dobTxt) {
        this.dobTxt = dobTxt;
    }

    public JComboBox getUserType() {
        return userType;
    }

    public void setUserType(JComboBox userType) {
        this.userType = userType;
    }

    public void clearTxts(){
        emailTxt.setText("");
        passwordField1.setText("");
        passwordField2.setText("");
        firstName.setText("");
        surname.setText("");
        gender_value.setSelectedIndex(0);
        if (dobTxt != null) dobTxt.setText("");
    }
}