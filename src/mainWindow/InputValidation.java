package mainWindow;

/**
 * Created by filip on 6/7/16.
 */




public class InputValidation {
    private InputValidation() { };

    static public void validate(InputValidationInterface function, String message) {
        if (function.test()) {
            AlertBox.display(message);
        }
    }
}
