package bookmarked.ui;
import bookmarked.Book;
import bookmarked.user.User;

import java.util.ArrayList;


public class Ui {
    static final String LINE_BREAK = "_______________________________________________________________________________\n"
            + "_______________________________________________________________________________";

    static final String SMALLER_LINE_BREAK =
            "_______________________________________________________________________________\n";


    public static void greetings() {
        System.out.println(LINE_BREAK);
        System.out.println("\nWelcome to BookMarked, a one-stop app for all your librarian needs!");
        System.out.println("To get started, you can type 'help' to see a list of commands!\n");
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
        System.out.println();
        System.out.println("1. To add books");
        System.out.println("   add BOOK_TITLE (optional)/quantity NUMBER_OF_COPIES");
        System.out.println();
        System.out.println("2. To delete current existing book");
        System.out.println("   delete INDEX (optional)/quantity NUMBER_OF_COPIES");
        System.out.println();
        System.out.println("3. To mark book as borrowed by book index");
        System.out.println("   borrow INDEX /by USER_NAME");
        System.out.println();
        System.out.println("4. To mark book as borrowed by book title");
        System.out.println("   borrow BOOK_TITLE /by USER_NAME");
        System.out.println();
        System.out.println("5. To extend the due date of a borrowed book by a week by book index");
        System.out.println("   extend INDEX /by USER_NAME");
        System.out.println();
        System.out.println("6. To extend the due date of a borrowed book by a week by book title");
        System.out.println("   extend BOOK_TITLE /by USER_NAME");
        System.out.println();
        System.out.println("7. To unmark book as returned by book index");
        System.out.println("   return INDEX /by USER_NAME");
        System.out.println();
        System.out.println("8. To unmark book as returned by book title");
        System.out.println("   return BOOK_TITLE /by USER_NAME");
        System.out.println();
        System.out.println("9. To list all the books added");
        System.out.println("   list /sortby default");
        System.out.println();
        System.out.println("10. To list all the books added in alphabetical order");
        System.out.println("   list /sortby alphabetical");
        System.out.println();
        System.out.println("11. To list all the users and their borrowed books");
        System.out.println("   list /sortby user");
        System.out.println();
        System.out.println("12. To edit the title of the existing book by index");
        System.out.println("   edit INDEX /title DESCRIPTION");
        System.out.println();
        System.out.println("13. To edit the title of the existing book by current book title");
        System.out.println("   edit CURRENT_BOOK_TITLE /title NEW_BOOK_TITLE");
        System.out.println();
        System.out.println("14.To find a specific user and their borrowed books:");
        System.out.println("   find /by user USER_NAME");
        System.out.println();
        System.out.println("15.To find a book in the inventory");
        System.out.println("   find /by book BOOK_TITLE");

    }

    public static void printUnknownCommand() {
        System.out.println("Unknown command; please type help to see what commands you can use.");
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
        System.out.println("The book does not exist; try adding it to the library first.");
    }


    public static void printNotNumberMessage() {
        System.out.println("Please enter the number index of the book.");
    }

    public static void printIncorrectInputFormat() {
        System.out.println("Please enter in the format as mentioned in help.");
    }

    public static void printNoEditChangeException() {
        System.out.println("Please specify what to edit using '/'.");
    }

    public static void printEditedBookConfirmation(String bookName, String oldName) {
        System.out.println("Edited the title of the book: " + oldName + " into " + bookName + "!");
    }

    public static void printDeleteNoCopiesErrorException(String bookName) {
        System.out.println("Automatically deleting " + bookName +
                " due to an error in the inventory.");
    }

    public static void printWrongInputFormat() {
        System.out.println("Please input command in the correct format.");
    }

    public static void printInvalidTxtLine() {
        System.out.println("Skipping a line due to invalid book index");
    }


    public static void printWrongQuantityFormat() {
        System.out.println("Please ensure that you input the proper arguments: \n" +
                "a whole number after '/quantity'.");
    }

    public static void printBlankAddQuantity() {
        System.out.println("Please input how many books you want to add after '/quantity'.");
    }

    public static void printNegativeAddQuantityMessage() {
        System.out.println("You must add at least 1 copy of a book. Please input a number greater than 0.");
    }

    public static void printMaxDeleteNumberMessage() {
        System.out.println("You can only hold and delete up to 1000 copies of a book in total.\n" +
                "Please delete fewer copies instead.");
    }

    public static void printNegativeDeleteQuantityMessage() {
        System.out.println("You must delete at least 1 copy of a book. Please input a number greater than 0.");
    }

    public static void printBlankDeleteQuantity() {
        System.out.println("Please input how many books you want to delete after '/quantity'.");
    }

    public static void printMaxNumberMessage() {
        System.out.println("You can only hold up to 1000 copies of a book in total.\n" +
                "Please add fewer copies instead.");
    }
    public static void printDeletingTooManyBooksMessage(int numberInInventory, int quantityToDelete, String bookTitle) {
        System.out.println("You tried to delete " + quantityToDelete + " copies of " + bookTitle + ".\n"
                + "However, there are currently only " + numberInInventory + " copies in the inventory.");
    }
    public static void printEmptyUserListMessage() {
        System.out.println("The user list is empty, please add a user through the borrow function first");
    }

    public static void printDeleteFewerBooksMessage() {
        System.out.println("Please delete fewer copies instead.");
    }

    public static void printDeleteStatement(int quantity, String bookTitle, int numberInventory, int numberTotal) {
        System.out.println("Deleted " + quantity + " copies of " + bookTitle + "!");
        System.out.println("There are now " + numberInventory + " copies in the library's inventory, and "
                + numberTotal + " copies in total.");
    }
    public static void incorrectFindArgument() {
        System.out.println("please key in a username");
    }
    public static void invalidUser(String userName) {
        System.out.println("user not found: " + userName);
    }
    public static void printCommand(User user, int i) {
        System.out.print(i + 1 + ". ");
        System.out.print(user.getUserBooks().get(i).getName());
        System.out.print(", Borrowed on: ");
        System.out.print(user.getUserBooks().get(i).getBorrowDate());
        System.out.print(", Return by: ");
        System.out.print(user.getUserBooks().get(i).getReturnDate());
    }
    public static void printOverdue() {
        System.out.print(", OVERDUE");
    }
    public static void bookIsOverdue() {
        System.out.println("This book is returned past its return date. Remember to keep track of due dates");
    }


    public static void printInvalidTitleMessage() {
        System.out.println("Please make sure the book title adheres to the format:\n" +
                "Not blank, does not contain only numbers, and does not contain any special characters.");
    }

    public static void printInvalidBorrowDate(String bookTitle) {
        System.out.println("Invalid borrow date format for book: " + bookTitle + ", setting to today's date.");
    }

    public static void printInvalidReturnDueDate(String bookTitle) {
        System.out.println("Invalid return date format for book: " + bookTitle + ", setting to two weeks from today.");
    }

    public static void printInvalidReturnBeforeBorrowDate(String bookTitle) {
        System.out.println("Return date before borrow date for book: " + bookTitle +
                ". Automatically adjusting return date to two weeks after borrow date.");
    }

    public static void printNotExistingUserMessage() {
        System.out.println("No such user exists. Please input the name of an existing user instead.");
    }

    public static void printBookNotBorrowedByUserMessage(String userName) {
        System.out.println(userName + " has not borrowed this book. Nothing to extend.");
    }


    public static void printNoAvailableCopiesInInventoryMessage() {
        System.out.println("There are currently no available copies of the book in the inventory.");
    }

    public static void printUserAlreadyBorrowedBookMessage(String userName) {
        System.out.println(userName + " has already borrowed this book. Please return before borrowing it again.");
    }
      
    public static void printInvalidUserTxtLine() {
        System.out.println("Skipping a line due to invalid line in user.txt");
    }

    public static void printExtensionSuccessMessage(String bookName) {
        System.out.println("The borrowing period for '" + bookName + "' has been successfully extended by one week.");
    }

    public static void printModifyBookQuantityMessage(String bookName) {
        System.out.println("Modifying book quantity of " + bookName + " to sync with total books borrowed by user.");
    }

    public static void printInvalidUserMessage() {
        System.out.println("Skipping a line due to invalid or duplicate user");
    }

    public static void printInvalidBookMessage() {
        System.out.println("Skipping a line due to invalid or duplicate book");
    }

    public static void printFileNotFoundError() {
        System.out.println("File not found!");
    }

    public static void printFileInterruptedError() {
        System.out.println("Access to file is interrupted");
    }

    public static void printDuplicateTitleMessage() {
        System.out.println("Unable to edit book title due to same title as other existing book title");
    }

    public static void printEditSameBookExceptionMessage() {
        System.out.println("Unable to edit book title due to same title as the current book title");
    }

    public static void printInvalidUsernameMessage() {
        System.out.println("Please ensure that the user name does not contain any special characters.");
    }

    public static void exitProgramme() {
        System.out.println("Thank you for using BookMarked! See you soon!");
    }
    /**
     * Prints the list of books of each user
     * Prints an extra line if it is the final user in the list
     * Used by both lisuser and find user class
     * hence placing this function under UI since it prints output and to avoid duplicate
     * @param user      the user that i currently represents
     * @param userCount the number of users
     */
    public static void printUserBooks(User user, int userCount, ArrayList<User> listOfUsers) {
        for (int i = 0; i < user.getUserBooks().size(); i++) {
            Ui.printCommand(user, i);
            if (Book.isOverdue(user.getUserBooks().get(i).getReturnDate())) {
                Ui.printOverdue();
            }
            System.out.println();
        }
        if (userCount < listOfUsers.size() - 1) {
            System.out.println();
        }
    }
}
