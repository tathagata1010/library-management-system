# Library Management System


The Library Management System is a web application that utilizes a smart contract as the database, with Spring Boot as the backend framework, Web3j library for interacting with the smart contract, and Next.js as the frontend framework. The system allows users to manage books, handle borrowing transactions, and maintain a catalog of available books.


## Features


- Book Management: Librarian can edit, edit, and delete books in the library catalog.

- Borrowing Management: Librarian can issue books to borrowers, track borrowed books, and manage return transactions or book lost transactions.

- Catalogue Management: Librarian can fetch a book by its name.


## Technology Stack


The Library Management System incorporates the following technologies:


- Smart Contract: Utilizes a smart contract as the database to store and manage book-related data and functions.

- Spring Boot: Provides the backend framework for handling business logic and integration with the smart contract.

- Web3j: Acts as the intermediary library for interacting with the smart contract, enabling seamless communication.

- Next.js: Serves as the frontend framework for building a modern and responsive user interface.


## Installation


To set up the Library Management System locally, follow these steps:


1. Clone the repository: `git clone `

2. Navigate to the backend directory: `cd library_management`

3. Configure the connection details in the `application.yaml` file to connect to your Ethereum network.

4. Build and run the backend: `./gradlew bootRun`

5. Navigate to the frontend directory: `cd ../library-management_system_ui`

6. Install dependencies: `npm install`

7. Configure the backend API endpoint in the `.env` file.

8. Start the frontend development server: `npm run dev` or `yarn dev`

9. Access the application in your web browser: `http://localhost:300*`


## Usage


1. Access the application in your web browser using the provided URL.

2. Explore the available books, search for specific books by their name, and view detailed information.

3. Manage the library catalog by adding, editing, or deleting books.

4. Issue books to borrowers by entering the necessary details, such as borrower information and book issue date.

5. Track borrowed books and manage return transactions as borrowers return the books.

6. Maintain an updated catalog of available books, ensuring accurate availability status.


## License


The Library Management System is open-source software licensed under the [MIT License](LICENSE). Feel free to use, modify, and distribute the software as permitted by the license terms.


## Acknowledgements


We would like to express our gratitude to the open-source community for their contributions and support in developing the Library Management System.