package bookmarked;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Book {

    private static final int EXTENSION_DAYS = 7;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final LocalDate DEFAULT_RETURNED_DATE = LocalDate.of(1900, 1, 1);
    public boolean isBorrowed;
    private String description;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private int numberInInventory;
    private int numberBorrowed;
    private int numberTotal;


    public Book(String description) {
        this.description = description;
        this.isBorrowed = false;
        this.borrowDate = null;
        this.returnDate = DEFAULT_RETURNED_DATE;
    }

    public String getName() {
        return this.description;
    }

    public boolean isAvailable() {
        return this.numberInInventory > 0;
    }

    public void borrowBook(LocalDate borrowDate, Period borrowPeriod) {
        if (isAvailable()) {
            this.isBorrowed = true;
            this.numberInInventory--;
            this.numberBorrowed++;
            this.borrowDate = borrowDate;
            this.returnDate = borrowDate.plus(borrowPeriod);
        }
    }

    public void extendDueDate() {
        if (this.isBorrowed) {
            this.returnDate = this.returnDate.plusDays(EXTENSION_DAYS);
        }
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setReturned() {
        this.isBorrowed = false;
        this.borrowDate = null;
        this.returnDate = DEFAULT_RETURNED_DATE;
        this.numberInInventory++;
        this.numberBorrowed--;
    }

    public void setName(String bookName) {
        this.description = bookName;
    }

    public String getFormattedReturnDate() {
        return isBorrowed ? returnDate.format(DATE_FORMATTER) : "Not borrowed";
    }

    public String getBorrowedStatus() {
        return (isBorrowed ? ", borrowed" : "available"); // mark done task with X
    }

    public void setNumberInInventory(int newNumber) {
        this.numberInInventory = newNumber;
    }

    public int getNumberInInventory() {
        return this.numberInInventory;
    }

    public void setNumberBorrowed(int newNumber) {
        this.numberBorrowed = newNumber;
    }

    public int getNumberBorrowed() {
        return this.numberBorrowed;
    }

    public void setNumberTotal(int newNumber) {
        this.numberTotal = newNumber;
    }

    public int getNumberTotal() {
        return this.numberTotal;
    }

    @Override
    public String toString() {
        String formattedNumberInventoryBorrowed = "Number of books in inventory: " + this.numberInInventory
                + ". Number of books borrowed: " + this.numberBorrowed;
        return this.description + ". " + formattedNumberInventoryBorrowed + ".";
    }

    @Override
    public boolean equals(Object obj) {
        Book otherBook = (Book) obj;
        return Objects.equals(this.description, otherBook.getName());
    }

    public static boolean isOverdue(LocalDate returnDate) {
        LocalDate dateToday = LocalDate.now();
        int compareDate = dateToday.compareTo(returnDate);
        if (compareDate > 0) {
            return true;
        }
        return false;
    }

}
