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

public class BookStorage {

    public static File createFile(String bookDataPath) {
        File bookDataFile = new File(bookDataPath);
        try{
            if (bookDataFile.createNewFile()) {
                System.out.println("New file created: " + bookDataFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Sorry, something's wrong, file is not created");
        }

        return bookDataFile;
    }

    public static ArrayList<Book> readFileStorage(File bookDataFile) {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(bookDataFile))) {
            fileReader.lines().forEach(line -> parseLineAndAddToBooks(line, listOfBooks));

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Access to file is interrupted");
        }

        return listOfBooks;
    }

    public static void writeBookToTxt(File bookDataFile, ArrayList<Book> listOfBooks) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bookDataFile, false))) {
            for (Book book : listOfBooks) {
                fileWriter.write(serializeBook(book));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    private static void parseLineAndAddToBooks(String line, ArrayList<Book> books) {
        String[] tokens = line.split(" \\| ");
        if (tokens.length < 4) {
            System.out.println("Skipping malformatted line: " + line);
            return;
        }
        Book book = createBookFromTokens(tokens);
        books.add(book);
    }

    private static Book createBookFromTokens(String[] tokens) {
        String title = tokens[0];
        boolean isBorrowed = Boolean.parseBoolean(tokens[1]);
        LocalDate borrowDate = parseDate(tokens[2]);
        LocalDate returnDate = parseDate(tokens[3]);

        Book book = new Book(title);
        if (isBorrowed) {
            book.setBorrowed();
            book.setBorrowDate(borrowDate);
            book.setReturnDate(returnDate);
        }
        return book;
    }

    private static String serializeBook(Book book) {
        String bookTitle = book.getName();
        String bookBorrowStatus = book.getIsBorrowed() ? "True" : "False";
        String borrowDate = (book.getBorrowDate() != null) ? book.getBorrowDate().toString() : "null";
        String returnDate = (book.getReturnDate() != null) ? book.getReturnDate().toString() : "null";
        String formattedString =
                String.format("%s | %s | %s | %s%n", bookTitle, bookBorrowStatus, borrowDate, returnDate);
        return formattedString; // Return the formatted string
    }

    private static LocalDate parseDate(String dateString) {
        return !"null".equals(dateString) ? LocalDate.parse(dateString) : null;
    }

}
