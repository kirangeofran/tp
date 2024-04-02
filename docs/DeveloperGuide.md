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
- `Ui`: The UI of the app shown in CLI
- `Main`: The main code that handles the running of program 
- `Storage`: Handles write and read data to and from txt file
- `Parser`: Handles user input and execute the necessary command
- `Command`: Handles functionality of the app
- `Book`: Books in the library

### Ui Component

### Storage Component
##### Overview
BookMarked application is using txt file as its main storage for all the data.
All data related to books are stored in `book.txt`.

##### Implementation Details
At the start of running the application, `readFileStorage` is called to get the data saved 
from the previous runs of the application.

![ReadFileStorageDiagram.png](images%2FReadFileStorageDiagram.png)

Whenever any there's any changes in the data for books during the running of the application, 
`writeBookToTxt` is called. Data stored in will be converted to formatted string and written 
to the txt file, `book.txt`.

![WriteBookToTxtDiagram.png](images%2FWriteBookToTxtDiagram.png)


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
Bookmarked is an application that allows new books bought to be added to the inventory
Discarded books can also be deleted through the command delete BOOK_NUMBER
- Add book:
- The add book function allows for new book to be added into the inventory
##### Class -Level
1. AddCommand class : It is processed through the AddCommand class
##### Implementation Details
- The handleCommand function splits the user command into the add and description of book
- processAddCommand adds the new book into the bottom of the list based on the current number of books

![AddCommandDiagram.png](images%2FAddCommandDiagram.png)


#### Delete Command


#### BorrowCommand 
##### Overview
The "borrow command" is a feature that manages the borrowing of books. The enhancements to this 
command aim to provide real-time feedback on book availability, track borrowing and due dates, 
and notify users of due dates of books that are currently being borrowed by others.

##### Component-Level 
The "borrow command" component interfaces with several others: 
1. UI component : To relay messages back to the user. 
2. Storage component : For persistent storage operations.
3. Book Domain Model : Represents the state and behaviour of the individual book entities.

##### Class-Level
1. Book Class : This class represents the domain entity with properties such as 'name', 'isAvailable', 
'borrowDate' and 'returnDate' along with the methods to manipulate these properties. 
2. BorrowCommand Class : Orchestrates the borrowing process. It parses the user command,
interacts with the book objects to check availability, and updates book states. 
3. BookStorage Class : Handles data persistence, ensuring that book statuses are consistently stored and 
retrieved from a file or database. 

##### Implementation Details 
How? The "BorrowCommand" upon execution will :
- Use the book's name provided by the user to locate the book within an 'ArrayList<Book>'.
- Check the 'isAvailable' attribute of the 'Book' instance to determine if it can be borrowed. 
- If available, the book's 'borrowDate' is set to the current date, and 'returnDate' is calculated based
on the pre-determined 2-week borrow period.
- If not available, the user is informed of the 'returnDate'.
- Post-interaction, 'BookStorage' updates the book's status in the storage file. 


#### Return Command


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
- If the user input is simply "list", the toString() function of each book in the ArrayList is called, printing out the books
- If with argument, parses the argument to figure which it is, then creates a new ArrayList<Book> to copy
    the original ArrayList and sort the new ArrayList according to the necessary argument. The toString()
    function of each book in the newly sorted ArrayList is then called.
- If there are no books in the original ArrayList, an exception is thrown and the user is informed of it.

![ListCommandDiagram.png](images%2FListCommandDiagram.png)


#### Find Command
##### Overview
The "find command" is a feature that allows user to search a book in the library based on the given keyword.
find command will find any books that contains the keyword and show the user in the form of list. If no
books are found with the given keyword, the application will show a message for no result.

##### Implementation Details
How? The "FindCommand" upon execution will:
- Check if keyword argument is empty, and process exception when empty keyword is given by user.
- If with keyword argument, but no book available in the array list, `EmptyListException` is thrown and handled.
- Books are filtered based on the given keyword.
- All the filtered books are output to user in a list.

![FindCommandDiagram.png](images%2FFindCommandDiagram.png)


#### Edit Command


#### AddCommand
##### Overview
Bookmarked is an application that allows new books bought to be added to the inventory
Discarded books can also be deleted through the command delete BOOK_NUMBER
- Add book:
- The add book function allows for new book to be added into the inventory
##### Class -Level
1. AddCommand class : It is processed through the AddCommand class
##### Implementation Details
- The handleCommand function splits the user command into the add and description of book
- processAddCommand adds the new book into the bottom of the list based on the current number of books

#### Extend Command 
##### Overview
The ExtendCommand is a feature within the book management system that enables users to extend the borrowing period 
of books. This command augments user experience by providing the flexibility to extend due dates, 
ensuring better management of borrowed materials.

##### Component-Level Design
The ExtendCommand component interfaces with the following components of the system:

1. UI Component: To communicate feedback and errors to the user.
2. Storage Component: For updating the persistence layer with the new return dates.
3. Book Domain Model: Represents the state and behavior of book entities, 
   particularly regarding their borrowing status.

##### Class-Level Design 
1. Book Class: Represents a book with properties such as name, isBorrowed, borrowDate, and dueDate. 
   It includes methods for extending the borrowing period.
2. ExtendCommand Class: Responsible for handling the extension of the book's borrowing period by interacting 
   with book instances to update their due dates.
3. BookStorage Class: Manages data persistence by saving updated book information to storage, 
   ensuring the changes are maintained across sessions.

##### Implementation Details 
How ?Upon execution, the ExtendCommand performs the following actions:
1. Parses the input to identify the book name specified by the user.
2. Searches for the book in an ArrayList of Book instances.
3. Verifies if the book is currently borrowed by checking the isBorrowed attribute.
4. If the book is borrowed, it calls extendDueDate() on the Book instance to
   extend the return date by a predefined period (e.g., one week).
5. Notifies the user of the successful extension of the due date.
6. In case the book is not available or not found, it throws BookNotFoundException or BookNotBorrowedException, 
   respectively, and the UI component handles user notification.
7. Post-interaction, the BookStorage class is tasked with updating the storage with the new state of the book entity.


#### Exit Command


### Book Component

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

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
