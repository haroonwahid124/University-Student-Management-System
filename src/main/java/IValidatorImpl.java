import java.text.SimpleDateFormat;
import java.text.ParseException;

public class IValidatorImpl implements IValidator {

    @Override
    public boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") &&
                email.indexOf("@") > 0 && // Ensure '@' is not the first character
                email.indexOf("@") < email.lastIndexOf(".") &&
                email.lastIndexOf(".") - email.indexOf("@") > 1; // Ensures at least one character between @ and .
    }

    @Override
    public boolean validateDate(String value) {
        // Check if the format is correct (YYYY-MM-DD)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public boolean validateName(String name) {
        // not empty and only letters
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z]+");
    }
}