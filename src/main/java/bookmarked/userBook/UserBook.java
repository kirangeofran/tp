package bookmarked.userBook;

import java.time.LocalDate;

public class UserBook {
    private Integer userBookIndex;
    protected LocalDate borrowDate;
    protected LocalDate returnDueDate;

    public UserBook(Integer userBookIndex, LocalDate borrowDate, LocalDate returnDueDate) {
        this.userBookIndex = userBookIndex;
        this.borrowDate = borrowDate;
        this.returnDueDate = returnDueDate;
    }

    public void setReturnDueDate(LocalDate returnDate) {
        this.returnDueDate = returnDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDueDate() {
        return returnDueDate;
    }

    public Integer getUserBookIndex() {
        return userBookIndex;
    }
}
