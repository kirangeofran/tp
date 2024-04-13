package bookmarked;

import bookmarked.parser.Parser;
import bookmarked.storage.UserStorage;
import bookmarked.ui.Ui;
import bookmarked.storage.BookStorage;
import bookmarked.user.User;
import bookmarked.arguments.StorageValidation;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class BookMarked {
    public static ArrayList<Book> listOfBooks = new ArrayList<>();
    public static ArrayList<User> listOfUsers = new ArrayList<>();
    private static final String BOOK_FILE_PATH = "./book.txt";
    private static final String USER_FILE_PATH = "./user.txt";

    public static void main(String[] args) {
        Ui.greetings();

        File bookDataFile = BookStorage.createFile(BOOK_FILE_PATH);
        listOfBooks = BookStorage.readFileStorage(bookDataFile);

        File userDataFile = UserStorage.createFile(USER_FILE_PATH);
        listOfUsers = UserStorage.readFileStorage(userDataFile, listOfBooks);
        UserStorage.writeUserToTxt(userDataFile, listOfUsers);

        StorageValidation.validateUsersAndBooksLists(listOfUsers, listOfBooks);

        Scanner in = new Scanner(System.in);
        String newItem = in.nextLine();

        Parser.runCommand(newItem, in, listOfBooks, bookDataFile, listOfUsers, userDataFile);
    }
}
