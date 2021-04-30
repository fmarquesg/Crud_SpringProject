package com.example.bookmanagement.BookPackage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
//Indica que contém lógica de negócios. Usa-se apenas na camada de serviços.
@Service
public class BookService {
//Contém as regras de negócio da aplicação.

    private final BookRepository bookRepository;

    public BookService(BookRepository booksRepository) {
        this.bookRepository = booksRepository;
    }

    //Método GET que retorna uma lista com todos os objetos no banco Books.
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book addNewBook(Book book) {
        Optional<Book> bookByCode = bookRepository.findByCode(book.getCode());
        if (bookByCode.isPresent()) {
            throw new IllegalStateException("Code " + book.getCode() + " taken");
        }
        log.info("############## Adding a new book ###############");
        bookRepository.save(book);
        return book;
    }

    public Long deleteBook(Long bookId) {
        boolean exists = bookRepository.existsById(bookId);
        if(!exists) {
            throw new IllegalStateException(
                    "Book with id" + bookId + "does not exist");
        }
        log.info("############## Deleting a book ###############");
        bookRepository.deleteById(bookId);
        return bookId;
    }

    @Transactional
    public Book updateBook(Long bookId, String title, String code) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalStateException(
                "Book with id" + bookId + "does not exist")
        );
        if(title != null && title.length() > 2 && !Objects.equals(book.getTitle(), title)) {
            book.setTitle(title);
            log.info("############## Adding a new title ###############");
        }

        if(code != null && code.length() > 2 && !Objects.equals(book.getCode(), code)) {
            book.setCode(code);
            log.info("############## Adding a new book code ###############");
        }

        book.setCode(code);
        return book;
    }

    public Optional<Book> getBookById(Long bookId) {
        boolean exists = bookRepository.existsById(bookId);
        if(!exists) {
            throw new IllegalStateException("book with id " + bookId + " does not exists");
        }
        log.info("############## Retrieving a book by ID ###############");
        return bookRepository.findById(bookId);
    }

}