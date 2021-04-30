package com.example.bookmanagement.BookPackage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

//Versão do @Controller que já inclui @ResponseBody. Usa-se normalmente com @RequestMapping.
@RestController
//Mapeia um request pra alguém lidar. Abaixo, define um endereço para esse controller específico.
@RequestMapping(path = "/books")
@Slf4j

// Lida com requests.
public class BookController {

    private final BookService bookService;

    //Caça as dependências (que no caso são beans) e injeta.
    @Autowired
    public BookController(BookService booksService) {
        this.bookService = booksService;
    }

    //Mapeia GET requests para um handler method específico. É internamente anotada com @RequesMapping.
    @GetMapping
    public List<Book> getBook(){
        log.info("############## Get Endpoint was called ###############");
        return bookService.getAllBooks();
    }

    @GetMapping(path = "{bookId}")
    public Optional<Book> getBookById(@PathVariable("bookId") Long bookId) {
        log.info("############## Get Endpoint was called ###############");
        return bookService.getBookById(bookId);
    }

    @PostMapping
    public String registerNewBook(@RequestBody Book book){
        log.info("############## Post Endpoint was called ###############");
        bookService.addNewBook(book);
        return ("Book " + book.getTitle() + " posted");
    }

    @DeleteMapping(path = "{bookId}")
    public String deleteBook(@PathVariable("bookId") Long bookId) {
        log.info("############## Delete Endpoint was called ###############");
        return bookService.deleteBook(bookId) + "deleted";
    }

    @PutMapping(path = "{bookId}")
    public String updateBook(
            @PathVariable("bookId") Long bookId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String code) {
        log.info("############## Put Endpoint was called ###############");
        return "Book with ID "+ bookId +" updated to "+ bookService.updateBook(bookId, title, code);
    }
}
