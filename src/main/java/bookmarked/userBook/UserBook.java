package bookmarked.userbook;

import java.time.LocalDate;
import java.util.ArrayList;

public class UserBook {
    private Integer userBookIndex;
    private String userBookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDueDate;

    public UserBook(Integer userBookIndex, String userBookTitle, LocalDate borrowDate, LocalDate returnDueDate) {
        this.userBookIndex = userBookIndex;
        this.userBookTitle = userBookTitle;
        this.borrowDate = borrowDate;
        this.returnDueDate = returnDueDate;
    }

    public void setUserBookTitle(String userBookTitle) {
        this.userBookTitle = userBookTitle;
    }
    public void setReturnDueDate(LocalDate returnDate) {
        this.returnDueDate = returnDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public LocalDate getReturnDueDate() {
        return this.returnDueDate;
    }

    public Integer getUserBookIndex() {
        return this.userBookIndex;
    }

    public String getUserBookTitle() {
        return this.userBookTitle;
    }

    @Override
    public String toString() {
        ArrayList<String> userBookDetails = new ArrayList<>();
        userBookDetails.add(String.valueOf(this.userBookIndex));
        userBookDetails.add(this.userBookTitle);
        userBookDetails.add(String.valueOf(borrowDate));
        userBookDetails.add(String.valueOf(returnDueDate));
        return String.valueOf(userBookDetails);
    }
}
