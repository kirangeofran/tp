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
  - Viewing help : `help` 
  - Adding a book : `add`
  - Deleting a book : `delete`
  - Viewing all books : `list`
  - Editing a book : `edit`
  - borrowing book : `borrow`
  - returning borrowed book : `return`
  - finding books : `find`
- FAQ
- Command Summary

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 11 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

## Features 

{Give detailed description of each feature}

### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.  

Example of usage: 

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`

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

* Add todo `todo n/TODO_NAME d/DEADLINE`
