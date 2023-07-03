## Smart Contract for Library Management

LibraryContract_updated is a Solidity smart contract designed for managing a library. It allows users to add, update, and delete books, as well as borrow and return books. Additionally, the contract keeps track of borrowed books, their borrowers, and any reported lost books.

## Prerequisites

Before using this contract, ensure that you have the following installed:

1. Node.js
2. npm (Node Package Manager)
3. Hardhat

## Installation

1. Clone the repository: `git clone https://github.com/tathagata1010/library-management-system.git`
2. Navigate to the project directory: `cd Library-contract`
3. Install the required packages: `npm install`

## Contract Deployment

The contract can be deployed to the Ethereum blockchain using Hardhat. Modify the deployment script located in the `scripts` folder to specify your desired network, contract parameters, and account settings.

1. Modify the `hardhat.config.js` file to configure your network settings.
2. Update the constructor parameters of the `LibraryContract_updated` contract, if necessary.
3. To test the contract using Hardhat: `npx hardhat test`
4. To generate the test coverage report: `npx hardhat coverage`
5. Deploy the contract using Hardhat: `npx hardhat run scripts/deploy2.js --network `

## Contract Functions

### addBook

Allows the contract owner to add a new book to the library.

```solidity
function addBook(string calldata _name, string calldata _author, string calldata _category, bool isDeleted) external onlyOwner
```

### updateBook

Allows the contract owner to update the details of an existing book.

```solidity
function updateBook(uint256 _id, string calldata _name, string calldata _author, string calldata _category) external onlyOwner
```

### getBook

Retrieves the details of a book by its ID.

```solidity
function getBook(uint256 id) public view returns (Book memory)
```

### getBookByName

Retrieves the details of a book by its name.

```solidity
function getBookByName(string calldata _name) external view returns (Book memory)
```

### getAllBooks

Retrieves all the books stored in the library.

```solidity
function getAllBooks() public view returns (Book[] memory)
```

### deleteBook

Allows the contract owner to delete a book from the library.

```solidity
function deleteBook(uint256 id) public onlyOwner
```

### borrowBook

Allows a user to borrow a book from the library.

```solidity
function borrowBook(uint256 bookId, string calldata borrowerName, string calldata borrowerPhoneNumber, address borrowerAddress, uint256 issueDate) public
```

### returnBook

Allows a user to return a borrowed book.

```solidity
function returnBook(uint256 borrowedBookId, uint256 returnDate, address _borrowerAddress) public
```

### reportLost

Allows a user to report a borrowed book as lost.

```solidity
function reportLost(uint256 borrowedBookId, address _borrowerAddress) public
```

### getAllBorrowedBooks

Retrieves all the currently borrowed books.

```solidity
function getAllBorrowedBooks() public view returns (BorrowedBook[] memory)
```

## Events

The contract emits various events to notify clients about important actions.

- `BookAdded`: Emitted when a new book is added to the library.
- `BookUpdated`: Emitted when the details of a book are updated.
- `BookDeleted`: Emitted when a book is deleted from the library.
- `BookBorrowed`: Emitted when a user borrows a book.
- `BookReturned`: Emitted when a user returns a borrowed book.
-

`BookLost`: Emitted when a user reports a book as lost.

## License

This project is licensed under the terms of the [UNLICENSED](https://choosealicense.com/licenses/unlicense/) license.

---
