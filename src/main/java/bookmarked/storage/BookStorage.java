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
            // After reading and potentially modifying the books, immediately write them back to ensure changes are saved.
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
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bookDataFile, false))) {
            for (Book book : listOfBooks) {
                fileWriter.write(serializeBook(book));
            }
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
        books.add(book);
    }

    /**
     * Creates a Book object from an array of String attributes.
     *
     * @param bookAttributes An array of strings representing the book's attributes.
     * @return A Book object or null if the input is malformed.
     */

    private static Book createBookFromAttributes(String[] bookAttributes) {
        if (bookAttributes.length <4){
            System.out.println("Skipping malformatted line due to insufficient attributes.");
            return null;
        }

        String title = bookAttributes[0];
        boolean isBorrowed = Boolean.parseBoolean(bookAttributes[1]);
        LocalDate borrowDate = parseDate(bookAttributes[2]);
        LocalDate returnDate = parseDate(bookAttributes[3]);

        Book book = new Book(title);
        if (isBorrowed) {
            book.setBorrowed();
            book.setBorrowDate(borrowDate);
            book.setReturnDate(returnDate);

            try {
                // Check if the return date is before the borrow date
                if (returnDate != null && borrowDate != null && returnDate.isBefore(borrowDate)) {
                    throw new IllegalArgumentException("Return date cannot be before borrow date for book: " + title);
                }
                book.setReturnDate(returnDate);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + ". Automatically setting return date to two weeks after the borrow date.");
                // Automatically set the return date to two weeks after the borrow date
                returnDate = borrowDate.plusWeeks(2);
                book.setReturnDate(returnDate);
            }
        }
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
        String bookBorrowStatus = book.getIsBorrowed() ? "True" : "False";
        String borrowDate = (book.getBorrowDate() != null) ? book.getBorrowDate().toString() : "null";
        String returnDate = (book.getReturnDate() != null) ? book.getReturnDate().toString() : "null";
        String formattedString =
                String.format("%s | %s | %s | %s%n", bookTitle, bookBorrowStatus, borrowDate, returnDate);
        return formattedString; // Return the formatted string
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
