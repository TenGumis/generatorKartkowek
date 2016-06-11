package mainWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static boolean onlyCharOrDigit(String s){
        Pattern p = Pattern.compile("[^A-za-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        boolean b = m.find();
        return !b;
    }
}
