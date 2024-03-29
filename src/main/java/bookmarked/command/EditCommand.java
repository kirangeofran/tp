package bookmarked.command;

import bookmarked.Book;
import bookmarked.storage.BookStorage;

import java.io.File;
import java.util.ArrayList;

public class EditCommand extends Command {
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;

    private String userInput;
    private ArrayList<Book> editedListOfBooks;

    private static final int TITLE_START_INDEX = 7;

    public EditCommand(String userInput, ArrayList<Book> listOfBooks, File bookDataFile) {
        // Current book details
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.userInput = userInput;
    }

    @Override
    public void handleCommand() {
        String[] splitInput = userInput.split(" ");
        int bookNumberToEdit = Integer.parseInt(splitInput[1]);

        Book bookToEdit = listOfBooks.get(bookNumberToEdit - 1);
        String bookName = bookToEdit.getName();

        if (userInput.contains("/title")) {
            int titleIndex = userInput.indexOf("/title");
            int nextSlash = userInput.indexOf("/", titleIndex + TITLE_START_INDEX);

            if (nextSlash == -1) {
                bookName = userInput.substring(titleIndex + TITLE_START_INDEX);
            } else {
                bookName = userInput.substring(titleIndex + TITLE_START_INDEX, nextSlash);
            }
        }

        bookToEdit.setName(bookName);

        BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
    }
}
