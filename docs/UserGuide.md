# BookMarked User Guide 📖

## Introduction

BookMarked is a desktop application designed specifically for librarians to streamline
the management of library inventory and borrower records. Optimized for use via a
Command Line Interface (CLI) while incorporating a Graphical User Interface (GUI),
BookMarked offers efficiency and speed in managing tasks. If you can type fast,
BookMarked enables you to perform inventory management tasks more swiftly than
traditional GUI-based applications.

## Table of Contents
- Quick Start
- Features
  - Viewing help : `/help`
  - Adding a book : `add`
  - Deleting a book : `delete`
  - Viewing books : `list`
  - Editing a book : `edit`
  - borrowing book : `borrow`
  - extending borrowed book : `extend`
  - returning borrowed book : `return`
  - finding books : `find`
  - exiting application : `bye`
- FAQ
- Command Summary

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 11 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

## Features
### Viewing all possible features: `/help`
Lists out all available commands and their format.

Format: `/help`

### Adding a book to inventory: `add`
Adds a new book to the library's inventory.

Format: `add NAME_OF_BOOK`

* The `add` cannot be in capital letters.

Example of usage:

`add The Book Thief`

`add The Hunger Games`

### Deleting books in the library: `delete`
Removes a book from the library's inventory based on its index in the list.

Format: `delete INDEX`

* 'Index' is the position number of the book in the list as shown by the `list` command.

Example of usage:

`delete 1`

### Viewing books in the library: `list`
Provides a list of books in the library.

Format: `list /sortby ARGUMENT`



There are three ways of sorting the list:
1. **Default**

   Format: `list`


2. **Alphabetical order**

   Format: `list /sortby alphabetical`


3. **By return date**

   Format: `list /sortby returndate`
* Sorting list by return date will only list books
  that are currently being borrowed.


### Editing books in the library: `edit`
Allows for the modification of the details of an existing book in the library. 
This command currently supports editing the title of a book.

Format: `edit BOOK_INDEX /title NEW_TITLE`

Example of usage:

`edit 1 /title The Story Thief`

### Borrowing books in the library: `borrow`
Allows a user to borrow a book from the library's inventory if it is available. 
The book will be marked as borrowed, and a due date will be set for its return.

Format: `borrow BOOK_NAME`

* Books are borrowed for a default period of two weeks from the date of borrowing.


Example of usage:

`borrow The Book Thief`

### Extending borrowed books in the library: `extend`
Allows a user to extend the borrowing period of a book they have already borrowed. 
The extension is for a predefined period of 7 days.

Format: `extend BOOK_NAME`

* You can only extend the due dates of books that are already borrowed. 

Example of usage:

`extend The Book Thief`

### Returning borrowed books in the library: `return`
Allows a user to return a book they have borrowed from the library's inventory, marking it as not borrowed.

Format: `return BOOK_NAME`

* The command updates the book's status to available, making it ready for borrowing again.

Example of usage:

`return The Book Thief`

### Finding books in the library: `find`
Enables users to search for books in the library's inventory that match a given keyword.

Format: `find KEYWORD`

* 'KEYWORD' is the word or phrase you want to search for in the titles of the books in the library.
* The command lists all books that contain the keyword in their title.

Example of usage:

`find Thief`

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

{Give a 'cheat sheet' of commands here}

* View all commands `/help`
* Add a book `add NAME_OF_BOOK`
* List books `list /sortby ARGUMENT`
