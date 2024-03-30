package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyListException;
import bookmarked.ui.Ui;

import java.util.ArrayList;
import java.util.Comparator;

public class ListUserCommand extends Command {
    private ArrayList<Book> listOfUsers;
    private String inputCommand;
    private String[] splitCommand;
    private int numberOfUsers;


    public ListUserCommand(ArrayList<Book> listOfUsers, String newItem) {
        this.listOfUsers = listOfUsers;
        this.inputCommand = newItem;
        this.numberOfUsers = listOfUsers.size();
    }

    @Override
    public void handleCommand() {
        this.splitCommand = inputCommand.split("listuser ");

        //This will be updated to switch/case when the date feature is added
        try {
            if (inputCommand.matches("listuser")) {
                runListCommand();
            }
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }


    public void runListCommand() throws EmptyListException {
        if (this.listOfUsers.isEmpty()) {
            throw new EmptyListException();
        }

        System.out.println("Here are all the users currently in the library's inventory!");
        for (int i = 0; i < numberOfUsers; i++) {
            System.out.println((i + 1) + ". " + this.listOfUsers.get(i).toString());
        }
    }


   /* public void runListAlphabeticalCommand() throws EmptyListException {
        if (this.listOfUsers.isEmpty()) {
            throw new EmptyListException();
        }

        System.out.println("Here are the list of all current books that are currently in\n" +
                "the library's inventory listed by alphabetical order!\n");
        this.sortedListOfBooks = new ArrayList<>(this.listOfBooks);
        this.sortedListOfBooks.sort(new Comparator<Book>() {
            @Override
            public int compare(Book bookOne, Book bookTwo) {
                return bookOne.getName().compareTo(bookTwo.getName());
            }
        });

        for (int i = 0; i < numberOfBooks; i++) {
            System.out.println((i + 1) + ". " + this.sortedListOfBooks.get(i).toString());
        }
    }
   *
    */

}
