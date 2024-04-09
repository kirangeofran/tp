package bookmarked;

import java.util.ArrayList;

public class User {
    private String description;
    private ArrayList<Book> userBooks;

    public User(String description) {
        this.description = description;
        this.userBooks = new ArrayList<>();

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
