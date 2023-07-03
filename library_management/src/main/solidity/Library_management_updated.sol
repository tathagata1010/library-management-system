// SPDX-License-Identifier: UNLICENSED

pragma solidity ^0.8.19;


contract LibraryContract_updated {

    address private owner;

    Book[] private books;

    BorrowedBook[] private borrowedBooks;

    mapping(uint256 => mapping(address => bool)) private bookBorrowers;

    mapping(string => Book) private bookByName;





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

        string borrowerName;

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

    event BookBorrowed(uint256 indexed borrowedBookId, uint256 bookId, string borrowerName, string bookName, address indexed borrower, uint256 issueDate, string borrowerPhoneNumber);

    event BookReturned(uint256 indexed borrowedBookId, string bookName, string borrowerName, address borrowerAddress, string borrowerPhoneNumber, uint256 issueDate, uint256 returnDate);

    event BookLost(uint256 indexed borrowedBookId, string bookName, string borrowerName, address borrowerAddress, string borrowerPhoneNumber, uint256 issueDate, bool lost);


    function addBook(string calldata _name, string calldata author, string calldata category, bool isDeleted) external onlyOwner {

        require(keccak256(bytes(bookByName[_name].name)) != keccak256(bytes(_name)) && !bookByName[_name].isDeleted, "Another book with the same name already exists");
        uint256 id = books.length;
        books.push(Book(id, _name, author, category, isDeleted));
        bookByName[_name] = books[id];
        emit BookAdded(id, _name, author, category, isDeleted);

    }

    function updateBook(uint256 _id, string calldata name, string calldata author, string calldata category) external onlyOwner {

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

    function getBookByName(string calldata _name) external view returns (Book memory) {

        require(!bookByName[_name].isDeleted, "The book with the given name is deleted.");

        return bookByName[_name];

    }


    function getAllBooks() public view returns (Book[] memory) {
        return books;
    }


    function deleteBook(uint256 id) public onlyOwner {
        require(id < books.length, "Invalid book ID");
    bool canDelete = true;
        uint256 borrowerCount = 0;
        uint256 lostOrReturnedCount = 0;

        for (uint256 i = 0; i < borrowedBooks.length; i++) {
            if (borrowedBooks[i].bookId == id) {
                borrowerCount++;
                if (!borrowedBooks[i].isLost && borrowedBooks[i].returnDate == 0) {
                    canDelete = false;
                    break;
                } else {
                    lostOrReturnedCount++;
                }
            }
        }

        canDelete = canDelete && (borrowerCount == lostOrReturnedCount);

        require(canDelete, "Book cannot be deleted");

        books[id].isDeleted = true;
        bookByName[books[id].name] = books[id];

    }



    function borrowBook(uint256 bookId, string calldata borrowerName, string calldata borrowerPhoneNumber, address borrowerAddress, uint256 issueDate) public {

        require(bookId < books.length, "Invalid book ID");

        require(!bookBorrowers[bookId][borrowerAddress], "You have already borrowed this book");

        uint256 borrowedBookId = borrowedBooks.length;

        borrowedBooks.push(BorrowedBook(borrowedBookId, bookId, borrowerName, books[bookId].name, borrowerAddress, issueDate, 0, false, borrowerPhoneNumber));


        bookBorrowers[bookId][borrowerAddress] = true;

        emit BookBorrowed(borrowedBookId, bookId, borrowerName, books[bookId].name, borrowerAddress, issueDate, borrowerPhoneNumber);

    }


    function returnBook(uint256 borrowedBookId, uint256 returnDate, address _borrowerAddress) public {

        require(borrowedBookId < borrowedBooks.length, "Invalid borrowed book ID");

        BorrowedBook storage borrowedBook = borrowedBooks[borrowedBookId];

        require(_borrowerAddress == borrowedBook.borrowerAddress, "Book not borrowed from this address");

        borrowedBook.returnDate = returnDate;

        bookBorrowers[borrowedBook.bookId][borrowedBook.borrowerAddress] = false;

        emit BookReturned(borrowedBookId, borrowedBook.bookName, borrowedBook.borrowerName, borrowedBook.borrowerAddress, borrowedBook.borrowerPhoneNumber, borrowedBook.issueDate, returnDate);

    }


    function reportLost(uint256 borrowedBookId, address _borrowerAddress) public {

        require(borrowedBookId < borrowedBooks.length, "Invalid borrowed book ID");

        BorrowedBook storage borrowedBook = borrowedBooks[borrowedBookId];

        require(_borrowerAddress == borrowedBook.borrowerAddress, "Book not borrowed from this address");

        borrowedBook.isLost = true;

        bookBorrowers[borrowedBook.bookId][borrowedBook.borrowerAddress] = false;

        emit BookLost(borrowedBookId, borrowedBook.bookName, borrowedBook.borrowerName, borrowedBook.borrowerAddress, borrowedBook.borrowerPhoneNumber, borrowedBook.issueDate, true);

    }


    function getAllBorrowedBooks() public view returns (BorrowedBook[] memory) {

        return borrowedBooks;

    }

}