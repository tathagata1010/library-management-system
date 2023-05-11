package com.dev.library_management.controller;

import com.dev.library_management.entity.BorrowedBooks;
import com.dev.library_management.entity.Borrower;
import com.dev.library_management.exception.BorrowedNotFoundException;
import com.dev.library_management.model.BookBorrowRequest;
import com.dev.library_management.model.BookBorrowResponse;
import com.dev.library_management.model.UpdateBookBorrowRequest;
import com.dev.library_management.service.BorrowedBooksService;
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
@RequestMapping("/library/borrowed")
public class BorrowedBooksController {

    private final BorrowedBooksService borrowedBooksService;

    public BorrowedBooksController(BorrowedBooksService borrowedBooksService) {
        this.borrowedBooksService = borrowedBooksService;
    }

    @GetMapping("")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Get all books reports")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the list of book reports",
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
        List<BookBorrowResponse> bookReports = borrowedBooksService.getAllBookReports();
        return ResponseEntity.ok().body(bookReports);
    }


    @GetMapping("/{id}")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Get book report by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the book report object with the specified ID",
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
    public ResponseEntity<BookBorrowResponse> getBookReportById(@PathVariable Long id) throws BorrowedNotFoundException {
        BookBorrowResponse bookReport = borrowedBooksService.getBookReportById(id);
        return ResponseEntity.ok().body(bookReport);
    }

    @PostMapping
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Add new book report")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Return the created book report object",
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
    public ResponseEntity<BookBorrowResponse> addBookReport(@RequestBody BookBorrowRequest bookBorrowRequest) {
        BookBorrowResponse newBookReport = borrowedBooksService.addBookReport(bookBorrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookReport);
    }

    @PutMapping("/{id}")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "update book details by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the updated book object",
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
    public ResponseEntity<BookBorrowResponse> updateBookReport(@PathVariable Long id,@RequestBody UpdateBookBorrowRequest updateBookBorrowRequest) throws BorrowedNotFoundException {
        System.out.println(updateBookBorrowRequest.toString());
        BookBorrowResponse updatedBookReport = borrowedBooksService.updateBookReport(id, updateBookBorrowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedBookReport);
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Borrowed Controller")
    @Operation(summary = "Delete book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteBookReport(@PathVariable Long id) throws BorrowedNotFoundException {
        borrowedBooksService.deleteBookReport(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("books/{bookId}/borrowers")
//    public List<Borrower> getBorrowersForBook(@PathVariable Long bookId) {
//        return borrowedBooksService.getBorrowersByBookId(bookId);
//    }

}
