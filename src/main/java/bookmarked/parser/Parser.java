package bookmarked.parser;

import bookmarked.Book;
import bookmarked.User;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.ui.Ui;
import bookmarked.command.ExitCommand;
import bookmarked.command.FindCommand;
import bookmarked.command.Command;
import bookmarked.command.ReturnCommand;
import bookmarked.command.AddCommand;
import bookmarked.command.DeleteCommand;
import bookmarked.command.BorrowCommand;
import bookmarked.command.HelpCommand;
import bookmarked.command.EditCommand;
import bookmarked.command.ListCommand;
import bookmarked.command.ListUserCommand;
import bookmarked.exceptions.BookMarkedException;
import bookmarked.command.ExtendCommand;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Parser {
    public static void runCommand(String newItem, Scanner in, ArrayList<Book> listOfBooks,
                                  File bookDataFile, ArrayList<User> listOfUsers, File userDataFile) {
        Command userCommand = new ListCommand(listOfBooks, newItem);

        while (!newItem.equalsIgnoreCase("bye")) {
            String[] splitItem = newItem.split(" ");
            Ui.setSmallerLineBreak();

            try {
                parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem,
                        listOfUsers, userDataFile);
            } catch (BookMarkedException | EmptyArgumentsException e) {
                Ui.printUnknownCommand();
            }
            Ui.setLineBreak();
            newItem = in.nextLine();
        }
        userCommand = new ExitCommand();
        userCommand.handleCommand();
    }


    public static void parseCommand(String newItem, Command userCommand, ArrayList<Book> listOfBooks,
                                    File bookDataFile, String[] splitItem, ArrayList<User> listOfUsers,
                                    File userDataFile)
            throws BookMarkedException, EmptyArgumentsException {
        switch(splitItem[0]) {
        case ("/help"):
            userCommand = new HelpCommand();
            break;
        case ("list"):
            userCommand = new ListCommand(listOfBooks, newItem);
            break;
        case ("add"):
            userCommand = new AddCommand(newItem, listOfBooks, splitItem, bookDataFile);
            break;
        case ("delete"):
            userCommand = new DeleteCommand(splitItem, listOfBooks, bookDataFile);
            break;
        case ("borrow"):
            userCommand = new BorrowCommand(splitItem, listOfBooks, bookDataFile, listOfUsers,
                    newItem, userDataFile);
            break;
        case ("return"):
            userCommand = new ReturnCommand(splitItem, listOfBooks, bookDataFile, listOfUsers, userDataFile);
            break;
        case ("find"):
            userCommand = new FindCommand(newItem, listOfBooks);
            break;
        case("listuser"):
            userCommand = new ListUserCommand(listOfUsers);
            break;
        case ("edit"):
            userCommand = new EditCommand(newItem, listOfBooks, bookDataFile);
            break;
        case "extend":
            // Ensure 'extend' is followed by the name of the book to extend
            userCommand = new ExtendCommand(splitItem, listOfBooks, bookDataFile);
            break;
        default:
            throw new BookMarkedException();
        }
        userCommand.handleCommand();
    }


}


