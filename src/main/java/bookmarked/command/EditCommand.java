package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.storage.BookStorage;

import java.io.File;
import java.util.ArrayList;

public class EditCommand extends Command {
    private static final int TITLE_START_INDEX = 7;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;

    private String userInput;

    public EditCommand(String userInput, ArrayList<Book> listOfBooks, File bookDataFile) {
        // Current book details
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.userInput = userInput;
    }

    @Override
    public void handleCommand() {
        String[] splitInput = userInput.split(" ");
        int totalBooks = listOfBooks.size();
        Book bookToEdit = null;
        int bookNumberToEdit = 0;
        String bookName = null;

        try {
            bookNumberToEdit = Integer.parseInt(splitInput[1]);

            bookToEdit = listOfBooks.get(bookNumberToEdit - 1);

            assert bookNumberToEdit > 0 : "bookNumberToEdit must be an integer greater than 0";
            assert bookNumberToEdit <= totalBooks : "bookNumberToEdit must be an integer " +
                    "less than or equals to the total number of books in library";

            if (userInput.contains("/title")) {
                int titleIndex = userInput.indexOf("/title");
                int nextSlash = userInput.indexOf("/", titleIndex + TITLE_START_INDEX);

                if (nextSlash == -1) {
                    bookName = userInput.substring(titleIndex + TITLE_START_INDEX);
                } else {
                    bookName = userInput.substring(titleIndex + TITLE_START_INDEX, nextSlash);
                }

                if (bookName.isBlank()) {
                    throw new EmptyArgumentsException();
                }

                bookToEdit.setName(bookName);
                BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
                System.out.println("Edited Book: " + bookNumberToEdit);
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a book number in integer format");
        } catch (StringIndexOutOfBoundsException | EmptyArgumentsException e) {
            System.out.println("Sorry, description cannot be empty");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Sorry, no book number of: " + bookNumberToEdit);
        } catch (NullPointerException e) {
            System.out.println("Please enter in the format as mentioned in help");
        }
    }
}
