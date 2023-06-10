//SPDX-License-Identifier:UNLICENSED

pragma solidity ^0.8.18;

contract LibraryContract {
    address private owner;

    modifier onlyOwner() {
        require(
            msg.sender == owner,
            "Only the contract owner can call this function"
        );
        _;
    }

    constructor() {
        owner = msg.sender;
    }
    event BookAdded(uint256 indexed id, string name, string author, string category, string isbn, bool isDeleted);
    event BookUpdated(uint256 indexed id, string name, string author, string category, string isbn, bool isDeleted);
    event BookBorrowed(uint256 indexed borrowedBookId, string bookName, string borrowerName, string borrowerPhoneNumber, uint256 issueDate);
    event BookReturned(uint256 indexed borrowedBookId, string bookName, string borrowerName, string borrowerPhoneNumber, uint256 issueDate, uint256 returnDate);
    event BookLost(uint256 indexed borrowedBookId, string bookName, string borrowerName, string borrowerPhoneNumber, uint256 issueDate, bool lost);

    struct Book {
        uint256 id;
        string name;
        string author;
        string category;
        string isbn;
        bool isDeleted;
    }

    struct BorrowedBook {
        uint256 id;
        uint256 bookId;
        uint256 borrowerId;
        uint256 issueDate;
        uint256 returnDate;
        bool isLost;
    }

    struct BorrowedBookResponse {
        uint256 id;
        string bookName;
        string borrowerName;
        string borrowerPhoneNumber;
        uint256 issueDate;
        uint256 returnDate;
        bool isLost;
    }

    struct Borrower {
        uint256 id;
        string name;
        string phoneNumber;
        string walletAddress;
        mapping(uint256 => bool) hasBorrowed;
    }

    mapping(uint256 => Book) private books;
    mapping(string => Book) private bookByName;
    mapping(uint256 => BorrowedBook) private borrowedBooks;
    mapping(uint256 => Borrower) private borrowers;
    mapping(uint256 => bool) private isDeleted; // Track borrowed status of books

    uint256 private bookIdCounter = 1;
    uint256 private borrowedBookIdCounter = 1;
    uint256 private borrowerIdCounter = 1;

    function addBook(
        string memory _name,
        string memory _author,
        string memory _category,
        string memory _isbn,
        bool _isDeleted
    ) external onlyOwner returns (uint256) {
        Book memory book;
        book.id = bookIdCounter;
        book.name = _name;
        book.author = _author;
        book.category = _category;
        book.isbn = _isbn;
        book.isDeleted = _isDeleted;
        books[bookIdCounter] = book;
        bookByName[_name] = book;
        bookIdCounter++;
        emit BookAdded(book.id, book.name, book.author, book.category, book.isbn, book.isDeleted);
        return book.id;
    }

    function getBook(uint256 _id) external view returns (Book memory) {
        require(_id > 0 && _id < bookIdCounter, "Invalid book ID");
        require(books[_id].id != 0 && !books[_id].isDeleted, "The book with the given id does not exist.");
        return books[_id];
    }

    function getBookByName(string memory _name) external view returns (Book memory) {
        require(bookByName[_name].id != 0 && !bookByName[_name].isDeleted, "The book with the given name does not exist.");
        return bookByName[_name];
    }

    function deleteBook(uint256 _id) external onlyOwner {
        require(_id > 0 && _id < bookIdCounter, "Invalid book ID");
        Book storage book = books[_id];
        string memory name = book.name;
        BorrowedBook storage borrowedBook = borrowedBooks[_id];
        uint256 borrowerId = borrowedBook.borrowerId;
        Borrower storage borrower = borrowers[borrowerId];
        require(
            !borrower.hasBorrowed[borrowedBook.bookId],
            "BOOK_CURRENTLY_BORROWED"
        );

        book.isDeleted = true;
        bookByName[name].isDeleted = true;
        isDeleted[_id] = true;
    }

    function getAllBooks() external view returns (Book[] memory) {
        Book[] memory ret = new Book[](bookIdCounter - 1);
        for (uint256 i = 1; i < bookIdCounter; i++) {
            ret[i - 1] = books[i];
        }
        return ret;
    }

    function updateBook(
        uint256 _id,
        string memory _name,
        string memory _author,
        string memory _category
    ) external onlyOwner returns (bool success) {
        require(_id > 0 && _id < bookIdCounter, "Invalid book ID");
        require(bookByName[_name].id == 0 || bookByName[_name].id == _id, "Another book with the same name already exists");
        Book storage book = books[_id];
        if (book.isDeleted) {
            return false;
        }
        book.name = _name;
        book.author = _author;
        book.category = _category;
        bookByName[book.name]=book;
        emit BookUpdated(_id, _name, _author, _category, book.isbn, book.isDeleted);
        return true;
    }

    function borrowBook(
        uint256 _bookId,
        string memory _name,
        string memory _phoneNumber,
        string memory _walletAddress,
        uint256 _issueDate
    ) external returns (uint256) {
        require(_bookId > 0 && _bookId < bookIdCounter, "Invalid book ID");

        Borrower storage borrower = findOrCreateBorrower(
            _name,
            _phoneNumber,
            _walletAddress
        );

        require(!borrower.hasBorrowed[_bookId], "BOOK_ALREADY_BORROWED");

        borrowedBooks[borrowedBookIdCounter] = BorrowedBook(
            borrowedBookIdCounter,
            _bookId,
            borrower.id,
            _issueDate,
            0,
            false
        );

        borrower.hasBorrowed[_bookId] = true;
        emit BookBorrowed(borrowedBookIdCounter, books[_bookId].name, borrower.name, borrower.phoneNumber, _issueDate);
        borrowedBookIdCounter++;
        return borrowedBookIdCounter;
    }

    function returnBook(uint256 _borrowedBookId, uint256 _returnDate) external {
        require(
            _borrowedBookId > 0 && _borrowedBookId < borrowedBookIdCounter,
            "Invalid borrowed book ID"
        );
        BorrowedBook storage borrowedBook = borrowedBooks[_borrowedBookId];
        borrowedBook.returnDate = _returnDate;
        uint256 borrowerId = borrowedBook.borrowerId;
        Borrower storage borrower = borrowers[borrowerId];
        borrower.hasBorrowed[borrowedBook.bookId] = false;
        emit BookReturned(_borrowedBookId, books[borrowedBook.bookId].name, borrower.name, borrower.phoneNumber, borrowedBook.issueDate, borrowedBook.returnDate);
    }

    function markBookLost(uint256 _borrowedBookId) external onlyOwner {
        require(
            _borrowedBookId > 0 && _borrowedBookId < borrowedBookIdCounter,
            "Invalid borrowed book ID"
        );
        BorrowedBook storage borrowedBook = borrowedBooks[_borrowedBookId];
        borrowedBook.isLost = true;

        uint256 borrowerId = borrowedBook.borrowerId;
        Borrower storage borrower = borrowers[borrowerId];
        borrower.hasBorrowed[borrowedBook.bookId] = false;
        emit BookLost(_borrowedBookId, books[borrowedBook.bookId].name, borrower.name, borrower.phoneNumber, borrowedBook.issueDate, borrowedBook.isLost);
    }


    function findOrCreateBorrower(
        string memory _name,
        string memory _phoneNumber,
        string memory _walletAddress
    ) internal returns (Borrower storage) {
        for (uint256 i = 1; i < borrowerIdCounter; i++) {
            Borrower storage borrower = borrowers[i];
            if (
                keccak256(bytes(borrower.name)) == keccak256(bytes(_name)) &&
                keccak256(bytes(borrower.phoneNumber)) ==
                keccak256(bytes(_phoneNumber))
            ) {
                return borrower;
            }
        }

        Borrower storage newBorrower = borrowers[borrowerIdCounter];
        newBorrower.id = borrowerIdCounter;
        newBorrower.name = _name;
        newBorrower.phoneNumber = _phoneNumber;
        newBorrower.walletAddress = _walletAddress;
        borrowerIdCounter++;

        return newBorrower;
    }

    function getBorrowedBooks() external view returns (BorrowedBookResponse[] memory) {
        BorrowedBookResponse[] memory ret = new BorrowedBookResponse[](borrowedBookIdCounter - 1);

        for (uint256 i = 1; i < borrowedBookIdCounter; i++) {
            BorrowedBook storage borrowedBook = borrowedBooks[i];

            if (!isDeleted[borrowedBook.bookId]) {
                Book storage book = books[borrowedBook.bookId];
                Borrower storage borrower = borrowers[borrowedBook.borrowerId];

                ret[i - 1] = BorrowedBookResponse(
                    borrowedBook.id,
                    book.name,
                    borrower.name,
                    borrower.phoneNumber,
                    borrowedBook.issueDate,
                    borrowedBook.returnDate,
                    borrowedBook.isLost
                );
            }
        }

        return ret;
    }
}
