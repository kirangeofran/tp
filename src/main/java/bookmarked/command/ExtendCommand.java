package bookmarked.command;

import bookmarked.Book;
import java.io.File;

import bookmarked.storage.BookStorage;
import java.util.ArrayList;


public class ExtendCommand extends Command {
    private String bookName;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;

    public ExtendCommand(String bookName, ArrayList<Book> listOfBooks, File bookDataFile) {
        this.bookName = bookName;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
    }

    @Override
    public void handleCommand() {
        for (Book book : listOfBooks) {
            if (book.getName().equalsIgnoreCase(bookName) && book.getIsBorrowed()) {
                book.extendDueDate(); // extend due date by 7 days
                System.out.println("Due date for " + bookName + " has been extended by a week.");
                BookStorage.writeBookToTxt(bookDataFile, listOfBooks); // Save the updated due date
                return;
            }
        }
        System.out.println("Book not found or not borrowed: " + bookName);
    }
}
