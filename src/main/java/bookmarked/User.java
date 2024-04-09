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

    public void borrowedBook(Integer bookIndex) {
        this.userBooksIndex.add(bookIndex);
    }
    public void unborrowBook(Integer bookIndex) {
        this.userBooksIndex.remove(bookIndex);
    }

    public void setListOfBooks(ArrayList<Book> listOfBooks) {
        this.listOfBooks = listOfBooks;
    }

    @Override
    public String toString() {
        return (this.description + getUserBooks());
    }

}
