# Elizabeth Tan - Project Portfolio Page

## Overview
BookMarked is a desktop application designed specifically for librarians to streamline
the management of library inventory and borrower records. It runs in Command Line Interface (CLI),
and thus it is targeted for fast input. BookMarked is able to manage the library's inventory
through the supported command features available.

## Summary of Contributions
### Code contributed
[RepoSense link](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2024-02-23&tabOpen=true&tabType=authorship&tabAuthor=eliztan&tabRepo=AY2324S2-CS2113-W13-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancement implemented
- Overhauled code to neaten code up for add, delete, borrow, return, extend, and extracted functions to be more OOP
- Helped add the list feature and its arguments (/sortby alphabetical, default)
- Added the quantity feature to: add, and delete
  - Add: added arguments from the user to optionally give a /quantity function and added methods to handle the argument.
  - Delete: added arguments from user to optionally give a /quantity function and added methodso to handle the argument.
 
- Changed code to reflect the new quantity function in:
  - Borrow: modified the way borrow works so to change it from being focused on
    being borrowed by the book, it focuses on being borrowed by the user instead.
  - Return: modified the code so that it returns the book of a specific user instead of the
     book in general. Added a /by argument from the user to the code and methods to handle the extra argument.
  - Extend: modified the code so that it extends the book of a specific user instead of the
     book in general. Added a /by argument from the user and methods to handle the extra argument.
- Parsed out argument classes into a new package for more OOP. (package: arguments. Classes: Validity, SetBookIndexName,
SetUserName, TitleValidity).

### Contributions to the UG
- Wrote the section for AddCommand
- Helped to write the sections for ListCommand
- Added code output images for all commands

### Contributions to the DG
- Created diagrams and helped write the details of:
  - Book Component
  - ListCommand
  - FindCommand
- Helped to edit diagrams for Borrow, Return, and Extend as well
- Wrote the glossary

### Contributions to team-based tasks
- Did manual testing for bugs and fixed them as needed
- Customised many exceptions
- Customised many Ui error messages
- Regularly reviewed team PRs

### Review/mentoring contributions
[Link to PRs reviewed by me](https://github.com/AY2324S2-CS2113-W13-4/tp/pulls?q=is%3Apr+reviewed-by%3Aeliztan)