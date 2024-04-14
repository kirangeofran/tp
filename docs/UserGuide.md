# BookMarked User Guide 📖

## Introduction

BookMarked is a desktop application designed specifically for librarians to streamline
the management of library inventory and borrower records. Optimized for use via a
Command Line Interface (CLI) while incorporating a Graphical User Interface (GUI),
BookMarked offers efficiency and speed in managing tasks. If you can type fast,
BookMarked enables you to perform inventory management tasks more swiftly than
traditional GUI-based applications.

## Table of Contents
* Quick Start
* Features
  * Viewing help : `help`
  * Adding a book : `add`
  * Deleting a book : `delete`
  * Viewing books : `list`
  * Editing a book : `edit`
  * borrowing book : `borrow`
  * extending borrowed book : `extend`
  * returning borrowed book : `return`
  * finding books : `find`
  * exiting application : `bye`
* FAQ
* Command Summary

## Quick Start

1. Ensure that you have installed Java `11` on your computer.
2. Download the latest version of `BookMarked.jar` from [here](https://github.com/AY2324S2-CS2113-W13-4/tp/releases).
3. Copy the file to your desired folder for use as a _home folder_.
4. Open a command terminal, `cd` into the folder you put the jar file in, 
   and run the command `java -jar BookMarked.jar`. A command line display 
   similar to the one shown below will appear:
    ```
    _______________________________________________________________________________
    _______________________________________________________________________________
    
    Welcome to BookMarked, a one-stop app for all your librarian needs!
    To get started, you can type 'help' to see a list of commands!
    
    _______________________________________________________________________________
    _______________________________________________________________________________
    ```
5. Type some commands in the terminal and press Enter to execute it. 

    Here are some commands you can try:
    * `add book`
    * `list /sortby default`
    * `borrow book /by human`
    * `bye`
   
6. You can refer to the features below for the details of each command.

## Features
### Viewing all possible features: `help`
Lists out all available commands and their format.

Format: `help`

* `help` must all be lower case.
* there **must not** be any leading character or space before the command.
* command may include extra spaces after the command `help` 
  but **must** not include any other character.

Examples: 
* `help`

### Adding a book to inventory: `add`
Adds a new book to the library's inventory.

Format: `add NAME_OF_BOOK (optional)/quantity NUMBER_OF_COPIES`

* If the /quantity argument is not inputted, the default number of copies is 1
* A maximum of 1000 copies of the books can be stored in the library.
* To add more copies of a book that already exists, simply type in the name of the book 
you wish to add, and type in the command per normal.

Example of usage:

`add The Book Thief`

`add The Hunger Games /quantity 50`

### Deleting books in the library: `delete`
Removes a book from the library's inventory based on its index in the list.

Format: `delete INDEX (optional)/quantity NUMBER_OF_COPIES`

* 'Index' is the position number of the book in the list as shown by the `list` command.
* A maximum of 1000 copies can be stored, and thus deleted from the library's inventory.
* Delete does not affect the books currently being borrowed by users, only those currently
   available in the library's inventory.
* To completely remove a book from the library, please ensure that all books are returned
  to the library before deleting all copies in the inventory.

Example of usage:

`delete 1`

`delete 3 /quantity 100`

### Viewing books in the library: `list`
Provides a list of books in the library.

Format: `list /sortby ARGUMENT`



There are four ways of sorting the list:
1. **Default**

   Format: `list /sortby default`


2. **Alphabetical order**

   Format: `list /sortby alphabetical`


3. **By return date**

   Format: `list /sortby returndate`
* Sorting list by return date will only list books
  that are currently being borrowed.

4. **By user**
    Format: `list /sortby user`
* Listing all users along with their borrowed books


### Editing books in the library: `edit`
Allows for the modification of the details of an existing book in the library. 
This command currently supports editing the title of a book.

Format:

To edit by book index : `edit INDEX /title NEW_TITLE`

* The `INDEX` of the book is based on the output shown in the default list function.
* The new title of the book is specified in `NEW_TITLE`
* `edit` and `title` are case-sensitive, and **must not** be in capital letter.
* The title of the book, `NEW_TITLE`, is also case-sensitive, and different in case may
  refer to a different book.
* There **must** be at least 1 space between the word `edit` and `INDEX` in the command.
* Similarly, there **must** be at least 1 space between the `INDEX` and `/title`
* There **must** be at least 1 space between the `/title` and `NEW_TITLE`, should there be
  more than 1 spaces, the space before the first character and the space after the last
  character will be **ignored**.

To edit by book title : `edit CURRENT_BOOK_TITLE /title NEW_TITLE`


Example of usage:

`edit 1 /title The Story Thief`

### Borrowing books in the library: `borrow`
Allows a user to borrow a book from the library's inventory if it is available. 
The book will be marked as borrowed, and a due date will be set for its return.

Format: 

To borrow by book title : `borrow BOOK_NAME /by USER_NAME`

To borrow by book index : `borrow INDEX /by USER_NAME`

* The INDEX of the book can be seen using the list function. 
* Books are borrowed for a default period of two weeks from the date of borrowing.
* Users do not need to be pre-added. They can be added directly when using the borrow command. 
* If the specified book is not available for borrowing, or if there are no available copies left in the inventory,
* an appropriate message will be displayed.

Examples of usage:

Borrow by book title : `borrow The Book Thief /by Tom`

Borrow by book index : `borrow 1 /by Tom`

### Extending borrowed books in the library: `extend`
Allows a user to extend the borrowing period of a book they have already borrowed. 
The extension is for a predefined period of 7 days.

Format: 

To extend by book title : `extend BOOK_NAME /by USER_NAME`

To extend by book index : `extend INDEX /by USER_NAME`

* The due dates of books can only be extended, if they are already borrowed. 
* The INDEX of the book can be seen using the list function.
* The system will output a message confirming the successful extension of the borrowing period, 
* along with the new due date. 

Examples of usage:

Extend by book title : `extend The Book Thief /by Tom`

Extend by book index : `extend 0 /by Tom`

### Returning borrowed books in the library: `return`
Allows a user to return a book they have borrowed from the library's inventory, marking it as not borrowed.

Format: 

To return by book title : `return BOOK_NAME /by USER_NAME`

To return by book index : `return INDEX /by USER_NAME`

* The command updates the book's status to available, making it ready for borrowing again.
* Only books that have been borrowed can be returned. 
* The INDEX of the book can be seen using the list function.

Examples of usage:

Return by book title : `return The Book Thief /by Tom`

Return by book index : `return 1 /by Tom`

### Finding books or users in the library: `find`
Enables users to search for books in the library's inventory that match a given keyword.
Enables users to search for those who borrowed books

To search for books:
Format: `find /by book KEYWORD`

* 'KEYWORD' is the word or phrase you want to search for in the titles of the books in the library.
* The command lists all books that contain the keyword in their title.

Example of usage:

`find /by book Thief`

To search for users:
Format: `find /by user USERNAME`

* 'USERNAME' is the name of the user you want to find.
* The command lists all users that contain the username in their name.

Example of usage:
`find /by user Tom`

### Exiting application: `bye`
Safely closes the BookMarked application.

Format: `bye`

Example of usage: 

`bye`

## FAQ
> **Q**: How do I transfer my data to another computer?
>
> **A**: Copy the /book.txt file to another computer and paste it in the
> same folder inside bookMarked.jar

> **Q**: Can I access the task I previously added when I restart the application?
>
> **A**: Yes, all data are saved in local hard disk and will be reloaded every time
> the application is restarted

## Command Summary

| Action                                   | Command Format                                  | Example                   |
|------------------------------------------|-------------------------------------------------|---------------------------|
| view all commands and its usage          | help                                            | help                      |
| add 1 book                               | add BOOK_TITLE                                  | add abc                   |
| add specific number of books             | add BOOK_TITLE /quantity NUMBER_OF_COPIES       | add abc /quantity 5       |
| borrow a book                            | borrow BOOK_TITLE /by USER_NAME                 | borrow abc /by human      |
| return a book                            | return BOOK_TITLE /by USER_NAME                 | return abc /by human      |
| extend return due date by a week         | extend BOOK_TITLE                               | extend abc                |
| edit book title by book number in list   | edit NUMBER_ACCORDING_TO_LIST /title BOOK_TITLE | edit 1 /title def         |
| edit book title by current book title    | edit CURRENT_BOOK_TITLE /title NEW_BOOK_TITLE   | edit abc /title def       |
| delete a book                            | delete BOOK_TITLE                               | delete abc                |
| list all books                           | list /sortby default                            | list /sortby default      |
| list all books alphabetically            | list /sortby alphabetical                       | list /sortby alphabetical |
| list borrowed books based on return date | list /sortby returndate                         | list /sortby returndate   |
| list users who borrowed books            | list /sortby user                               | list /sortby user         |
| find a book                              | find /by book FIND_KEYWORD                      | find /by book abc         |
| find a user                              | find /by user FIND_KEYWORD                      | find /by user human       |
