package bookmarked.user;

import bookmarked.Book;
import bookmarked.userBook.UserBook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    private static final int EXTENSION_DAYS = 7;
    private String userName;
    private ArrayList<Book> listOfBooks;
    private ArrayList<UserBook> listOfUserBooks;
    private ArrayList<Integer> userBooksIndex;

    public User(String userName, ArrayList<Book> listOfBooks) {
        this.userName = userName;
        this.userBooksIndex = new ArrayList<>();
        this.listOfBooks = listOfBooks;
        this.listOfUserBooks = new ArrayList<>();
    }

    public String getName() {
        return this.userName;
    }

    public ArrayList<Book> getUserBooks() {
        ArrayList<Book> userBooks = new ArrayList<>();

        for (int i = 0; i < this.userBooksIndex.size(); i += 1) {
            int currentBookIndex = this.userBooksIndex.get(i);
            userBooks.add(this.listOfBooks.get(currentBookIndex));  // check this -1
        }

        return userBooks;
    }

    public ArrayList<Integer> getUserBooksIndex() {
        return this.userBooksIndex;
    }

    public ArrayList<UserBook> getListOfUserBooks() {
        return this.listOfUserBooks;
    }

    public void borrowBook(Integer bookIndex, LocalDate borrowDate, LocalDate returnDueDate) {
        this.userBooksIndex.add(bookIndex);
        String bookTitleToBorrow = listOfBooks.get(bookIndex).getName();
        UserBook bookToBorrow = new UserBook(bookIndex, bookTitleToBorrow, borrowDate, returnDueDate);
        this.listOfUserBooks.add(bookToBorrow);
    }

    public void unborrowBook(Integer bookIndex) {
        this.userBooksIndex.remove(bookIndex);
        UserBook bookToReturn = null;
        for (int i = 0; i < this.listOfUserBooks.size(); i += 1) {
            UserBook currentBook = this.listOfUserBooks.get(i);
            if (Objects.equals(currentBook.getUserBookIndex(), bookIndex)) {
                bookToReturn = currentBook;
            }
        }
        this.listOfUserBooks.remove(bookToReturn);
    }

    public void editBook(String oldTitle, String newTitle, ArrayList<UserBook> currentUserBooksList) {
        for (UserBook currentUserBook : currentUserBooksList) {
            String currentUserBookTitle = currentUserBook.getUserBookTitle();
            if (currentUserBookTitle.equals(oldTitle)) {
                currentUserBook.setUserBookTitle(newTitle);
            }
        }
    }

    public void extendDueDate(Integer bookIndex) {
        for (int i = 0; i < this.listOfUserBooks.size(); i += 1) {
            UserBook currentUserBook = this.listOfUserBooks.get(i);
            LocalDate currentReturnDueDate = currentUserBook.getReturnDueDate();
            currentUserBook.setReturnDueDate(currentReturnDueDate.plusDays(EXTENSION_DAYS));
        }
    }

    public void setListOfBooks(ArrayList<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    @Override
    public String toString() {
        return (this.userName + getUserBooks());
    }

    @Override
    public boolean equals(Object obj) {
        User otherUser = (User) obj;
        return Objects.equals(this.userName, otherUser.getName());
    }
}
