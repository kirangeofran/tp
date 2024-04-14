package bookmarked.command;
import bookmarked.user.User;

import bookmarked.exceptions.EmptyUserListException;
import bookmarked.ui.Ui;

import java.util.ArrayList;

public class ListUserCommand extends Command {
    private static ArrayList<User> listOfUsers;

    public ListUserCommand(ArrayList<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    /**
     * handles the command list /sortby user
     * splits the command to extract username and iterates the list of
     * users to find any user whose name contain the keyword name
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
     *
     * @throws EmptyUserListException if the list of users is empty
     */

    public void printUsersAndBooks() throws EmptyUserListException {
        if (listOfUsers.isEmpty()) {
            throw new EmptyUserListException();
        }
        System.out.println("List of Users and Borrowed Books:");
        System.out.println();
        printUserAndBooks();
    }

    /**
     * prints users and their list of books
     * Iterates through the list of users to find all users. Within each user, iterates
     * through the list of books of each user and prints it
     */

    private static void printUserAndBooks() {
        int userCount = 0;
        for (User user : listOfUsers) {
            System.out.println("User: " + user.getName());
            System.out.print("Borrowed Books: ");
            if (user.getUserBooks().isEmpty()) {
                System.out.println("None");
            } else {
                System.out.println();
                PrintUserBooksCommand.printUserBooks(user, userCount, listOfUsers);
            }
            userCount++;
        }
    }
}


