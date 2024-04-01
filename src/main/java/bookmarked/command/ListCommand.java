package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.ui.Ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class ListCommand extends Command {
    private ArrayList<Book> listOfBooks;
    private String inputCommand;
    private ArrayList<Book> sortedListOfBooks;
    private String[] splitCommand;
    private int numberOfBooks;

    public enum Status {
        DEFAULT, ALPHABETICAL, RETURNDATE, UNBORROWED
    }


    public ListCommand(ArrayList<Book> listOfBooks, String newItem) {
        this.listOfBooks = listOfBooks;
        this.inputCommand = newItem;
        this.numberOfBooks = listOfBooks.size();
    }

    @Override
    public void handleCommand() {
        this.splitCommand = inputCommand.split("/sortby");
        try {
            if (inputCommand.matches("list")) {
                runListBlankCommand();
            } else {
                parseCommand();
            }
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        } catch (EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
        }
    }


    public void parseCommand() throws EmptyListException, EmptyArgumentsException {
        if (this.splitCommand.length <= 1) {
            throw new EmptyArgumentsException();
        }

        switch (splitCommand[1].trim()) {
        case ("alphabetical"):
            runListAlphabeticalCommand();
            break;
        case ("returndate"):
            runListDateCommand();
            break;
        default:
            Ui.printEmptyArgumentsMessage(); //prints an error message for incorrect or empty arguments
        }
    }


    public void runListBlankCommand() throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        System.out.println(printMessage(Status.DEFAULT));
        for (int i = 0; i < numberOfBooks; i++) {
            System.out.println((i + 1) + ". " + this.listOfBooks.get(i).toString());
        }
    }


    public void runListAlphabeticalCommand() throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        System.out.println(printMessage(Status.ALPHABETICAL));
        this.sortedListOfBooks = new ArrayList<>(this.listOfBooks);

        //compares two books by their names, and sorts based on alphabetical order
        this.sortedListOfBooks.sort(Comparator.comparing(Book::getName));

        for (int i = 0; i < numberOfBooks; i++) {
            System.out.println((i + 1) + ". " + this.sortedListOfBooks.get(i).toString());
        }
    }


    public void runListDateCommand() throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        System.out.println(printMessage(Status.RETURNDATE));
        this.sortedListOfBooks = new ArrayList<>(this.listOfBooks);

        //compares two books by their names, and sorts based by return date
        this.sortedListOfBooks.sort(Comparator.comparing(Book::getReturnDate));

        int j = 0;
        for (int i = 0; (i < numberOfBooks); i++) {
            //Checks if the book's return date is the default return date, meaning it's currently unborrowed, and skips
            //over the book for the sorted list
            if (!this.sortedListOfBooks.get(i).getReturnDate().equals(LocalDate.of(1900, 1, 1))) {
                System.out.println((j + 1) + ". " + this.sortedListOfBooks.get(i).toString());
                j++;
            }
        }

        if (j == 0) {
            System.out.println(printMessage(Status.UNBORROWED));
        }
    }


    public String printMessage(Status status) {
        switch (status) {
        case DEFAULT:
            return "Here are all the books currently in the library's inventory!";
        case ALPHABETICAL:
            return "Here are the list of all current books that are currently in\n" +
                    "the library's inventory listed by alphabetical order!\n";
        case RETURNDATE:
            return "Here are the list of all current books that are currently\n" +
                    "being borrowed by users by date of return!\n";
        case UNBORROWED:
            return "There are no currently borrowed books!";
        default:
            return "Error";
        }
    }

}
