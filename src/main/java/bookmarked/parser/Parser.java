package bookmarked.parser;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.command.AddCommand;
import bookmarked.command.BorrowCommand;
import bookmarked.command.EditCommand;
import bookmarked.command.Command;
import bookmarked.command.ExtendCommand;
import bookmarked.command.HelpCommand;
import bookmarked.command.DeleteCommand;
import bookmarked.command.ExitCommand;
import bookmarked.command.FindCommand;
import bookmarked.command.ListCommand;
import bookmarked.command.ReturnCommand;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.WrongInputFormatException;
import bookmarked.ui.Ui;
import bookmarked.exceptions.BookMarkedException;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Parser {
    public static void runCommand(String newItem, Scanner in, ArrayList<Book> listOfBooks,
                                  File bookDataFile, ArrayList<User> listOfUsers, File userDataFile) {
        Command userCommand = new ListCommand(listOfBooks, newItem, listOfUsers);

        while (!newItem.equals("bye")) {
            String[] splitItem = newItem.split(" ");
            Ui.setSmallerLineBreak();

            try {
                parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem,
                        listOfUsers, userDataFile);
            } catch (BookMarkedException | EmptyArgumentsException e) {
                Ui.printUnknownCommand();
            } catch (WrongInputFormatException e) {
                Ui.printWrongInputFormat();
            }
            Ui.setLineBreak();
            newItem = in.nextLine();
        }
        Ui.setSmallerLineBreak();
        userCommand = new ExitCommand();
        userCommand.handleCommand();
    }


    public static void parseCommand(String newItem, Command userCommand, ArrayList<Book> listOfBooks,
                                    File bookDataFile, String[] splitItem, ArrayList<User> listOfUsers,
                                    File userDataFile)
            throws BookMarkedException, EmptyArgumentsException, WrongInputFormatException {
        switch(splitItem[0]) {
        case ("help"):
            if (splitItem.length > 1) {
                throw new WrongInputFormatException();
            }
            userCommand = new HelpCommand();
            break;
        case ("list"):
            userCommand = new ListCommand(listOfBooks, newItem, listOfUsers);
            break;
        case ("add"):
            userCommand = new AddCommand(newItem, listOfBooks, bookDataFile);
            break;
        case ("delete"):
            userCommand = new DeleteCommand(newItem, listOfBooks, bookDataFile);
            break;
        case ("borrow"):
            userCommand = new BorrowCommand(listOfBooks, bookDataFile, listOfUsers,
                    newItem, userDataFile);
            break;
        case ("return"):
            userCommand = new ReturnCommand(newItem, listOfBooks, bookDataFile, listOfUsers, userDataFile);
            break;
        case ("find"):
            userCommand = new FindCommand(newItem, listOfBooks, listOfUsers);
            break;
        case ("edit"):
            userCommand = new EditCommand(newItem, listOfBooks, bookDataFile, userDataFile, listOfUsers);
            break;
        case ("extend"):
            userCommand = new ExtendCommand(newItem, listOfBooks, bookDataFile, listOfUsers, userDataFile);
            break;
        default:
            throw new BookMarkedException();
        }
        userCommand.handleCommand();
    }


}


