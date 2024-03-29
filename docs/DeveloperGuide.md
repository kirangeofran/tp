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
BookMarked application is using txt file as its main storage for all the data.
All data related to books are stored in `book.txt`.

At the start of running the application, `readFileStorage` is called to get the data saved 
from the previous runs of the application.

![ReadFileStorageDiagram.png](images%2FReadFileStorageDiagram.png)

Whenever any there's any changes in the data for books during the running of the application, 
`writeBookToTxt` is called. Data stored in will be converted to formatted string and written 
to the txt file, `book.txt`.

![WriteBookToTxt.png](images%2FWriteBookToTxt.png)

### Parser Component

### Command Component

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
How ? The "BorrowCommand" upon execution will :
- Use the book's name provided by the user to locate the book within an 'ArrayList<Book>'.
- Check the 'isAvailable' attribute of the 'Book' instance to determine if it can be borrowed. 
- If available, the book's 'borrowDate' is set to the current date, and 'returnDate' is calculated based
on the pre-determined 2-week borrow period.
- If not available, the user is informed of the 'returnDate'.
- Post-interaction, 'BookStorage' updates the book's status in the storage file. 


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
How ? The "ListCommand" upon execution will:
- Split user input with the regex "list" to determine the various arguments the user has for the list function
- If without argument, the toString() function of each book in the ArrayList is called, printing out the books
- If with argument, parses the argument to figure which it is, then creates a new ArrayList<Book> to copy
    the original ArrayList and sort the new ArrayList according to the necessary argument. The toString()
    function of each book in the newly sorted ArrayList is then called.
- If there are no books in the original ArrayList, an exception is thrown and the user is informed of it.



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
