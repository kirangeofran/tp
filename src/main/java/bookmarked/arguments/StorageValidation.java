package bookmarked.arguments;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.ui.Ui;

import java.util.ArrayList;

public class StorageValidation {
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

            syncBookQuantity(numberOfBooksBorrowedByIndex, numberOfBookBorrowedInBook, currentBook, oldNumberInInventory);

        }
    }

    private static void syncBookQuantity(int numberOfBooksBorrowedByIndex, int numberOfBookBorrowedInBook, Book currentBook, int oldNumberInInventory) {
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
}
