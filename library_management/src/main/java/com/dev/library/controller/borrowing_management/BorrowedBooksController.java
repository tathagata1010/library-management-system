package com.dev.library.controller.borrowing_management;

import com.dev.library.model.BorrowedBooks;
import com.dev.library.exception.BorrowedNotFoundException;
import com.dev.library.dto.borrowing_management.BookBorrowRequest;
import com.dev.library.dto.borrowing_management.BookBorrowResponse;
import com.dev.library.dto.borrowing_management.UpdateBookBorrowRequest;
import com.dev.library.service.borrowing_management.implementation.BorrowedBooksServiceImpl;
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

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @implNote Controller to handle the borrowing management functions
 */
@RestController
@RequestMapping("/books")
public class BorrowedBooksController {


    private final BorrowedBooksServiceImpl borrowedBooksServiceImpl;
    public static final String CONTROLLER = "Borrowed Book Controller";
    private final Logger logger = Logger.getLogger(this.getClass().getName());

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
                                                    "        \"borrowerName\": \"borrower-x\",\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"issueDate\": \"2023-05-14\",\n" +
                                                    "        \"bookName\": \"book1\"\n" +
                                                    "    },\n" +
                                                    "    {\n" +
                                                    "        \"returnDate\": \"2023-05-14\",\n" +
                                                    "        \"borrowerPhone\": \"123457890\",\n" +
                                                    "        \"borrowerName\": \"borrower-y\",\n" +
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
    public ResponseEntity<List<BookBorrowResponse>> getAllBookReports() throws Exception {
        List<BookBorrowResponse> bookReports = borrowedBooksServiceImpl.getAllBooksBorrowed();
        logger.log(Level.INFO,"{0} - Request to get all the borrowed book records",CONTROLLER);
        return ResponseEntity.ok().body(bookReports);
    }


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
    public ResponseEntity<BookBorrowResponse> addBookReport(@PathVariable BigInteger bookId, @RequestBody BookBorrowRequest bookBorrowRequest) {
        BookBorrowResponse newBookReport = borrowedBooksServiceImpl.addBookBorrow(bookId, bookBorrowRequest);
        logger.log(Level.INFO,"{0} - Request to add a new borrowed book details of book id: {1}",new Object[]{CONTROLLER,bookId});
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
    public ResponseEntity<BookBorrowResponse> updateBookReport(@PathVariable BigInteger bookId, @PathVariable BigInteger borrowedId, @RequestBody UpdateBookBorrowRequest updateBookBorrowRequest) throws BorrowedNotFoundException {
        BookBorrowResponse updatedBookReport = borrowedBooksServiceImpl.updateBorrowed(bookId, borrowedId, updateBookBorrowRequest);
        logger.log(Level.INFO,"{0} - Request to update the borrowed book status of borrowing Id: {1}",new Object[]{CONTROLLER,borrowedId});
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedBookReport);
    }


}
