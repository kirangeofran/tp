package bookmarked.storage;

import bookmarked.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;


/**
 * This class provides storage functionalities for Book objects.
 */
public class BookStorage {

    /**
     * Creates a file at the specified path if it does not already exist.
     *
     * @param bookDataPath The path where the file is to be created.
     * @return The file object.
     */
    public static File createFile(String bookDataPath) {
        File bookDataFile = new File(bookDataPath);
        try {
            boolean fileCreated = bookDataFile.createNewFile();
            if (fileCreated) {
                System.out.println("New file created: " + bookDataFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Sorry, something's wrong, file is not created");
        }

        return bookDataFile;
    }

    /**
     * Reads books from a storage file and returns them as a list.
     *
     * @param bookDataFile The file from which to read Book data.
     * @return A list of books read from the file.
     */
    public static ArrayList<Book> readFileStorage(File bookDataFile) {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(bookDataFile))) {
            fileReader.lines().forEach(line -> parseLineAndAddToBooks(line, listOfBooks));
            // After reading and potentially modifying the books, immediately write them back to save changed.
            writeBookToTxt(bookDataFile, listOfBooks); // Add this line

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Access to file is interrupted");
        }

        return listOfBooks;
    }

    /**
     * Writes a list of books to a specified file.
     *
     * @param bookDataFile The file to which the books should be written.
     * @param listOfBooks  The list of books to write.
     */
    public static void writeBookToTxt(File bookDataFile, ArrayList<Book> listOfBooks) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bookDataFile, false));

            for (Book book : listOfBooks) {
                fileWriter.write(serializeBook(book));
            }

            fileWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    /**
     * Parses a line of text into a Book object and adds it to the provided list.
     *
     * @param line  A string representing a line of text from the storage file.
     * @param books The list to which the parsed Book will be added.
     */
    private static void parseLineAndAddToBooks(String line, ArrayList<Book> books) {
        String[] bookAttributes = line.split(" \\| ");
        if (bookAttributes.length < 4) {
            System.out.println("Skipping malformatted line: " + line);
            return;
        }

        Book book = createBookFromAttributes(bookAttributes);
        if (book == null) {
            return;
        }
        books.add(book);
    }

    /**
     * Creates a Book object from an array of String attributes.
     *
     * @param bookAttributes An array of strings representing the book's attributes.
     * @return A Book object or null if the input is malformed.
     */

    private static Book createBookFromAttributes(String[] bookAttributes) {
        String title = bookAttributes[0];
        int bookNumberTotal;
        int bookNumberBorrowed;
        int bookNumberInInventory;

        try {
            bookNumberTotal = Integer.parseInt(bookAttributes[1]);
            bookNumberBorrowed = Integer.parseInt(bookAttributes[2]);
            bookNumberInInventory = Integer.parseInt(bookAttributes[3]);

            // check if bookNumberBorrowed + bookNumberInInventory = bookNumberTotal
            if (bookNumberBorrowed + bookNumberInInventory < bookNumberTotal) {
                bookNumberInInventory = bookNumberTotal - bookNumberBorrowed;
            } else if (bookNumberBorrowed + bookNumberInInventory > bookNumberTotal) {
                bookNumberTotal = bookNumberBorrowed + bookNumberInInventory;
            }

        } catch (NumberFormatException e) {
            System.out.println("Skipping malformatted line due to invalid quantity");
            return null;
        }

        Book book = setBookDetails(title, bookNumberTotal, bookNumberBorrowed, bookNumberInInventory);

        return book;
    }

    private static Book setBookDetails(String title, int bookNumberTotal, int bookNumberBorrowed, int bookNumberInInventory) {
        Book book = new Book(title);
        book.setNumberTotal(bookNumberTotal);
        book.setNumberBorrowed(bookNumberBorrowed);
        book.setNumberInInventory(bookNumberInInventory);
        return book;
    }

    /**
     * Serializes a Book object into a string representation for file storage.
     *
     * @param book The Book object to serialize.
     * @return A string representation of the Book object.
     */
    private static String serializeBook(Book book) {
        String bookTitle = book.getName();
        int bookNumberTotal = book.getNumberTotal();
        int bookNumberBorrowed = book.getNumberBorrowed();
        int bookNumberInInventory = book.getNumberInInventory();

        return String.format("%s | %d | %d | %d%n", bookTitle, bookNumberTotal,
                bookNumberBorrowed, bookNumberInInventory);
    }

    /**
     * Parses a date from a string unless the string represents a null value.
     *
     * @param dateString The string to parse.
     * @return The LocalDate object or null if the string represents a null value.
     */
    private static LocalDate parseDate(String dateString) {
        return !"null".equals(dateString) ? LocalDate.parse(dateString) : null;
    }

}
