// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.18;


contract LibraryContract_updated {

    address private owner;

    Book[] private books;

    BorrowedBook[] private borrowedBooks;

    mapping(uint256 => mapping(address => bool)) private bookBorrowers;

    mapping(string => Book) private bookByName;

    mapping(uint256 => bool) private isBorrowed;



    modifier onlyOwner() {

        require(msg.sender == owner, "Only the contract owner can call this function");

        _;

    }


    constructor() {

        owner = msg.sender;

    }


    struct Book {

        uint256 id;

        string name;

        string author;

        string category;

        bool isDeleted;

    }


    struct BorrowedBook {

        uint256 id;

        uint256 bookId;

        string bookName;

        address borrowerAddress;

        uint256 issueDate;

        uint256 returnDate;

        bool isLost;

        string borrowerPhoneNumber;

    }


    event BookAdded(uint256 indexed id, string name, string author, string category, bool isDeleted);

    event BookUpdated(uint256 indexed id, string name, string author, string category, bool isDeleted);

    event BookDeleted(uint256 indexed id);

    event BookBorrowed(uint256 indexed borrowedBookId, uint256 bookId, string bookName,address indexed borrower, uint256 issueDate, string borrowerPhoneNumber);

    event BookReturned(uint256 indexed borrowedBookId, string bookName, address borrowerAddress, string borrowerPhoneNumber, uint256 issueDate, uint256 returnDate);

    event BookLost(uint256 indexed borrowedBookId, string bookName, address borrowerAddress, string borrowerPhoneNumber, uint256 issueDate, bool lost);



    function addBook(string memory name, string memory author, string memory category, bool isDeleted) external onlyOwner {

        uint256 id = books.length;

        books.push(Book(id, name, author, category, isDeleted));

        bookByName[name] = books[id];

        emit BookAdded(id, name, author, category, isDeleted);

    }


    function updateBook(uint256 _id, string memory name, string memory author, string memory category) external onlyOwner {

        require(_id < books.length, "Invalid book ID");
        require(bookByName[name].id == 0 || (bookByName[name].id == _id && !bookByName[name].isDeleted), "Another book with the same name already exists");
        Book storage book = books[_id];

        book.name = name;

        book.author = author;

        book.category = category;

        bookByName[book.name] = book;

        emit BookUpdated(_id, name, author, category, book.isDeleted);

    }


     function getBook(uint256 id) public view returns (Book memory) {

         require(id < books.length, "Invalid book ID");

         return books[id];

     }

    function getBookByName(string memory _name) external view returns (Book memory) {

        require(!bookByName[_name].isDeleted, "The book with the given name is deleted.");

        return bookByName[_name];

    }


    function getAllBooks() public view returns (Book[] memory) {
        return books;
    }


    function deleteBook(uint256 id) public onlyOwner {

        require(id < books.length, "Invalid book ID");
        require(!isBorrowed[id],"Book is currently borrowed");
        books[id].isDeleted = true;
        bookByName[books[id].name]=books[id];
    }


    function borrowBook(uint256 bookId, string memory borrowerPhoneNumber, address borrowerAddress, uint256 issueDate) public {

        require(bookId < books.length, "Invalid book ID");

        require(!bookBorrowers[bookId][borrowerAddress], "You have already borrowed this book");

        uint256 borrowedBookId = borrowedBooks.length;

        borrowedBooks.push(BorrowedBook(borrowedBookId, bookId, books[bookId].name ,borrowerAddress, issueDate, 0, false, borrowerPhoneNumber));

        isBorrowed[bookId] = true;

        bookBorrowers[bookId][borrowerAddress] = true;

        emit BookBorrowed(borrowedBookId, bookId, books[bookId].name ,borrowerAddress, issueDate, borrowerPhoneNumber);

    }


    function returnBook(uint256 borrowedBookId, uint256 returnDate) public {

        require(borrowedBookId < borrowedBooks.length, "Invalid borrowed book ID");

        BorrowedBook storage borrowedBook = borrowedBooks[borrowedBookId];

        borrowedBook.returnDate = returnDate;

        isBorrowed[borrowedBook.bookId] = false;

        bookBorrowers[borrowedBook.bookId][borrowedBook.borrowerAddress] = false;

        emit BookReturned(borrowedBookId,borrowedBook.bookName,borrowedBook.borrowerAddress,borrowedBook.borrowerPhoneNumber,borrowedBook.issueDate ,returnDate);

    }


    function reportLost(uint256 borrowedBookId) public {

        require(borrowedBookId < borrowedBooks.length, "Invalid borrowed book ID");

        BorrowedBook storage borrowedBook = borrowedBooks[borrowedBookId];

        borrowedBook.isLost = true;

        emit BookLost( borrowedBookId,borrowedBook.bookName,borrowedBook.borrowerAddress,borrowedBook.borrowerPhoneNumber,borrowedBook.issueDate, true );

    }


    function getAllBorrowedBooks() public view returns (BorrowedBook[] memory) {

        return borrowedBooks;

    }

}