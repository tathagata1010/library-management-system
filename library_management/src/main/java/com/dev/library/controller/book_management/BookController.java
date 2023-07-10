package com.dev.library.controller.book_management;

import com.dev.library.entity.Book;
import com.dev.library.exception.BookCannotBeDeletedException;
import com.dev.library.exception.BookNotFoundException;
import com.dev.library.service.book_management.implementation.BookServiceImpl;
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

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookServiceImpl bookServiceImpl;

    public BookController(BookServiceImpl bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }


    @GetMapping("")
    @Tag(name = "Books Controller")
    @Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the list of books",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Book.class),
                                    examples = @ExampleObject(
                                            name = "Book List",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"name\": \"book1\",\n" +
                                                    "    \"author\": \"auth1\",\n" +
                                                    "    \"category\": \"cat1\",\n" +
                                                    "    \"isbn\": \"10001\"\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 2,\n" +
                                                    "    \"name\": \"book2\",\n" +
                                                    "    \"author\": \"auth2\",\n" +
                                                    "    \"category\": \"cat2\",\n" +
                                                    "    \"isbn\": \"10002\"\n" +
                                                    "  }\n" +
                                                    "]"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "No books found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Book>> getAllBooks() throws Exception {
        List<Book> books = bookServiceImpl.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("/name")
    @Tag(name = "Books Controller")
    @Operation(summary = "Get book by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the book object with the specified Name",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Book.class),
                                    examples = @ExampleObject(
                                            name = "Book",
                                            value = "{" +
                                                    "        \"id\": 3," +
                                                    "        \"name\": \"book 3\"," +
                                                    "        \"author\": \"auth\"," +
                                                    "        \"category\": \"cat\"," +
                                                    "        \"isbn\": \"10003\"" +
                                                    "    }"
                                    )
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Book> getBookByName(@RequestParam(name = "name", defaultValue = "Modern Physics") String name) {
        Book book = bookServiceImpl.getBookByName(name);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }


    @PostMapping("")
    @Tag(name = "Books Controller")
    @Operation(summary = "Add new book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Return the created book object",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class),
                            examples = @ExampleObject(
                                    name = "Book",
                                    value = "{" +
                                            "        \"id\": 3," +
                                            "        \"name\": \"book 3\"," +
                                            "        \"author\": \"auth\"," +
                                            "        \"category\": \"cat\"," +
                                            "        \"isbn\": \"10003\"" +
                                            "    }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content
            )
    })

    public ResponseEntity<Book> createBook(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Book object that needs to be added",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class),
                    examples = @ExampleObject(
                            name = "Book",
                            value = "{" +
                                    "        \"name\": \"book 3\"," +
                                    "        \"author\": \"auth\"," +
                                    "        \"category\": \"cat\"," +
                                    "        \"isbn\": \"10003\"" +
                                    "    }"
                    )
            )
    ) @RequestBody Book book) throws Exception {
        Book bookResponse = bookServiceImpl.addBook(book);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @Tag(name = "Books Controller")
    @Operation(summary = "update book details by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated book object",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class),
                            examples = @ExampleObject(
                                    name = "Book",
                                    value = "{" +
                                            "        \"id\": 3," +
                                            "        \"name\": \"book 3\"," +
                                            "        \"author\": \"auth\"," +
                                            "        \"category\": \"cat\"," +
                                            "        \"isbn\": \"10003\"" +
                                            "    }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Book> updateBook(@PathVariable BigInteger id, @RequestBody Book book) {

        Book bookResponse = bookServiceImpl.updateBook(id, book);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);

    }


    @DeleteMapping("/{id}")
    @Tag(name = "Books Controller")
    @Operation(summary = "Delete book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteBook(@PathVariable BigInteger id) throws BookNotFoundException, BookCannotBeDeletedException {
        bookServiceImpl.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

