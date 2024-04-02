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
  - returning borrowed book : `return`
  - finding books : `find`
- FAQ
- Command Summary

## Quick Start

1. Ensure that you have Java 11 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

## Features
### Adding a book to inventory: `/help`
Lists out all available commands and their format.

Format: `/help`

### Adding a book to inventory: `add`
Adds a new book to the library's inventory.

Format: `add NAME_OF_BOOK`

* The `add` cannot be in capital letters.

Example of usage:

`add The Book Thief`

`add The Hunger Games`

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

* View all commands `/help`
* Add a book `add NAME_OF_BOOK`
* List books `list /sortby ARGUMENT`
* Edit books `edit /ARGUMENT_TO_EDIT ARGUMENT`
* Find books `find ARGUMENT`
* Exit programme `bye`
* Delete books `delete ARGUMENT`
