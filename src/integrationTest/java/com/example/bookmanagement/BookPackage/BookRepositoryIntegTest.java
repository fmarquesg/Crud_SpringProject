package com.example.bookmanagement.BookPackage;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest

class BookRepositoryIntegTest {

    @Autowired
    private BookRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }
    @Test
    void checksIfBookExistsByCode() {
        //given
        Book book = new Book(
                "TestBook",
                1,
                "77",
                LocalDate.of(2000, Month.JANUARY, 5));
        underTest.save(book);
        //when
        Optional<Book> expected = underTest.findByCode(book.getCode());
        //then
        Assertions.assertEquals(book.getCode(),expected.get().getCode());
    }

    @Test
    void checksIfBookDoesNotExistByCode() {
        //given
        String code = "89";
        //when
        Optional<Book> expected = underTest.findByCode(code);
        //then
        assertThat(expected.isPresent()).isFalse();
    }
}