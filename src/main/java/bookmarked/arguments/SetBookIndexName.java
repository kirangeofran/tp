package bookmarked.arguments;

import bookmarked.Book;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.IndexOutOfListBounds;

import java.util.ArrayList;

public class SetBookIndexName {
    private int bookIndex;
    private String bookName;
    private String inputArgument;
    private ArrayList<Book> listOfBooks;

    public SetBookIndexName(String inputArgument, ArrayList<Book> listOfBooks) {
        this.inputArgument = inputArgument;
        this.listOfBooks = listOfBooks;
    }


    public void setArguments() throws IndexOutOfListBounds, BookNotFoundException {
        boolean isInputIndex;
        try {
            this.bookIndex = checkBookIndexValidity();
            this.bookName = listOfBooks.get(this.bookIndex).getName();
            isInputIndex = true;
        } catch (NumberFormatException e) {
            this.bookName = (inputArgument);
            isInputIndex = false;
        } catch (IndexOutOfListBounds e) {
            throw new IndexOutOfListBounds();
        }

        if (!isInputIndex) {
            try {
                updateBookIndex();
            } catch (BookNotFoundException e) {
                throw new BookNotFoundException();
            }
        }
    }


    public int checkBookIndexValidity() throws IndexOutOfListBounds {
        int bookIndex = Integer.parseInt(this.inputArgument);
        if (bookIndex < 0 || bookIndex > this.listOfBooks.size()) {
            throw new IndexOutOfListBounds();
        }
        return bookIndex - 1;
    }


    private void updateBookIndex() throws BookNotFoundException {
        if (!doesBookExists()) {
            throw new BookNotFoundException();
        }

        for (int i = 0; i < this.listOfBooks.size(); i += 1) {
            String currentBookName = this.listOfBooks.get(i).getName();
            if (currentBookName.equals(bookName)) {
                this.bookIndex = i;
            }
        }
    }


    public boolean doesBookExists() {
        for (Book book : listOfBooks) {
            if (book.getName().matches(bookName)) {
                return true;
            }
        }
        return false;
    }


    public int getBookIndex() {
        return this.bookIndex;
    }

    public String getBookName() {
        return this.bookName;
    }


}
