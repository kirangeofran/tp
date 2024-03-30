package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyListException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowCommand extends Command {

    private static final Period DEFAULT_BORROW_PERIOD = Period.ofWeeks(2);
    private String bookName;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;

    public BorrowCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile) {
        assert commandParts != null : "commandParts should not be null";
        assert commandParts.length > 1 : "commandParts should contain at least two elements";
        this.bookName = String.join(" ", List.of(commandParts).subList(1, commandParts.length));
        assert listOfBooks != null : "listOfBooks should not be null";
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
    }

    @Override
    public void handleCommand() {
        // Find the book with the matching name
        List<Book> foundBooks = listOfBooks.stream()
                .filter(book -> book.getName().equalsIgnoreCase(bookName))
                .collect(Collectors.toList());
        try {
            runBorrowCommand(foundBooks);
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }

    public void runBorrowCommand(List<Book> foundBooks) throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        if (!foundBooks.isEmpty()) {
            Book bookToBorrow = foundBooks.get(0);
            if (bookToBorrow.isAvailable()) {
                bookToBorrow.borrowBook(LocalDate.now(), DEFAULT_BORROW_PERIOD);
                System.out.println("Borrowed " + bookToBorrow.getName() + "!");
                System.out.println("Please return by " + bookToBorrow.getFormattedReturnDate() + ".");
            } else {
                System.out.println("Book is currently unavailable. Expected return date is " +
                        bookToBorrow.getFormattedReturnDate() + ".");
            }
        } else {
            System.out.println("Book not found: " + bookName);
        }
    }
}
