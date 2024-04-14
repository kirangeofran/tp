package bookmarked.arguments;

import bookmarked.exceptions.InvalidStringException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class TitleValidity {

    /**
     * Checks the validity of the book title string.
     *
     * @param bookName The title of the book to be checked.
     * @throws InvalidStringException if the book title contains illegal characters or is numerical.
     */
    public static void checkTitleValidity(String bookName) throws InvalidStringException {
        if (bookName.matches("^[0-9]+$")) {
            throw new InvalidStringException();
        }

        try {
            boolean hasSpecialCharacters = checkSpecialCharacters(bookName);
            if (hasSpecialCharacters) {
                throw new InvalidStringException();
            }
        } catch (PatternSyntaxException e) {
            throw new InvalidStringException();
        }

    }

    public static boolean checkSpecialCharacters(String bookName) {
        String pattern = "[^A-Za-z0-9 ]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(bookName);
        return m.find();
    }
}
