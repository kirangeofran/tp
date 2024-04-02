package bookmarked;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import bookmarked.User;

public class Book {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    protected String description;
    protected boolean isBorrowed;
    protected LocalDate borrowDate;
    protected LocalDate returnDate;
    protected String user;




    public Book(String description) {
        this.description = description;
        this.isBorrowed = false;
        this.borrowDate = null;
        this.returnDate = null;
    }

    public String getName() {
        return this.description;
    }

    public boolean isAvailable() {
        return !isBorrowed;
    }

    public void borrowBook(LocalDate borrowDate, Period borrowPeriod) {
        if (isAvailable()) {
            this.isBorrowed = true;
            this.borrowDate = borrowDate;
            this.returnDate = borrowDate.plus(borrowPeriod);
        }
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean getIsBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed() {
        this.isBorrowed = true;
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
        this.returnDate = null;
    }

    public String getFormattedReturnDate() {
        return returnDate != null ? returnDate.format(DATE_FORMATTER) : "Not borrowed";
    }

    public String getBorrowedStatus() {
        return (isBorrowed ? ", borrowed" : "available"); // mark done task with X
    }


    @Override
    public String toString() {
        if (isBorrowed) {
            String formattedBorrowDate =
                    (borrowDate != null) ? borrowDate.format(DATE_FORMATTER) : "Not set";
            String formattedReturnDate = getFormattedReturnDate();
            return String.format(
                    "%s, borrowed on: %s, due on: %s", description, formattedBorrowDate,
                    formattedReturnDate);
        } else {
            return description + " available";
        }
    }

}





