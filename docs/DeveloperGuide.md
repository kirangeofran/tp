# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation
## Design
### Architecture
![ArchitectureDiagram.png](images%2FArchitectureDiagram.png)

The architecture given above explains the high-level design of BookMarked application.

Given below is quick overview of main components and how they interact with each other.

#### Main components of the architecture
- `UI`: The UI of the app shown in CLI
- `Main`: The main code that handles the running of program 
- `Storage`: Handles write and read data to and from txt file
- `Parser`: Handles user input and execute the necessary command
- `Command`: Handles functionality of the app
- `Book`: Books in the library
- `User`: Users who currently borrow books in the library
- `UserBook`: Books borrowed by user which specified the borrow and 
              return due date

### UI Component
##### Overview
* BookMarked application makes use of the UI component to print out messages to the user.
* Messages include introduction messages when the user first loads the application, exit
messages when the user exits the programme as well as minor messages such as lines (-------)
to make the output neater

##### Implementation details
When UI is called, it finds the desired function and runs it, printing out the wanted message

![UiComponentClassDiagram.png](images%2FUiComponentClassDiagram.png)
### Storage Component
##### Overview
BookMarked application is using txt file as its main storage for all the data.
All data related to books in the library are stored in `book.txt`, while data 
related to user and book borrowing are stored in `user.txt`.

##### Implementation Details
At the start of running the application, `readFileStorage` in `bookStorage` are called to get the 
data saved from the previous runs of the application.

![ReadFileStorageDiagram.png](images%2FReadFileStorageDiagram.png)

After reading from bookStorage has completed, the application will then run readFileStorage
for userStorage to get the data saved related to user from the previous runs of the application.

![ReadFileStorageUserDiagram.png](images%2FReadFileStorageUserDiagram.png)

Once both data from `book.txt` and `user.txt` has been fetch into the application, validation is done
to ensure both the data fetch, such as the total books borrowed by user and in library inventory are 
in sync.

![ValidateUserBooksListsDiagram.png](images%2FValidateUserBooksListsDiagram.png)

Whenever any there's any changes in the data for books during the running of the application, 
`writeBookToTxt` is called. Data stored in will be converted to formatted string and written 
to the txt file, `book.txt`.

![WriteBookToTxtDiagram.png](images%2FWriteBookToTxtDiagram.png)
![WriteUserToTxtDiagram.png](images%2FWriteUserToTxtDiagram.png)

### Parser Component


### Command Component
All the command user can input to the application are inheriting from the `Command` class.
Each of these classes are explained in details below.

![CommandComponentClassDiagram.png](images%2FCommandComponentClassDiagram.png)

#### HelpCommand
##### Overview
The "help command" is a feature that summarise to user all the available commands in
BookMarked application. It also includes all the command format that user can follow for use in the application.

##### Component-Level
"Help command" interfaces with another component:
1. UI component: To relay messages on command instructions back to the user.
2. Command component: Represents the list of different commands to be called by the user.

##### Class-Level
The help command interfaces with various classes:
1. Help command class: Call UI class to print the standard message of list of commands
that can be called

##### Implementation
How? Upon the user calling `help`, "help command" is called 

- Help command handled by calling method in UI to show to user the messages
- Prints a list of 15 commands that can be called by the user
- Instructions on what parameters to be typed are clearly stated
- Examples on how to use the commands are shown

![HelpCommandDiagram.png](images%2FHelpCommandDiagram.png)


#### AddCommand
##### Overview
Bookmarked is an application that allows new books bought to be added to the inventory. The add
book function allows new books that have just been bought to be added into the library's inventory.
If more than one book with the same name is added, the book count of that particular book is updated

##### Component - Level
The 'add' command interfaces with:
1. Ui component : To relay messages back to the user
2. Storage component: For persistent storage operation
3. Book domain model : represents the state and behaviour of the individual entities
4. Exceptions component : to catch and print exceptions such as empty arguments

##### Class -Level
The 'add' command interfaces with several classes:
1. Book Class: Represents a book with attributes like name, isAvailable, borrowDate, and returnDate, along with methods to manipulate these properties.
2. AddCommand class: Handles the command entered, whether or not the command states quantity of each book, the list of books in the library,
and handles the updating of list of books in storage 
3. BookStorage class: Manages the status in the storage file, of list of books when new books are added and status of each book, such as whether it is borrowed, returned, borrow date, return date

##### Implementation Details
How? Upon execution, the AddCommand class will:

- The handleCommand function splits the user command into the add and description of book
  processAddCommand then takes in the description of the book
- Checks for validity of Book Name
- Check for empty arguments where the name of book is not keyed in
- Check whether there is a quantity keyed in
- Checks for empty quantity argument where quantity is called in the command but not provided
- If there is a quantity, handle the command by extracting the quantity 
- Add that number of books into the inventory
- By default, one book is added if quantity is not called
- Adds the new book into the bottom of the list based on the current number of books
- If a book with a name which matches that of a book that is already is added, update the quantity
- The storage component stores and updates the book added and its borrowed status into a text file


![AddCommand.png](images%2FAddCommand.png)


#### Delete Command
##### Overview
The "delete command" oversees the removal of books from the library's inventory. 
It enables books to be deleted based on either their index or title. 
The command verifies the specified quantity against the inventory and removes the book(s) if conditions are met. 
It also communicates with users about the outcome of the deletion operation.

##### Component - Level
The 'delete' command interfaces with multiple components within the system:
1. Ui component : For providing user feedback and error messages.
2. Storage component: Handles persistent storage modifications after deletion operations.
3. Book component : Manages the book entities' attributes and behaviours related to deletion.
4. Exceptions component : Catches and reports errors during the delete operations such as out-of-bound indexes. 

##### Class -Level
At the class level for the "delete command" in the BookMarked application, several classes are involved in the 
process of removing a book from the library's inventory. Here's what each class does:

1. Book Class: Represents each book and includes methods for adjusting inventory counts and availability status.
2. DeleteCommand Class: Parses user input, processes the deletion logic, and updates the state of the book entities.
3. BookStorage Class: Ensures that the library's persistent storage reflects the updated inventory post-deletion.
4. Ui Class: Communicates the outcome of the deletion to the user, enhancing transparency and clarity.

##### Implementation Details
How? The "DeleteCommand" class executes the following steps when called:

- Validates user input to ensure the command format and arguments are correct.
- Determines the book to delete based on the provided index or name.
- Validates that the book exists in the inventory and that the quantity specified does not exceed the available stock.
- Proceeds with the deletion process, adjusting the book's inventory counts, and if the total count reaches zero,
  removes the book from the inventory list.
- Updates the user with messages indicating the successful deletion or reasons for failure 
  (e.g., trying to delete more copies than are available).
- Employs BookStorage to save the revised book data, ensuring the library's inventory is accurately recorded.

With these functionalities, the "delete command" plays a pivotal role in inventory management, 
maintaining the accuracy of the library's catalog, and facilitating an organized removal process.

![DeleteCommandDiagram.png](images%2FDeleteCommandDiagram.png)

#### BorrowCommand 
##### Overview
The "borrow command" manages the borrowing process of books within the library system. 
It allows users to borrow books by specifying either the book name or its index. 
The command confirms the availability of the book, marks it as borrowed if available, 
and assigns a due date based on a default two-week borrow period. 
It also provides real-time feedback to users regarding the status of the book.

##### Component-Level 
The "borrow command" component interfaces with several others: 
1. UI component : To relay messages back to the user. 
2. Storage component : For persistent storage operations for books and users. 
3. Book component : Represents the state and behaviour of the individual book entities.
4. User component : Represents the library users and their borrowing status. 

##### Class-Level
At the class level for the "borrow command" in the BookMarked application, several classes are involved in 
the process of borrowing a book from the library's inventory. Here's what each class does:

1. Book Class: Represents a book with attributes like name, isAvailable, borrowDate, and returnDate, along with methods to manipulate these properties.
2. User Class: Represents a library user with methods to manage borrowed books and their respective due dates.
3. BorrowCommand Class: Manages the command input, validates user input, identifies the book to be borrowed, and handles the updating of book and user states.
4. BookStorage Class: Manages the persistence of book data, updating book statuses in the storage file.
5. UserStorage Class: Manages the persistence of user data, updating user borrowing details in the storage file.

##### Implementation Details 
How ? Upon execution, the "BorrowCommand" class performs the following actions:

- Validates the command input format and arguments.
- Determines the book to be borrowed by index or name using the provided input.
- Validates the availability of the book and whether the user has already borrowed it.
- If the book is available and not already borrowed by the same user, it marks the book as borrowed, 
  sets the current date as the borrowDate, and calculates the returnDate using the default two-week borrow period.
- Updates the user's borrowing details to include the newly borrowed book and its due date.
- Provides feedback to the user by printing messages about the borrowing status, book availability, and due dates.
- Utilizes BookStorage to write the updated book data and UserStorage to write 
  the updated user data to their respective files.

This updated borrow command enhances the user experience by ensuring a flexible and reliable borrowing process, 
preventing double borrowing by the same user, and maintaining the integrity of book and user data.

![BorrowCommandDiagram.png](images%2FBorrowCommandDiagram.png)

#### Return Command
##### Overview
The "return command" enables users to return borrowed books to the library system. 
It allows identification of the book to be returned by either its index in the list or its name. 
The command ensures that the book is marked as not borrowed upon successful return and updates the system accordingly.
This command simplifies the process of returning books, enhancing user experience by allowing flexible identifiers.

##### Component-Level
The "return command" interacts with the following components:

1. UI Component: To communicate messages and prompts to the user during the return process.
2. Storage Component: For persisting changes to the state of books and users.
3. Book component: Represents individual book entities and their borrow/return state.
4. User component: Represents library users and tracks their borrowed books.

##### Class-Level
At the class level for the "relete command" in the BookMarked application, several classes are involved 
in the process of returning a book from the library's inventory. Here's what each class does:

1. Book Class: Represents a book with properties like name, isAvailable, and methods to mark the book as returned.
2. User Class: Represents library users and provides methods to manage returning books and updating borrowing records.
3. ReturnCommand Class: Parses the user command, checks book availability, processes the return operation, 
   and updates book and user statuses.
4. BookStorage Class: Manages the persistence of updated book data.
5. UserStorage Class: Manages the persistence of updated user data.
6. UserBook Class: Represents the link between a user and a borrowed book, holding details like returnDueDate.

##### Implementation Details
Upon execution, the "ReturnCommand" class conducts the following operations:

- Validates the format and arguments of the user input using InputValidity.
- Identifies the book to be returned by name or index through SetBookIndexName.
- Validates the user making the return using SetUserName and confirms the book has been borrowed by this user.
- If valid, marks the book as returned using the Book class method setReturned.
- Removes the book from the user's borrowed list using User class methods.
- If the user has no more borrowed books, their record is removed from the user list.
- Updates both the book and user data files using BookStorage.writeBookToTxt and UserStorage.writeUserToTxt.
- Provides user feedback via System.out.println and Ui component methods for various possible outcomes 
  like successful return, overdue notices, or errors in finding the book or user.
- This comprehensive return command is designed to ensure a smooth operation and maintain the integrity of both book 
  and user data within the system following a book's return.

![ReturnCommandDiagram.png](images%2FReturnCommandDiagram.png)


#### List Command
##### Overview
* The "list command" is a feature that manages the listing all the books in the inventory.
* The list command can be used for 2 lists: user list and book list
* It is so librarians can keep track of all the books, as well as their status of whether they have
been borrowed or are currently available in the library.
* Librarians can also keep track of all users, their borrowed books and due dates as well as
their due status

##### Component-Level
The "list command" component interfaces with several others:
1. UI component : To relay messages back to the user.
2. User Domain Model : Represents the borrowing status of each user
3. Book Domain Model : Represents the state and behaviour of the individual book entities.
4. Exceptions component : To catch and exceptions like empty list when the list of books
is empty and empty arguments when there is no argument keyed in after list /sortby. Empty
user list is also caught when there are no users

##### Class-Level
1. Book Class : This class represents the domain entity with properties such as 'name', 'isAvailable',
'borrowDate' and 'returnDate' along with the methods to manipulate these properties.
2. User Class : Handles and stores the status of users who borrowed books and the status of
each user, such as their borrowed books, return due date, overdue status 
3. ListCommand Class : Handles the command by user and handles the different commands of the list input, such as regular list,
list by alphabetical order and list by user
4. ListUserCommand Class : It handles the command list by user storing user information

##### Implementation Details
How? The "ListCommand" upon execution will:
- Split user input with the regex "/sortby" to determine the various arguments the user has for the list function
- Parses the argument to figure which it is
- If the argument is empty, an exception EmptyArgumentsException is thrown
- In the case that the argument is `alphabetical` or `default`, the function creates a new ArrayList<Book> to copy
the original ArrayList and sort the new ArrayList according to the necessary argument. The toString() function of each book in the newly sorted ArrayList is then called.
- If there are no books in the original ArrayList, an exception is thrown and the user is informed of it.
- In the case that the argument is `user`, the class ListUserCommand is called. 
- If user list is empty, an exception EmptyUserListException is thrown
- Iterates through the list of users. For every user, iterates through the list of books borrowed by that user
- Repeats until the whole user list is completed

![ListCommandDiagram.png](images%2FListCommandDiagram.png)


#### Find Command
##### Overview
The `FindCommand` is a feature that allows user to search a book or user in the library based on the given keyword.
find command will find any books (if the book function is called) or user (if the user function is called) that contains 
the keyword and show the book in the form of list. If no books are found with the given keyword, the application will 
show a message for no result.

##### Implementation Details
How? The `FindCommand`, when called, will:
- Identify the command as find /by book or find /by user
If find /by book is called, upon execution will:
- Check if keyword argument is empty, and process exception when empty keyword is given by user.
- If with keyword argument, but no book available in the array list, `EmptyListException` is thrown and handled.
- Books are filtered based on the given keyword.
- All the filtered books are output to user in a list.
if find /by user is called, will:
- Split command accordingly to extract username.
- Check if username is empty, and process exception if empty user name
- Passes command into FindUserCommand class to extract user name 
- Iterates through the list of users to find users with matching user name
- Stores all users and their books in a new User array
- Iterate the new array to print users and their books
username

![FindCommandDiagram.png](images%2FFindCommandDiagram.png)


#### Edit Command
##### Overview
The `EditCommand` is a feature that allows book title of current existing books to be
updated or changed. This is done through the command `edit INDEX /title NEW_BOOK_TITLE` or
`edit CURRENT_BOOK_TITLE /title NEW_BOOK_TITLE`, which user can choose to specify the book to
by the index shown in default list or the book title. 

##### Component-Level
The `EditCommand` interfaces with the following components during the operation:
1. Storage component : For persistent storage operations for books and users.
2. UI component : To relay back message to user.

##### Class-Level
Editing the title of a book is handled in:
1. `EditCommand` : This class is handling operation by taking user input which has been
                   identified as an edit command by parser and modified data in the library
                   according to user needs.

#### Implementation Details
The `FindCommand` is called when `handleCommand()` is called for this class, which most of
the time is called in Parser component. Once called, EditCommand interfaced with some component,
such as Exception, Book, and User Component during the `handleBookEdit()` operation. Successful
edit will modify `book.txt` and `user.txt` by Storage, and confirmation message is printed.

![EditCommandDiagram.png](images%2FEditCommandDiagram.png)


#### Extend Command 
##### Overview
The ExtendCommand enables users to extend the borrowing period of books they currently have borrowed. 
This command is integral to providing flexibility in book management, allowing 
for due date adjustments to accommodate users' needs.

##### Component-Level Design
The ExtendCommand component interfaces with the following components of the system:

1. UI Component: Communicates with the user, displaying success messages and errors.
2. Storage Component: Updates persistent data with the new due dates for books and users.
3. Book Domain Model: Reflects the state and behavior of book entities, 
   especially concerning their borrow status and due date.
4. User Domain Model: Represents the users and maintains their current state, 
   including borrowed books and their due dates.

##### Class-Level Design 
At the class level for the "extend command" in the BookMarked application, several classes are involved 
in the process of extending a book from the library's inventory. Here's what each class does:

1. Book Class: Manages book attributes, including the extension of due dates.
2. User Class: Tracks user information and updates book borrowing details.
3. ExtendCommand Class: Processes the user command to extend the due dates of borrowed books.
4. BookStorage Class: Ensures the persistence of updated book information.
5. UserStorage Class: Ensures the persistence of updated user borrowing details.

##### Implementation Details 
Upon execution, the ExtendCommand:

- Parses the user input to obtain book identification (by name or index) and the user's name.
- Validates the existence of the book and the borrowing status of the user.
- Extends the borrowing period by updating the book's due date.
- Reflects this extension in the user's borrowed books list.
- Saves the updated book data to the storage file using BookStorage.writeBookToTxt.
- Saves the updated user data to the storage file using UserStorage.writeUserToTxt.
- Informs the user about the successful extension using UI methods.
- Handles any exceptions such as BookNotFoundException, UserNotFoundException, and others 
  by displaying appropriate messages through the UI.
- This ExtendCommand ensures that users can easily manage their borrowed materials 
  and maintain compliance with the library's borrowing policies, all while providing a clear and responsive feedback loop through the UI component.

![ExtendCommandDiagram.png](images%2FExtendCommandDiagram.png)

#### Exit Command
##### Overview
The "Exit command" is a feature that exits the program. It is run through the command 'bye'.
This allows users to safely close the program

##### Component-Level
"Exit command" interfaces with another component:
1. UI component: To relay messages after successfully exiting the programme back to the user.
2. Command component: Represents the list of different commands to be called by the user.

##### Class-Level
The Exit command interfaces with various classes:
1. Exit command class: Call UI class to print the standard message when exiting

##### Implementation
How? Upon the user calling `bye`, "Exit command" is called

- Exit command activated by identifying the command
- UI class is called to print out "bye message" when user exits the programme


![ExitClassDiagram.png](images%2FExitClassDiagram.png)
### Book Component

### User Component
##### Overview
User Component manages the user who at that time has at least 1 borrowed books. It has data on the name of the user, 
the book user borrowed, borrow date, and return due date.

##### Implementation Details
Data in User Component is stored in `user.txt` in the form:
```
USER_NAME | BOOK_INDEX1 | BOOK_TITLE1 | BORROW_DATE1 | RETURN_DATE1 | BOOK_INDEX2 | BOOK_TITLE2 | BORROW_DATE2 | RETURN_DATE2
```
If user borrowed more than 1 books, all the details are placed after the details of the other book, as shown by the
above format for 2 books borrowed.

During the start of the application, details about the user name, book index, book title, borrow date, and return due
date is fetched from `user.txt` through Storage Component. If data in `user.txt` is not complete or has invalid details,
operations are handled accordingly in Storage Component.

`User` may consist of 0 to as many `UserBook` and thus book index in the ArrayList. However, if user has no
borrowed books, it will be removed from the list of users the application track.
![UserComponentClassDiagram.png](images%2FUserComponentClassDiagram.png)

### UserBook Component

## Product scope
### Target user profile

Our target user is librarians at educational institutions. 

1. Profile 
- Work at school, college or university libraries. 
- Manage a medium to large inventory of books.
- Interact with a diverse group of borrowers including students, teachers and researchers. 
- Often assist borrowers with book searches, borrowing, and returns. 
- Handle adding and removing books from inventory. 

2. Needs
- Efficiently update and maintain a current and accurate inventory of the library's books.
- Easily manage the borrowing process, including loan periods and due dates. 
- Swiftly accommodate extension requests for borrowed books. 
- Efficiently remove outdated or damaged books from the system. 
- Edit book details for corrections or updates.
- Record and process book returns effectively.

3. Behaviours
- Prefer quick, keyboard-driven actions over mouse-based navigation
- Require clear, immediate feedback from the system for all actions taken.
- Value time-saving features that simplify repetitive tasks.

### Value proposition

Bookmarked is an application for librarians that streamlines library management.  
Bookmarked offers a command-line interface that allows librarians to efficiently add,borrow,extend,delete,edit 
and return books using quick keyboard commands. This streamlines their workflow and increases efficiency, 
especially during peak hours. 
The application's robust storage system ensures that all changes are immediately reflected
in the library's inventory and user records. This real-time update prevents discrepancies
and maintains the integrity of the library's data. 
With immediate feedback for each action and clear error messages, librarians can be confident in the 
outcomes of their inputs, ensuring a smooth and transparent user experience. 

## User Stories

| Version | As a ...       | I want to ...                                                           | So that I can ...                                                                   |
|---------|----------------|-------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| v1.0    | new librarian  | easily access usage instructions                                        | quickly learn how to use <br/>the application without confusion.                    |
| v1.0    | librarian      | add new book titles to my inventory                                     | keep the inventory current and offer <br/>the latest books to users.                |
| v2.1    | librarian      | add new users to my inventory                                           | can keep track of users who are<br/> borrowing books.                               |
| v2.1    | librarian      | record multiple copies of the same book title                           | accurately reflect book quantities and manage<br/> multiple loans of popular titles. |
| v1.0    | librarian      | track books that have been borrowed                                     | monitor which books are currently on loan.                                          |
| v2.1    | librarian      | track the borrow and return dates of books that <br/>have been borrowed | manage due dates efficiently.                                                       |
| v2.1    | librarian      | track users that have borrowed books                                    | keep track of borrow and <br/>return dates to remind users when books are overdue.  |
| v2.1    | librarian      | extend due dates of books                                               | offer flexibility to users who need more time<br/> with borrowed books.             |
| v1.0    | librarian      | track books that have been returned                                     | keep track of available books currently<br/> in the inventory.                      |
| v2.1    | librarian      | track books that have been returned past the due date                   | remind users of late fees.                                                          |
| v2.1    | librarian      | edit books in my inventory                                              | make sure the book details in <br/>the inventory are up-to-date.                    |
| v1.0    | librarian      | delete books from my inventory                                          | make sure the inventory does not<br/>reflect outdated or damaged books.             |
| v1.0    | librarian      | view a complete list of the books in the inventory                      | have a comprehensive<br/>overview of the inventory.                                 |
| v2.1    | busy librarian | view the list of books in my inventory in alphabetical order            | locate books more easily in the inventory, <br/>especially during peak hours.       |
| v2.1    | librarian      | view the list of users who are currently borrowing books                | keep track of the inventory and users,<br/> including following up on book returns       |

## Non-Functional Requirements

1. Should work on any mainstream OS as long as `Java 11` is installed.
2. Application should be able to be run in any terminal.
3. The application should be able to hold up to 1000 books in the library.
4. Application should work reliably on normal operation.
5. Application should be easy to use with the given summary command in `help`. 

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
