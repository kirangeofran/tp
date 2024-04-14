package bookmarked.arguments;

import bookmarked.Book;
import bookmarked.exceptions.DifferentUserBookStorageException;
import bookmarked.user.User;
import bookmarked.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class StorageValidation {
    public static boolean isValidLine(ArrayList<User> listOfUsers, User currentUser, String[] userAttributes) {
        // check valid user
        if (listOfUsers.contains(currentUser) || isValidDate(userAttributes[0])) {
            Ui.printInvalidUserMessage();
            return true;
        }

        int userAttributesLength = userAttributes.length - 1;

        // if incomplete data
        if (userAttributesLength == 0 || userAttributesLength % 4 != 0) {
            Ui.printInvalidUserTxtLine();
            return true;
        }
        return false;
    }

    private static boolean isValidDate(String dateInString) {
        try {
            LocalDate date = LocalDate.parse(dateInString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void validateUsersAndBooksLists(ArrayList<User> listOfUsers, ArrayList<Book> listOfBooks) {
        for (int i = 0; i < listOfBooks.size(); i += 1) {
            Book currentBook = listOfBooks.get(i);
            int numberOfBooksBorrowedByIndex = 0;
            for (User currentUser : listOfUsers) {
                if (currentUser.getUserBooksIndex().contains(i)) {
                    numberOfBooksBorrowedByIndex += 1;
                }
            }

            int numberOfBookBorrowedInBook = currentBook.getNumberBorrowed();
            int oldNumberInInventory = currentBook.getNumberInInventory();

            syncBookQuantity(numberOfBooksBorrowedByIndex, numberOfBookBorrowedInBook,
                    currentBook, oldNumberInInventory);

        }
    }

    private static void syncBookQuantity(int numberOfBooksBorrowedByIndex, int numberOfBookBorrowedInBook,
                                         Book currentBook, int oldNumberInInventory) {
        if (numberOfBooksBorrowedByIndex > numberOfBookBorrowedInBook) {
            // change number of books borrowed and total books in book storage to follow user storage
            currentBook.setNumberBorrowed(numberOfBooksBorrowedByIndex);
            currentBook.setNumberTotal(oldNumberInInventory + numberOfBooksBorrowedByIndex);
            Ui.printModifyBookQuantityMessage(currentBook.getName());
        }

        if (numberOfBooksBorrowedByIndex < numberOfBookBorrowedInBook) {
            // set the borrow in book storage to follow user storage
            // take the difference and add it to inventory number
            int differenceInBorrowed = numberOfBookBorrowedInBook - numberOfBooksBorrowedByIndex;
            currentBook.setNumberBorrowed(numberOfBooksBorrowedByIndex);
            currentBook.setNumberInInventory(oldNumberInInventory + differenceInBorrowed);
            Ui.printModifyBookQuantityMessage(currentBook.getName());
        }
    }

    public static void checkValidBookInBookStorage(ArrayList<Book> listOfBooks, int bookIndex, String bookTitle)
            throws DifferentUserBookStorageException {
        if (!listOfBooks.get(bookIndex).getName().equals(bookTitle)) {
            throw new DifferentUserBookStorageException();
        }
    }

    public static void checkValidBookIndex(ArrayList<Book> listOfBooks, int bookIndex) {
        if (bookIndex < 0 || bookIndex > listOfBooks.size()) {
            throw new IndexOutOfBoundsException();
        }
    }
}
