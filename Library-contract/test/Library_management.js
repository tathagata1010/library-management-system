
const { expect } = require("chai");


describe("LibraryContract", function () {

  let libraryContract;

  let owner;

  let borrower;


  before(async function () {

    const LibraryContract = await ethers.getContractFactory("LibraryContract");

    libraryContract = await LibraryContract.deploy();

    await libraryContract.deployed();


    [owner, borrower] = await ethers.getSigners();

  });


  it("should add a book", async function () {

    const tx = await libraryContract.addBook(

      "Book 1",

      "Author 1",

      "Category 1",

      "ISBN 1",

      false

    );

    await tx.wait();


    const book = await libraryContract.getBook(1);

    expect(book.name).to.equal("Book 1");

  });


  it("should borrow a book", async function () {

    const tx = await libraryContract.borrowBook(

      1,

      "Borrower 1",

      "123456789",

      "0x0123456789abcdef",

      Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date

    );

    await tx.wait();


    const borrowedBooks = await libraryContract.getBorrowedBooks();

    expect(borrowedBooks.length).to.equal(1);

    expect(borrowedBooks[0].bookName).to.equal("Book 1");

  });


  it("should return a book", async function () {

    const tx = await libraryContract.returnBook(1, Math.floor(Date.now() / 1000)); // Use the current timestamp as the return date

    await tx.wait();


    const borrowedBooks = await libraryContract.getBorrowedBooks();

    expect(borrowedBooks.length).to.equal(1);

  });


  it("should mark a book as lost", async function () {

    const tx = await libraryContract.markBookLost(1);

    await tx.wait();


    const borrowedBooks = await libraryContract.getBorrowedBooks();

    expect(borrowedBooks.length).to.equal(1);

  });


  it("should update a book", async function () {

    const tx = await libraryContract.updateBook(

      1,

      "Book 1 Updated",

      "Author 1 Updated",

      "Category 1 Updated"

    );

    await tx.wait();


    const book = await libraryContract.getBook(1);

    expect(book.name).to.equal("Book 1 Updated");

    expect(book.author).to.equal("Author 1 Updated");

    expect(book.category).to.equal("Category 1 Updated");

  });


  it("should delete a book", async function () {

    const tx = await libraryContract.deleteBook(1);

    await tx.wait();


    const book = await libraryContract.getBook(1);

    // expect(book.isDeleted).to.equal(true);

  });
  it("should not borrow a non-existent book", async function () {
    const nonExistentBookId = 2;
    await expect(
      libraryContract.borrowBook(
        nonExistentBookId,
        "Borrower 2",
        "987654321",
        "0xfedcba9876543210",
        Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
      )
    ).to.be.revertedWith("Invalid book ID");
  });

  it("should not borrow an already borrowed book", async function () {
    await libraryContract.borrowBook(

      1,

      "Borrower 1",

      "123456789",

      "0x0123456789abcdef",

      Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date

    );

   
    const alreadyBorrowedBookId = 1;
    await expect(
      libraryContract.borrowBook(
        alreadyBorrowedBookId,
        "Borrower 2",
        "987654321",
        "0xfedcba9876543210",
        Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date
      )
    ).to.be.revertedWith("Book is already borrowed");
  });

  // it("should not return a book that is not borrowed", async function () {
  //   const notBorrowedBookId = 2;
  //   await expect(
  //     libraryContract.returnBook(notBorrowedBookId, Math.floor(Date.now() / 1000))
  //   ).to.be.revertedWith("Book is not borrowed");
  // });

  // it("should not mark a non-existent book as lost", async function () {
  //   const nonExistentBookId = 2;
  //   await expect(libraryContract.markBookLost(nonExistentBookId)).to.be.revertedWith(
  //     "Book does not exist"
  //   );
  // });

  it("should not update a non-existent book", async function () {
    const nonExistentBookId = 2;
    await expect(
      libraryContract.updateBook(
        nonExistentBookId,
        "Book 2 Updated",
        "Author 2 Updated",
        "Category 2 Updated"
      )
    ).to.be.revertedWith("Invalid book ID");
  });

  it("should not delete a non-existent book", async function () {
    const nonExistentBookId = 2;
    await expect(libraryContract.deleteBook(nonExistentBookId)).to.be.revertedWith(
      "Invalid book ID"
    );
  });

  it("should not delete a borrowed book", async function () {
    await libraryContract.borrowBook(

      1,

      "Borrower 1",

      "123456789",

      "0x0123456789abcdef",

      Math.floor(Date.now() / 1000) // Use the current timestamp as the issue date

    );

    const borrowedBookId = 1;
    await expect(libraryContract.deleteBook(borrowedBookId)).to.be.revertedWith(
      "BOOK_CURRENTLY_BORROWED"
    );
  });


});