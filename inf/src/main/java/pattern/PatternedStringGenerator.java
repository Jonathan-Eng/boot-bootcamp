package pattern;

import com.mifmif.common.regex.Generex;

public class PatternedStringGenerator {

    /**
     * Generates a random account name, abides by the NAME_PATTERN pattern
     * @return
     */
    public static String generateName() {
        return new Generex(PatternValidator.NAME_PATTERN).random();
    }

    /**
     * Generates a random account token, abides by the TOKEN_PATTERN pattern
     * @return
     */
    public static String generateToken() {
        return new Generex(PatternValidator.TOKEN_PATTERN).random();
    }

    /**
     * Generates a random account ES index, abides by the ESINDEX_PATTERN pattern
     * @return
     */
    public static String generateEsindex() {
        return new Generex(PatternValidator.ESINDEX_PATTERN).random();
    }

    /**
     * Generates a message composed of letters of size len
     * @param len
     * @return
     */
    public static String generateMessage(int len) {
        String messagePattern = "[A-Za-z]{" + len + "}";
        return new Generex(messagePattern).random();
    }
}
