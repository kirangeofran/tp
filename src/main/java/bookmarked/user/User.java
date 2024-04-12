package bookmarked.user;

import bookmarked.Book;
import bookmarked.userBook.UserBook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String description;
    private ArrayList<Book> listOfBooks;
    private ArrayList<UserBook> listOfUserBooks;
    private ArrayList<Integer> userBooksIndex;

    public User(String description, ArrayList<Book> listOfBooks) {
        this.description = description;
        this.userBooksIndex = new ArrayList<>();
        this.listOfBooks = listOfBooks;
        this.listOfUserBooks = new ArrayList<>();
    }

    public String getName() {
        return this.description;
    }

    public ArrayList<Book> getUserBooks() {
        ArrayList<Book> userBooks = new ArrayList<>();
        for (int i = 0; i < userBooksIndex.size(); i += 1) {
            int currentBookIndex = userBooksIndex.get(i);
            userBooks.add(listOfBooks.get(currentBookIndex - 1));
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
        UserBook bookToBorrow = new UserBook(bookIndex, borrowDate, returnDueDate);
        this.listOfUserBooks.add(bookToBorrow);
    }

    public void unborrowBook(Integer bookIndex) {
        this.userBooksIndex.remove(bookIndex);
        UserBook bookToReturn = null;
        for (int i = 0; i < listOfUserBooks.size(); i += 1) {
            UserBook currentBook = listOfUserBooks.get(i);
            if (Objects.equals(currentBook.getUserBookIndex(), bookIndex)) {
                bookToReturn = currentBook;
            }
        }
        this.listOfUserBooks.remove(bookToReturn);
    }

    public void setListOfBooks(ArrayList<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    @Override
    public String toString() {
        return (this.description + getUserBooks());
    }

}
