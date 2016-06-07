package mainWindow;

/**
 * Created by filip on 6/7/16.
 */




public class InputValidation {
    private InputValidation() { };

    static public boolean validate(InputValidationInterface function, String message) {
        // Returns true if validation was successful
        if (!function.test()) {
            AlertBox.display(message);
            return false;
        }
        return true;
    }
}
