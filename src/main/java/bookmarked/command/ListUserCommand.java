package bookmarked.command;

import bookmarked.User;
import bookmarked.exceptions.EmptyListException;
import bookmarked.ui.Ui;

import java.util.ArrayList;

public class ListUserCommand extends Command {
    private ArrayList<User> listOfUsers;

    public ListUserCommand(ArrayList<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    @Override
    public void handleCommand() {
        try {
            printUsersAndBooks();
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }

    private void printUsersAndBooks() throws EmptyListException {
        if (listOfUsers.isEmpty()) {
            throw new EmptyListException();
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
