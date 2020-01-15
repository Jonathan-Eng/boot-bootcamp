package pattern;

import java.util.regex.Pattern;

public class PatternValidator {
    public final static String NAME_PATTERN = "[A-Za-z]\\w{2,19}";
    public final static String TOKEN_PATTERN = "[A-Za-z]\\w{2,19}";
    public final static String ESINDEX_PATTERN = "[A-Za-z]\\w{2,19}";

    public static boolean isNameValid(String input) {
        return Pattern.matches(NAME_PATTERN, input);
    }
    public static boolean isTokenValid(String input) {
        return Pattern.matches(TOKEN_PATTERN, input);
    }
    public static boolean isEsindexValid(String input) {
        return Pattern.matches(ESINDEX_PATTERN, input);
    }
}
