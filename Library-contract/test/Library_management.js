const { expect } = require("chai");
const { ethers } = require("hardhat");

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
    const book = await libraryContract.getBook(0); // Use ID 0 for the first book
    expect(book.name).to.equal("Book 1");
  });

  it("should update a book", async function () {
    const tx = await libraryContract.updateBook(
      0, // Use ID 0 for the first book
      "Book 1 Updated",
      "Author 1 Updated",
      "Category 1 Updated"
    );
    await tx.wait();
    const book = await libraryContract.getBook(0); // Use ID 0 for the first book
    expect(book.name).to.equal("Book 1 Updated");
    expect(book.author).to.equal("Author 1 Updated");
    expect(book.category).to.equal("Category 1 Updated");
  });

  it("should get a book by name", async function () {
    // Add a book before retrieving it by name
    await libraryContract.addBook("Book 3", "Author 3", "Category 3", false);

    // Retrieve the book by name
    const bookByName = await libraryContract.getBookByName("Book 3");

    // Validate the book details
    expect(bookByName.name).to.equal("Book 3");
    expect(bookByName.author).to.equal("Author 3");
    expect(bookByName.category).to.equal("Category 3");
    expect(bookByName.isDeleted).to.equal(false);
  });


  it("should delete a book", async function () {
    const tx = await libraryContract.deleteBook(0); // Use ID 0 for the first book
    await tx.wait();
    // After deletion, the book is marked as deleted but still exists in the contract
    // You may want to add additional checks for this scenario, such as checking the isDeleted flag.
  });

  it("should borrow a book", async function () {
    // Add a book before borrowing
    await libraryContract.addBook("Book 3", "Author 2", "Category 2", false);
    const tx = await libraryContract.borrowBook(
      1, // Use ID 1 for the second book
      "Borrower 1",
      "123456789",
      borrower.address,
      Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
    );
    await tx.wait();
    const borrowedBooks = await libraryContract.getAllBorrowedBooks();
    expect(borrowedBooks.length).to.equal(1);
    expect(borrowedBooks[0].bookName).to.equal("Book 2");
  });

  it("should return a book", async function () {
    // Return the previously borrowed book (ID 1)
    const tx = await libraryContract.returnBook(0, Math.floor(Date.now() / 1000), borrower.address);
    await tx.wait();
    const borrowedBooks = await libraryContract.getAllBorrowedBooks();
    expect(borrowedBooks.length).to.equal(1); // No books should be borrowed now
  });

  it("should mark a book as lost", async function () {
    // Borrow the book (ID 1) again to test marking it as lost
    await libraryContract.borrowBook(
      1, // Use ID 1 for the second book
      "Borrower 1",
      "123456789",
      borrower.address,
      Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
    );

    // Mark the borrowed book (ID 1) as lost
    const tx = await libraryContract.reportLost(0, borrower.address);
    await tx.wait();

    const borrowedBooks = await libraryContract.getAllBorrowedBooks();
    expect(borrowedBooks.length).to.equal(1);
    expect(borrowedBooks[0].isLost).to.equal(true);
  });

  // it("should not borrow a non-existent book", async function () {
  //   // Borrow a non-existent book with ID 2 (assuming the contract has only two books so far)
  //   await expect(
  //     libraryContract.borrowBook(
  //       2,
  //       "Borrower 2",
  //       "987654321",
  //       borrower.address,
  //       Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
  //     )
  //   ).to.be.revertedWith("Invalid book ID");
  // });

  it("should not borrow an already borrowed book", async function () {
    // Borrow the book (ID 1) again (assuming the contract has only two books so far)
    await libraryContract.borrowBook(
      1,
      "Borrower 1",
      "123456789",
      borrower.address,
      Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
    );

    // Try to borrow the same book again
    await expect(
      libraryContract.borrowBook(
        1,
        "Borrower 2",
        "987654321",
        borrower.address,
        Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
      )
    ).to.be.revertedWith("You have already borrowed this book");
  });

  // Add more test cases for edge cases and additional functionalities as needed

});