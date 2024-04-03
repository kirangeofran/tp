package bookmarked.ui;

public class Ui {
    static final String LINE_BREAK = "_______________________________________\n"
            + "_______________________________________";

    static final String SMALLER_LINE_BREAK = "_______________________________________\n";


    public static void greetings() {
        System.out.println(LINE_BREAK);
        System.out.println("\nWelcome to BookMarked, a one-stop app for all your librarian needs!");
        System.out.println("To get started, you can type /help to see a list of commands!\n");
        System.out.println(LINE_BREAK + "\n");
    }

    public static void setLineBreak() {
        System.out.println("\n" + LINE_BREAK + "\n");
    }

    public static void setSmallerLineBreak() {
        System.out.println("\n" + SMALLER_LINE_BREAK);
    }
    
    public static void printHelpMessage() {
        System.out.println("These are the current available features and the format that you need");
        System.out.println("to follow to use it in using this software:");
        System.out.println("1. To add books");
        System.out.println("   add NAME_OF_BOOK");
        System.out.println("2. To delete current existing book");
        System.out.println("   delete NUMBER_ACCORDING_TO_LIST");
        System.out.println("3. To mark book as borrowed");
        System.out.println("   borrow NAME_OF_BOOK by USER_NAME");
        System.out.println("4. To extend the due date of a borrowed book by a week:");
        System.out.println("   extend NAME_OF_BOOK");
        System.out.println("5. To unmark book as returned");
        System.out.println("   return NAME_OF_BOOK");
        System.out.println("6. To list all the books added");
        System.out.println("   list");
        System.out.println("7. To list all the books added in alphabetical order");
        System.out.println("   list /sortby alphabetical");
        System.out.println("8. To list all the borrowed books by return date");
        System.out.println("   list /sortby returndate");
        System.out.println("9. To edit the details of the existing book");
        System.out.println("   edit NUMBER_ACCORDING_TO_LIST /WHAT_TO_EDIT DESCRIPTION");
        System.out.println("   example: edit 1 /title book");
        System.out.println("10.To list the users with their borrowed books");
        System.out.println("   listuser");

    }

    public static void printUnknownCommand() {
        System.out.println("Unknown command; please type /help to see what commands you can use.");
    }

    public static void printEmptyListMessage() {
        System.out.println("The list is empty, try adding a book first.");
    }

    public static void printEmptyArgumentsMessage() {
        System.out.println("Please type in the correct arguments.");
    }

    public static void printOutOfBoundsMessage() {
        System.out.println("Please enter a book index that exists on the current list.");
    }

    public static void printBookNotFoundExceptionMessage() {
        System.out.println("Book not found");}


    public static void printBookNotBorrowedExceptionMessage() {
        System.out.println("Book not borrowed");
    }
    public static void printNotNumberMessage() {
        System.out.println("Please enter the number index of the book.");
    }

    public static void exitProgramme() {
        System.out.println("Bye!");
    }
}
