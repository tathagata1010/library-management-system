const { expect } = require("chai");

describe("LibraryContract_updated", function () {
    let libraryContract;
    let owner;
    let borrower;

    before(async function () {
        const LibraryContract = await ethers.getContractFactory("LibraryContract_updated");
        libraryContract = await LibraryContract.deploy();
        await libraryContract.deployed();

        [owner, borrower] = await ethers.getSigners();
    });

    it("should add a book", async function () {
        const tx = await libraryContract.addBook(
            "Book 1",
            "Author 1",
            "Category 1",
            false
        );
        await tx.wait();

        const book = await libraryContract.getBook(0);
        expect(book.name).to.equal("Book 1");
        expect(book.author).to.equal("Author 1");
        expect(book.category).to.equal("Category 1");
        expect(book.isDeleted).to.equal(false);
    });

    it("should update a book", async function () {
        await libraryContract.addBook("Book 2", "Author 2", "Category 2", false);

        const tx = await libraryContract.updateBook(
            0,
            "Book 1 Updated",
            "Author 1 Updated",
            "Category 1 Updated"
        );
        await tx.wait();

        const book = await libraryContract.getBook(0);
        expect(book.name).to.equal("Book 1 Updated");
        expect(book.author).to.equal("Author 1 Updated");
        expect(book.category).to.equal("Category 1 Updated");
        expect(book.isDeleted).to.equal(false);
    });

    it("should get all books", async function () {
        await libraryContract.addBook("Book 3", "Author 5", "Category 5", false);
        const books = await libraryContract.getAllBooks();
        expect(books.length).to.equal(3); for (let i = 0; i < books.length; i++) {
            const book = await libraryContract.getBook(i);
            expect(book.name).to.equal(books[i].name);
            expect(book.author).to.equal(books[i].author);
            expect(book.category).to.equal(books[i].category);
            expect(book.isDeleted).to.equal(books[i].isDeleted);
        }
    });

    it("should get all books", async function () {
        const books = await libraryContract.getAllBooks();
        expect(books.length).to.equal(3);

        for (let i = 0; i < books.length; i++) {
            const book = await libraryContract.getBook(i);
            expect(book.name).to.equal(books[i].name);
            expect(book.author).to.equal(books[i].author);
            expect(book.category).to.equal(books[i].category);
            expect(book.isDeleted).to.equal(books[i].isDeleted);
        }
    });


    it("should get a book by name", async function () {
        const bookName = "Book 1"; // Specify the name of the book to retrieve
        const book = await libraryContract.getBookByName(bookName);
        expect(book.name).to.equal(bookName);
    });

    it("should delete a book", async function () {
        const tx = await libraryContract.deleteBook(0);
        await tx.wait();

        const book = await libraryContract.getBook(0);
        expect(book.isDeleted).to.equal(true);
    });

    it("should borrow a book", async function () {
        await libraryContract.addBook("Book 4", "Author 3", "Category 3", false);

        const borrowerPhoneNumber = "123456789";
        const issueDate = Math.floor(Date.now() / 1000);

        const tx = await libraryContract.borrowBook(
            3,
            borrowerPhoneNumber,
            borrower.address,
            issueDate
        );
        await tx.wait();

        const borrowedBooks = await libraryContract.getAllBorrowedBooks();
        expect(borrowedBooks.length).to.equal(1);
        expect(borrowedBooks[0].bookName).to.equal("Book 4");
        expect(borrowedBooks[0].borrowerAddress).to.equal(borrower.address);
        expect(borrowedBooks[0].issueDate).to.equal(issueDate);
        expect(borrowedBooks[0].isLost).to.equal(false);
        expect(borrowedBooks[0].borrowerPhoneNumber).to.equal(borrowerPhoneNumber);
    });


    it("should return a book", async function () {
        const returnDate = Math.floor(Date.now() / 1000);
        const borrowedBooks = await libraryContract.getAllBorrowedBooks();
        const borrowedBook = borrowedBooks[0];
        const tx = await libraryContract.returnBook(borrowedBook.id,returnDate);
        await tx.wait();

        const updatedBorrowedBooks = await libraryContract.getAllBorrowedBooks();
        expect(updatedBorrowedBooks[0].returnDate.toNumber()).to.equal(returnDate);
    });

    it("should mark a book as lost", async function () {

        await libraryContract.addBook("Book 5", "Author 5", "Category 5", false);

        const borrowerPhoneNumber = "987654321";

        const issueDate = Math.floor(Date.now() / 1000);

        const tx = await libraryContract.borrowBook(3, borrowerPhoneNumber, borrower.address, issueDate);

        await tx.wait();

        const borrowedBooks = await libraryContract.getAllBorrowedBooks();

        const borrowedBook = borrowedBooks[0]; // Assuming there is at least one borrowed book

        const tx2 = await libraryContract.reportLost(borrowedBook.id);

        await tx2.wait();

        const updatedBorrowedBooks = await libraryContract.getAllBorrowedBooks();

        expect(updatedBorrowedBooks[0].isLost).to.equal(true);

    });

    it("should get all borrowed books", async function () {
        const borrowedBooks = await libraryContract.getAllBorrowedBooks();
        expect(borrowedBooks.length).to.equal(2);
        const issueDate = Math.floor(Date.now() / 1000);

        const borrowedBook = borrowedBooks[0];
        console.log(borrowedBook);
        expect(borrowedBooks[0].bookName).to.equal("Book 4");
        expect(borrowedBooks[0].borrowerAddress).to.equal("0x70997970C51812dc3A010C7d01b50e0d17dc79C8");
        expect(borrowedBooks[0].issueDate).to.equal(issueDate);
        expect(borrowedBooks[0].isLost).to.equal(true);
        expect(borrowedBooks[0].borrowerPhoneNumber).to.equal("123456789");
    });
});