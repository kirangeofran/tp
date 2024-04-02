package bookmarked.command;

import bookmarked.Book;
import bookmarked.User;
import bookmarked.exceptions.EmptyArgumentsException;
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
    private String userName;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;

    public BorrowCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, String newItem) throws EmptyArgumentsException {
        assert commandParts != null : "commandParts should not be null";
        assert commandParts.length > 1 : "commandParts should contain at least two elements";
        //this.bookName = String.join(" ", List.of(commandParts).subList(1, commandParts.length));
        String itemUserName = newItem.substring(7);
        String[] splitParts = itemUserName.split ("by");
        assert splitParts.length > 1: "please enter both the borrowed book and userName";
        if (!containsUser (commandParts)) {
            throw new EmptyArgumentsException();
        }
        this.bookName = splitParts[0].trim();
        this.userName = splitParts[1].trim();
        assert listOfBooks != null : "listOfBooks should not be null";
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
    }
    public boolean containsUser(String[] commandParts) {
        for (int i = 0; i <commandParts.length;i ++) {
            if (commandParts[i].equalsIgnoreCase("by")) {
                return true;
            }
        }
        return false;
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
                updateListOfUsers(bookToBorrow, userName);
                System.out.println("Borrowed " + bookToBorrow.getName() + " by " + userName +"!");
                System.out.println("Please return by " + bookToBorrow.getFormattedReturnDate() + ".");
            } else {
                System.out.println("Book is currently unavailable. Expected return date is " +
                        bookToBorrow.getFormattedReturnDate() + ".");
            }
        } else {
            System.out.println("Book not found: " + bookName);
        }
    }
    private void updateListOfUsers(Book book, String userName) {
        for (User user : listOfUsers) {
            if (user.getName().equalsIgnoreCase(userName)) {
                user.borrowedBook(book); // Add the borrowed book to the user's list of borrowed books
                return;
            }
        }
        // If user not found, create a new user and add the borrowed book
        User newUser = new User(userName);
        newUser.borrowedBook(book);
        listOfUsers.add(newUser);
    }
}
