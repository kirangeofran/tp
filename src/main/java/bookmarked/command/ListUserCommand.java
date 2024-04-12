package bookmarked.command;
import bookmarked.user.User;

import bookmarked.Book;

import bookmarked.exceptions.EmptyUserListException;
import bookmarked.ui.Ui;

import java.awt.*;
import java.util.ArrayList;

public class ListUserCommand extends Command {
    private static ArrayList<User> listOfUsers;

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
     *
     * @throws EmptyUserListException if the list of users is empty
     */

    public void printUsersAndBooks() throws EmptyUserListException {
        if (listOfUsers.isEmpty()) {
            throw new EmptyUserListException();
        }
        System.out.println("List of Users and Borrowed Books:");
        System.out.println();
        userAndBooks();
    }

    private static void userAndBooks() {
        int userCount = 0;
        for (User user : listOfUsers) {
            System.out.println("User: " + user.getName());
            System.out.print("Borrowed Books: ");
            if (user.getUserBooks().isEmpty()) {
                System.out.println("None");
            } else {
                System.out.println();
                printUserBooks(user, userCount);
            }
            userCount++;
        }
    }

    public static void printUserBooks(User user, int UserCount) {
        for (int i = 0; i < user.getUserBooks().size(); i++) {
            Ui.printElse(user, i);
            if (Book.isOverdue(user.getUserBooks().get(i).getReturnDate())) {
                Ui.printOverdue();
            }
            System.out.println();
        }
        if (UserCount < listOfUsers.size() - 1) {
            System.out.println();
        }
    }
}