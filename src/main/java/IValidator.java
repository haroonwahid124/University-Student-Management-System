public interface IValidator {
    boolean validateEmail(String email);
    boolean validateDate(String value);
    boolean validateName(String name); // For first name and surname
}