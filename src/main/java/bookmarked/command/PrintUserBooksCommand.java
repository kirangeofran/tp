package bookmarked.command;

import bookmarked.Book;
import bookmarked.ui.Ui;
import bookmarked.user.User;

import java.util.ArrayList;

public class PrintUserBooksCommand {
    private static ArrayList<User> listOfUsers;

    /**
     * Prints the list of books of each user
     * Prints an extra line if it is the final user in the list
     *
     * @param user      the user that i currently represents
     * @param UserCount the number of users
     */
    public static void printUserBooks(User user, int UserCount, ArrayList<User> listOfUsers) {
        for (int i = 0; i < user.getUserBooks().size(); i++) {
            Ui.printCommand(user, i);
            if (Book.isOverdue(user.getUserBooks().get(i).getReturnDate())) {
                Ui.printOverdue();
            }
        }
        if (UserCount < listOfUsers.size() - 1) {
            System.out.println();
        }
    }
}
