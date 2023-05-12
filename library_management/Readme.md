# Library Management System Backend

This is the backend code for the Library Management System project. The purpose of this system is to allow librarians to manage books and users, as well as allowing users to borrow and return books.

## Technologies Used

- Java 11
- Spring Boot
- Gradle
- PostgreSQL

## Getting Started

To get started with the project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your favorite IDE.
4. Update the `application.yaml` file with your own PostgreSQL database settings.
5. Run the application.

## Endpoints

The following endpoints are available in the backend:

### Books

- `GET /library/books` - get all books
- `GET /library/books/{id}` - get a book by id
- `GET /library/books/name?name={name}` - get a book by name
- `POST /library/books` - add a new book
- `PUT /library/books/{id}` - update a book by id
- `DELETE /library/books/{id}` - delete a book by id

### Borrowed Books

- `GET /library/borrowed` - get all borrowed books
- `GET /library/borrowed/{id}` - get a borrowed book by id
- `POST /library/borrowed` - add a new borrowed book
- `PUT /library/borrowed/{id}` - update a borrowed book by id

## License

This project is licensed under the MIT License