package bookmarked.storage;

import bookmarked.Book;
import bookmarked.arguments.StorageValidation;
import bookmarked.exceptions.DifferentUserBookStorageException;
import bookmarked.user.User;
import bookmarked.ui.Ui;
import bookmarked.userbook.UserBook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class UserStorage {
    /**
     * Creates a file at the specified path if it does not already exist.
     *
     * @param userDataPath The path where the file is to be created.
     * @return The file object.
     */
    public static File createFile(String userDataPath) {
        File bookDataFile = new File(userDataPath);
        try {
            boolean fileCreated = bookDataFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Sorry, something's wrong, file is not created");
        }

        return bookDataFile;
    }

    /**
     * Reads books from a storage file and returns them as a list.
     *
     * @param userDataFile The file from which to read Book data.
     * @return A list of books read from the file.
     */
    public static ArrayList<User> readFileStorage(File userDataFile, ArrayList<Book> listOfBooks) {
        ArrayList<User> listOfUser = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(userDataFile))) {
            fileReader.lines().forEach(line ->
                    processReadLine(line, listOfUser, listOfBooks));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Access to file is interrupted");
        }

        return listOfUser;
    }

    /**
     * Write all users in listOfUsers to txt file for storage, with a specific format.
     *
     * @param userDataFile The file from which to read Book data.
     * @param listOfUsers The list to which users who borrowed books are kept tracked.
     */
    public static void writeUserToTxt(File userDataFile, ArrayList<User> listOfUsers) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(userDataFile, false));

            for (User user : listOfUsers) {
                fileWriter.write(serializeUser(user));
            }

            fileWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    /**
     * Takes in a user from an array list and convert into a formatted string to be written
     * in txt file for storage.
     *
     * @param user a user from the array list who currently have at least a book borrowed.
     * @return string of user and the book borrowed in a specific format.
     */
    private static String serializeUser(User user) {
        StringBuilder serializedString = new StringBuilder();
        serializedString.append(user.getName());

        ArrayList<UserBook> listOfUserBooks = user.getListOfUserBooks();

        for (UserBook userBooks : listOfUserBooks) {
            serializedString.append(" | ");
            serializedString.append(userBooks.getUserBookIndex());
            serializedString.append(" | ");
            serializedString.append(userBooks.getUserBookTitle());
            serializedString.append(" | ");
            serializedString.append(userBooks.getBorrowDate());
            serializedString.append(" | ");
            serializedString.append(userBooks.getReturnDueDate());
        }
        serializedString.append("\n");

        return serializedString.toString();
    }

    /**
     * Process each line read in txt file, by parsing and adding the users and the list of
     * books they borrowed into listOfUsers for user in the program.
     *
     * @param line A string representing a line of text from the storage file.
     * @param listOfUsers The list to which users who borrowed books are kept tracked.
     */
    private static void processReadLine(String line, ArrayList<User> listOfUsers, ArrayList<Book> listOfBooks) {
        String[] userAttributes = line.split(" \\| ");
        User currentUser = new User(userAttributes[0], listOfBooks);

        if (StorageValidation.isValidLine(listOfUsers, currentUser, userAttributes)) {
            return;
        }

        for (int i = 1; i < userAttributes.length; i += 4) {
            try {
                int bookIndex = Integer.parseInt(userAttributes[i].strip());

                StorageValidation.checkValidBookIndex(listOfBooks, bookIndex);
                StorageValidation.checkValidBookInBookStorage(listOfBooks, bookIndex, userAttributes[i + 1]);

                setBookBorrowDetails(listOfBooks, userAttributes, i, bookIndex, currentUser);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                Ui.printInvalidTxtLine();
                return;
            } catch (DifferentUserBookStorageException e) {
                Ui.printInvalidUserTxtLine();
                return;
            }
        }

        listOfUsers.add(currentUser);
    }

    private static void setBookBorrowDetails(ArrayList<Book> listOfBooks, String[] userAttributes, int i,
                                             int bookIndex, User currentUser) {
        String borrowDateInString = userAttributes[i + 2].strip();
        String returnDueDateInString = userAttributes[i + 3].strip();

        LocalDate borrowDate = getBorrowDate(borrowDateInString, listOfBooks.get(bookIndex));
        LocalDate returnDueDate = getReturnDueDate(returnDueDateInString, listOfBooks.get(bookIndex),
                borrowDate);

        currentUser.borrowBook(bookIndex, borrowDate, returnDueDate);
    }

    private static LocalDate getBorrowDate(String borrowDateInString, Book bookToInput) {
        LocalDate borrowDate;

        try {
            borrowDate = LocalDate.parse(borrowDateInString.strip());
        } catch (DateTimeParseException e) {
            String bookTitle = bookToInput.getName();
            Ui.printInvalidBorrowDate(bookTitle);
            borrowDate = LocalDate.now();
        }

        return borrowDate;
    }

    private static LocalDate getReturnDueDate(String borrowDateInString, Book bookToInput, LocalDate borrowDate) {
        LocalDate returnDueDate;
        String bookTitle = bookToInput.getName();

        try {
            returnDueDate = LocalDate.parse(borrowDateInString);
        } catch (DateTimeParseException e) {
            Ui.printInvalidReturnDueDate(bookTitle);
            returnDueDate = LocalDate.now().plusWeeks(2);
        }

        // Ensure borrow date is before return due date
        if (borrowDate.isAfter(returnDueDate) || borrowDate.isEqual(returnDueDate)) {
            returnDueDate = borrowDate.plusWeeks(2);
            Ui.printInvalidReturnBeforeBorrowDate(bookTitle);
        }

        return returnDueDate;
    }
}
