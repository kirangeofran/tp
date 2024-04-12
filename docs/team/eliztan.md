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
- Neatened up code for add, delete, borrow, return, extend, and extracted functions to be more OOP
- Added the list feature and its arguments (/sortby alphabetical, returndate, default)
- Added the quantity feature to: add, delete, borrow, return, and extend
  - Add: added arguments from the user to optionally give a /quantity function
  - Delete: added arguments from user to optionally give a /quantity function
  - Borrow: modified the way borrow works so to change it from being focused on
    being borrowed by the book, it focuses on being borrowed by the user instead.
  - Return: modified the code so that it returns the book of a specific user instead of the
     book in general. Added a /by argument from the user to the code.
  - Extend: modified the code so that it extends the book of a specific user instead of the
     book in general. Added a /by argument from the user.
- 
### Contributions to the UG

### Contributions to the DG
- list

### Contributions to team-based tasks
- Did manual testing for bugs and fixed them as needed
- Customised many exceptions
- Customised many Ui error messages
- regularly reviewed team PRs

### Review/mentoring contributions
[Link to PRs reviewed by me](https://github.com/AY2324S2-CS2113-W13-4/tp/pulls?q=is%3Apr+reviewed-by%3Aeliztan)