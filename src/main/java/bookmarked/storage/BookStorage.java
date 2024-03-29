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
        if (!bookDataFile.exists()) {
            try {
                bookDataFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Sorry, something's wrong, file is not created");
            }
        }
        return bookDataFile;
    }


    public static ArrayList<Book> readFileStorage(File bookDataFile) {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(bookDataFile))) {
            String currentTextLine;
            while ((currentTextLine = fileReader.readLine()) != null) {
                addToArrayList(currentTextLine, listOfBooks);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Access to file is interrupted");
        }

        return listOfBooks;
    }

    public static void writeBookToTxt(File bookDataFile, ArrayList<Book> listOfBooks) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bookDataFile, false))) {
            for (Book currentBook : listOfBooks) {
                String bookTitle = currentBook.getName();
                String bookBorrowStatus = currentBook.getIsBorrowed() ? "True" : "False";
                String borrowDate = (currentBook.getBorrowDate() != null) ?
                        currentBook.getBorrowDate().toString() : "null";
                String returnDate = (currentBook.getReturnDate() != null) ?
                        currentBook.getReturnDate().toString() : "null";
                fileWriter.write(String.format("%s | %s | %s | %s%n",
                        bookTitle, bookBorrowStatus, borrowDate, returnDate));
            }
            fileWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    private static void addToArrayList(String currentTextLine, ArrayList<Book> listOfBooks) {
        String[] splitTextLine;
        splitTextLine = currentTextLine.split(" \\| ");
        if (splitTextLine.length < 4) {
            System.out.println("Skipping malformatted line: " + currentTextLine);
            return;
        }

        String title = splitTextLine[0];

        boolean isBorrowed = "True".equalsIgnoreCase(splitTextLine[1]);
        LocalDate borrowDate = !"null".equals(splitTextLine[2]) ? LocalDate.parse(splitTextLine[2]) : null;
        LocalDate returnDate = !"null".equals(splitTextLine[3]) ? LocalDate.parse(splitTextLine[3]) : null;

        Book currentBook = new Book(title);

        if (isBorrowed) {
            currentBook.setBorrowed();
            currentBook.setBorrowDate(borrowDate);
            currentBook.setReturnDate(returnDate);
        }

        listOfBooks.add(currentBook);
    }
}
