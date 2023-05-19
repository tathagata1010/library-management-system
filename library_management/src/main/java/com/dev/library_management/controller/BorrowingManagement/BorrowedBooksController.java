package com.dev.library_management.controller.BorrowingManagement;

import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.exception.BorrowedNotFoundException;
import com.dev.library_management.model.BorrowingManagement.BookBorrowRequest;
import com.dev.library_management.model.BorrowingManagement.BookBorrowResponse;
import com.dev.library_management.model.BorrowingManagement.UpdateBookBorrowRequest;
import com.dev.library_management.service.BorrowingManagement.implementation.BorrowedBooksServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BorrowedBooksController {

    private final BorrowedBooksServiceImpl borrowedBooksServiceImpl;

    public BorrowedBooksController(BorrowedBooksServiceImpl borrowedBooksServiceImpl) {
        this.borrowedBooksServiceImpl = borrowedBooksServiceImpl;
    }

    @GetMapping("/borrowed")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Get all borrowed books records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the list of books borrowed",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BorrowedBooks.class),
                                    examples = @ExampleObject(
                                            name = "Book Report List",
                                            value = "[\n" +
                                                    "    {\n" +
                                                    "        \"returnDate\": \"2023-05-14\",\n" +
                                                    "        \"borrowerPhone\": \"123467890\",\n" +
                                                    "        \"borrowerName\": \"borrwer-x\",\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"issueDate\": \"2023-05-14\",\n" +
                                                    "        \"bookName\": \"book1\"\n" +
                                                    "    },\n" +
                                                    "    {\n" +
                                                    "        \"returnDate\": \"2023-05-14\",\n" +
                                                    "        \"borrowerPhone\": \"123457890\",\n" +
                                                    "        \"borrowerName\": \"borrwer-y\",\n" +
                                                    "        \"id\": 2,\n" +
                                                    "        \"issueDate\": \"2023-05-14\",\n" +
                                                    "        \"bookName\": \"book1\"\n" +
                                                    "    },\n" +
                                                    "    {\n" +
                                                    "        \"returnDate\": \"2023-05-14\",\n" +
                                                    "        \"borrowerPhone\": \"123456780\",\n" +
                                                    "        \"borrowerName\": \"borrower-z\",\n" +
                                                    "        \"id\": 3,\n" +
                                                    "        \"issueDate\": \"2023-05-14\",\n" +
                                                    "        \"bookName\": \"book2\"\n" +
                                                    "    },\n" +
                                                    "]\n"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "No books found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<BookBorrowResponse>> getAllBookReports() {
        List<BookBorrowResponse> bookReports = borrowedBooksServiceImpl.getAllBookReports();
        return ResponseEntity.ok().body(bookReports);
    }


    @GetMapping("/{bookId}/borrowed")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Get book record by borrowed book Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the book record  by the specified record ID",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BorrowedBooks.class),
                                    examples = @ExampleObject(
                                            name = "Book Report",
                                            value = "{\n" +
                                                    "    \"returnDate\": \"2023-05-14\",\n" +
                                                    "    \"borrowerPhone\": \"1234567890\",\n" +
                                                    "    \"borrowerName\": \"John Smith\",\n" +
                                                    "    \"issueDate\": \"2023-05-14\",\n" +
                                                    "    \"bookName\": \"System Design\"\n" +
                                                    "}"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<BookBorrowResponse>> getBookReportByBookId(@PathVariable Long bookId) throws BorrowedNotFoundException {
        System.out.println(bookId);
        List<BookBorrowResponse> bookReport = borrowedBooksServiceImpl.getBorrowedBooksByBookId(bookId);
        return ResponseEntity.ok().body(bookReport);
    }

//    @GetMapping("/{id}")
//    @Tag(name = "Borrowed Controller")
//    @Operation(summary = "Get book record by borrowed book Id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Return the book record  by the specified record ID",
//                    content = {
//                            @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = BorrowedBooks.class),
//                                    examples = @ExampleObject(
//                                            name = "Book Report",
//                                            value = "{\n" +
//                                                    "    \"returnDate\": \"2023-05-14\",\n" +
//                                                    "    \"borrowerPhone\": \"1234567890\",\n" +
//                                                    "    \"borrowerName\": \"John Smith\",\n" +
//                                                    "    \"issueDate\": \"2023-05-14\",\n" +
//                                                    "    \"bookName\": \"System Design\"\n" +
//                                                    "}"
//                                    )
//                            )
//                    }
//            ),
//            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
//    })
//    public ResponseEntity<BookBorrowResponse> getBookReportById(@PathVariable Long id) throws BorrowedNotFoundException {
//        BookBorrowResponse bookReport = borrowedBooksService.getBookReportById(id);
//        return ResponseEntity.ok().body(bookReport);
//    }

    @PostMapping("/{bookId}/borrowed")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Add new book borrow in to the record")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Return the created book record",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BorrowedBooks.class),
                            examples = @ExampleObject(
                                    name = "Book Report",
                                    value = "{\n" +
                                            "    \"returnDate\": \"2023-05-14\",\n" +
                                            "    \"borrowerPhone\": \"1234567890\",\n" +
                                            "    \"borrowerName\": \"borrower-x\",\n" +
                                            "    \"issueDate\": \"2023-05-14\",\n" +
                                            "    \"bookName\": \"book1\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content
            )
    })
    public ResponseEntity<BookBorrowResponse> addBookReport(@PathVariable Long bookId,@RequestBody BookBorrowRequest bookBorrowRequest) {
        BookBorrowResponse newBookReport = borrowedBooksServiceImpl.addBookReport(bookId,bookBorrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookReport);
    }

    @PutMapping("{bookId}/borrowed/{borrowedId}")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "update borrowed book record Status by book Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the updated book record object",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookBorrowResponse.class),
                            examples = @ExampleObject(
                                    name = "Book Report",
                                    value = "{\n" +
                                            "    \"returnDate\": \"2023-05-14\",\n" +
                                            "    \"borrowerPhone\": \"1234567890\",\n" +
                                            "    \"borrowerName\": \"borrower-y\",\n" +
                                            "    \"issueDate\": \"2023-05-26\",\n" +
                                            "    \"bookName\": \"book2\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<BookBorrowResponse> updateBookReport(@PathVariable Long bookId,@PathVariable Long borrowedId,@RequestBody UpdateBookBorrowRequest updateBookBorrowRequest) throws BorrowedNotFoundException {
        BookBorrowResponse updatedBookReport = borrowedBooksServiceImpl.updateBookReport(bookId,borrowedId, updateBookBorrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedBookReport);
    }

    @DeleteMapping("borrowed/{id}")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Delete book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteBookReport(@PathVariable Long id) throws BorrowedNotFoundException {
        borrowedBooksServiceImpl.deleteBookReport(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("books/{bookId}/borrowers")
//    public List<Borrower> getBorrowersForBook(@PathVariable Long bookId) {
//        return borrowedBooksService.getBorrowersByBookId(bookId);
//    }

}
