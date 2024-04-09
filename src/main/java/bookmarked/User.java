package bookmarked;

import java.util.ArrayList;

public class User {
    private String description;
    private ArrayList<Book> listOfBooks;
    private ArrayList<Integer> userBooksIndex;

    public User(String description, ArrayList<Book> listOfBooks) {
        this.description = description;
        this.userBooksIndex = new ArrayList<>();
        this.listOfBooks = listOfBooks;
    }

    public String getName() {
        return this.description;
    }

    public ArrayList<Book> getUserBooks() {
        return this.userBooks;
    }
    public void borrowedBook (Book book) {
        this.userBooks.add(book);
    }
    public void unborrowBook (Book book) {
        this.userBooks.remove(book);
    }
    @Override
    public String toString() {
        return (this.description + getUserBooks());
    }

}
