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

### Ui Component

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
![CommandComponentClassDiagram.png](images%2FCommandComponentClassDiagram.png)

#### HelpCommand
##### Overview
The "help command" is a feature that summarise to user all the available commands in
BookMarked application. It also includes all the command format that user can follow for use in the application.

##### Component-Level
"Help command" interfaces with another component:
1. UI component : To relay messages back to the user.

##### Implementation
Whenever the user input in `/help`, "help command" is called and command handled by calling method in UI
to show to user the messages.

![HelpCommandDiagram.png](images%2FHelpCommandDiagram.png)


#### AddCommand
##### Overview
Bookmarked is an application that allows new books bought to be added to the inventory. The add
book function allows new books to be added into the library's inventory
##### Component - Level
The 'add' command interfaces with:
1. Ui component : To relay messages back to the user
2. Storage component: For persistent storage operation
3. Book domain model : represents the state and behaviour of the individual entities
4. Exceptions component : to catch and print exceptions such as empty arguments

##### Class -Level
1. AddCommand class : It is processed through the AddCommand class where a string item
   is taken in representing user command and is added into list of books
##### Implementation Details
- The handleCommand function splits the user command into the add and description of book
  processAddCommand then takes in the split array array[1] which is the description of the book
  adds the new book into the bottom of the list based on the current number of books
- The storage component stores the book added and its borrowed status into a text file


![AddCommandDiagram.png](images%2FAddCommandDiagram.png)


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
3. Book domain model : Manages the book entities' attributes and behaviours related to deletion.
4. Exceptions component : Catches and reports errors during the delete operations such as out-of-bound indexes. 

##### Class -Level
1. Book Class: Represents each book and includes methods for adjusting inventory counts and availability status.
2. DeleteCommand Class: Parses user input, processes the deletion logic, and updates the state of the book entities.
3. BookStorage Class: Ensures that the library's persistent storage reflects the updated inventory post-deletion.
4. Ui Class: Communicates the outcome of the deletion to the user, enhancing transparency and clarity.

##### Implementation Details
How? The "DeleteCommand" class executes the following steps upon invocation:

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
3. Book Domain Model : Represents the state and behaviour of the individual book entities.
4. User Domain Model : Represents the library users and their borrowing status. 

##### Class-Level
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
3. Book Domain Model: Represents individual book entities and their borrow/return state.
4. User Domain Model: Represents library users and tracks their borrowed books.

##### Class-Level
1. Book Class: Represents a book with properties like name, isAvailable, and methods to mark the book as returned.
2. User Class: Represents library users and provides methods to manage returning books and updating borrowing records.
3. ReturnCommand Class: Parses the user command, checks book availability, processes the return operation, and updates book and user statuses.
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
The "list command" is a feature that manages the listing all the books in the inventory.
It is so librarians can keep track of all the books, as well as their status of whether they have
been borrowed or are currently available in the library.

##### Component-Level
The "list command" component interfaces with several others:
1. UI component : To relay messages back to the user.
3. Book Domain Model : Represents the state and behaviour of the individual book entities.

##### Class-Level
1. Book Class : This class represents the domain entity with properties such as 'name', 'isAvailable',
   'borrowDate' and 'returnDate' along with the methods to manipulate these properties.
2. ListCommand Class : It handles the different commands of the list input, such as regular list,
    list by alphabetical order, and so on.

##### Implementation Details
How? The "ListCommand" upon execution will:
- Split user input with the regex "/sortby" to determine the various arguments the user has for the list function
- Parses the argument to figure which it is, then creates a new ArrayList<Book> to copy
    the original ArrayList and sort the new ArrayList according to the necessary argument. The toString()
    function of each book in the newly sorted ArrayList is then called.
- If there are no books in the original ArrayList, an exception is thrown and the user is informed of it.

![ListCommandDiagram.png](images%2FListCommandDiagram.png)

#### ListUser Command
##### Overview
The "listuser command" is a feature that allows the entire list of users who are people who have borrowed books
along with all the books they borrowed 

##### Component-Level
The "listuser command" component interfaces with several others:
1. UI component : To relay messages back to the user. 
2. User Domain Model : Represents the state of the individual user entities.

##### Class-Level
1. User Class : This class represents the domain entity with properties such as 'name', 'borrowedBooks',
   'unborrowBook' along with the methods to manipulate these properties.

##### Implementation Details
How? The "ListUserCommand" upon execution will:
- Iterate through the list of users, by order of index
- If list of users is empty, throws EmptyListException
- For each user, iterates through the list of books and prints


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
- Passes command into FindUserCommand class to extract user name and iterate the list of users for users containing
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

#### AddCommand
##### Overview
Bookmarked is an application that allows new books bought to be added to the inventory
Discarded books can also be deleted through the command delete BOOK_NUMBER
- Add book:
- The add book function allows for new book to be added into the inventory
##### Class-Level
1. AddCommand class : It is processed through the AddCommand class
##### Implementation Details
- The handleCommand function splits the user command into the add and description of book
- processAddCommand adds the new book into the bottom of the list based on the current number of books

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


### Book Component

## Product scope
### Target user profile

Our target user is librarians. 

### Value proposition

Bookmarked is an application for librarians to easily keep up to date with available books, their status and
intended return date. Other than books, librarians can also check on borrowers' return status, whether they 
have overdue books and could potentially send reminders to them if they have due dates soon

## User Component

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
