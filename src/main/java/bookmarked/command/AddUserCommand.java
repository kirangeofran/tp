package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.exceptions.EmptyArgumentsException;

import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

public class AddUserCommand extends Command {
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private String[] splitItem;
    private File bookDataFile;
    public AddUserCommand(String newItem, ArrayList<Book> listOfBooks, String[] splitItem,
                          File bookDataFile, ArrayList<User> listOfUsers){
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.splitItem = splitItem;
        this.bookDataFile = bookDataFile;
    }

    @Override
    public void handleCommand() {
        assert newItem != null : "Item should not be null";
        String[] newSplitUser = this.newItem.split("adduser");
        try {
            processAddUserCommand(newSplitUser, listOfUsers);
            assert newSplitUser.length >= 1 : "There should be an argument to the command";
            assert ! listOfUsers.isEmpty();
        } catch (EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
        }
    }

    public void processAddUserCommand(String[] newSplitUser, ArrayList<User> listOfUsers)
            throws EmptyArgumentsException {

        if (newSplitUser.length < 1 || newSplitUser[1].isBlank()) {
            throw new EmptyArgumentsException();
        }

        User userName = new User(newSplitUser[1].trim(), listOfBooks);
        this.listOfUsers.add(userName);
        System.out.println("Added User " + userName + "!");
    }
}
