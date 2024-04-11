package bookmarked.command;

import bookmarked.User;
import bookmarked.exceptions.EmptyUserListException;
import bookmarked.ui.Ui;

import java.util.ArrayList;

public class FindUserCommand extends Command {
    private ArrayList<User> listOfUsers;
    private String userName;

    public FindUserCommand(ArrayList<User> listOfUsers, String userName) {
        this.listOfUsers = listOfUsers;
        this.userName = userName;
    }

    /**
     * handles the command finduser
     */

    @Override
    public void handleCommand() {
        try {
            printUsers();
        } catch (EmptyUserListException e) {
            Ui.printEmptyUserListMessage();
        }
    }

    /**
     * prints the wanted user and their list of books
     * iterates through the list of users to find user printed
     * iterates through each users list of books to print user followed by their books borrowed
     *
     * @throws EmptyUserListException if the list of users is empty
     */

    private void printUsers() throws EmptyUserListException {
        boolean userFound = false;
        if (listOfUsers.isEmpty()) {
            throw new EmptyUserListException();
        }
        for (User user : listOfUsers) {
            if (user.getName().contains(userName)) {
                userFound = true;
                findUser(user);
                break;
            }
        }
        if (!userFound) {
            System.out.println("non valid user");
        }
    }

    private void findUser(User user) {
        System.out.println("User: " + user.getName());
        System.out.print("Borrowed Books: ");
        for (int i = 0; i < user.getUserBooks().size(); i++) {
            System.out.print(user.getUserBooks().get(i).getName());
            if (i < user.getUserBooks().size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
}


