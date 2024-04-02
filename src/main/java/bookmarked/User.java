package bookmarked;
import java.util.ArrayList;
public class User {
    protected String description;
    protected ArrayList<Book> userBooks;

    public User(String description) {
        this.description = description;
        this.userBooks = new ArrayList<>();

    }

    public String getName() {
        return this.description;
    }

    public ArrayList<Book> getUserBooks() {
        return userBooks;
    }
    public void borrowedBook (Book book) {
        userBooks.add(book);
    }
    public void unborrowBook (Book book) {
        userBooks.remove(book);
    }
    @Override
    public String toString() {
        return (this.description + getUserBooks());
    }

}