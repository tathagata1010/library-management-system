// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.18;


contract LibraryContract {

    address private owner;

    Book[] private books;

    BorrowedBook[] private borrowedBooks;

    mapping(address => uint256[]) private borrowerBooks;

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

        address[] borrowers;

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

        books.push(Book(id, name, author, category, isDeleted, new address[](0)));

        bookByName[name] = books[id];

        emit BookAdded(id, name, author, category, isDeleted);

    }


    function updateBook(uint256 id, string memory name, string memory author, string memory category) external onlyOwner {

        require(id < books.length, "Invalid book ID");

        Book storage book = books[id];

        book.name = name;

        book.author = author;

        book.category = category;

        bookByName[book.name] = book;

        emit BookUpdated(id, name, author, category, book.isDeleted);

    }


    // function getBook(uint256 id) public view returns (Book memory) {

    //     require(id < books.length, "Invalid book ID");

    //     return books[id];

    // }

    function getBookByName(string memory _name) external view returns (Book memory) {

        require(!bookByName[_name].isDeleted, "The book with the given name is deleted.");

        return bookByName[_name];

    }


    function getAllBooks() public view returns (Book[] memory) {

        return books;

    }


    function deleteBook(uint256 id) public onlyOwner {

        require(id < books.length, "Invalid book ID");

        books[id].isDeleted = true;

    }


    function borrowBook(uint256 bookId, string memory borrowerPhoneNumber, address borrowerAddress, uint256 issueDate) public {

        require(bookId < books.length, "Invalid book ID");

        require(
            borrowerBooks[borrowerAddress].length == 0 || borrowerBooks[borrowerAddress][0] != bookId,
            "You have already borrowed this book"
        );

        books[bookId].borrowers.push(msg.sender);

        uint256 borrowedBookId = borrowedBooks.length;

        borrowedBooks.push(BorrowedBook(borrowedBookId, bookId, books[bookId].name ,borrowerAddress, issueDate, 0, false, borrowerPhoneNumber));

        isBorrowed[bookId] = true;

        borrowerBooks[borrowerAddress].push(bookId);

        emit BookBorrowed(borrowedBookId, bookId, books[bookId].name ,msg.sender, block.timestamp, borrowerPhoneNumber);

    }


    function returnBook(uint256 borrowedBookId, uint256 returnDate) public {

        require(borrowedBookId < borrowedBooks.length, "Invalid borrowed book ID");

        BorrowedBook storage borrowedBook = borrowedBooks[borrowedBookId];

        borrowedBook.returnDate = returnDate;

        isBorrowed[borrowedBook.bookId] = false;

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