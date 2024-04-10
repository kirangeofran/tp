package bookmarked.command;

import bookmarked.User;
import bookmarked.exceptions.EmptyUserListException;
import bookmarked.ui.Ui;

import java.util.ArrayList;

public class ListUserCommand extends Command {
    private ArrayList<User> listOfUsers;

    public ListUserCommand(ArrayList<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    /**
     * handles the command listuser
     */

    @Override
    public void handleCommand() {
        try {
            printUsersAndBooks();
        } catch (EmptyUserListException e) {
            Ui.printEmptyUserListMessage();
        }
    }

    /**
     * prints users and their list of books
     * iterates through the list of users to find and user printed
     * iterates through each users list of books to print user followed by their books borrowed
     * @throws EmptyUserListException if the list of users is empty
     */

    private void printUsersAndBooks() throws EmptyUserListException {
        if (listOfUsers.isEmpty()) {
            throw new EmptyUserListException();
        }

        System.out.println("List of Users and Borrowed Books:");
        for (User user : listOfUsers) {
            System.out.println("User: " + user.getName());
            System.out.print("Borrowed Books: ");
            if (user.getUserBooks().isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < user.getUserBooks().size(); i++) {
                    System.out.print(user.getUserBooks().get(i).getName());
                    if (i < user.getUserBooks().size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
