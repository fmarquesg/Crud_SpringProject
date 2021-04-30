package com.example.bookmanagement.BookPackage;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookService underTest;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void Setup(){
      underTest = new BookService(bookRepository);
    }

    @Test
    void getAllBooks() {
      //when
      underTest.getAllBooks();

      //then
      verify(bookRepository).findAll();
    }

    @Test
    void canAddNewBooks() {
      //given
      Book book = new Book(
              "TestBook",
              1,
              "77",
              LocalDate.of(2000, Month.JANUARY, 5));

      //when
      underTest.addNewBook(book);

      //then
      ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
      verify(bookRepository).save(bookArgumentCaptor.capture());
      Book capturedBook = bookArgumentCaptor.getValue();
      assertThat(capturedBook).isEqualTo(book);
    }

      @Test
      void throwsExceptionWhenCodeIsTaken() {
        //given
        Book book = new Book(
                "TestBook",
                1,
                "77",
                LocalDate.of(2000, Month.JANUARY, 5));

        //verificar
        BDDMockito.given(bookRepository.findByCode(book.getCode())).willReturn(Optional.of(book));
        //when
        //then
        assertThatThrownBy(() ->underTest.addNewBook(book))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Code " + book.getCode() + " taken");
      }

    @Test
    void testIfBooksAreDeleted() {

        // given
        Book book = new Book(
                "TestBook",
                1,
                "77",
                LocalDate.of(2000, Month.JANUARY, 5));

        BDDMockito.given(bookRepository.existsById(book.getId())).willReturn(true);

        // when
        underTest.deleteBook(book.getId());

        // then
        verify(bookRepository).deleteById(book.getId());
      }

  @Test
  void verifyIFStudentIsUpdated() {
    // given
    Book book = new Book(
            "TestBook",
            1,
            "77",
            LocalDate.of(2000, Month.JANUARY, 5));

    bookRepository.save(book);

    String title = "Pyotr";
    String code = "12";

    BDDMockito.given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));

    // when
    underTest.updateBook(book.getId(), title, code);

    // then
    assertThat(book.getTitle()).isEqualTo(title);
    assertThat(book.getCode()).isEqualTo(code);
  }

    @Test
    void getBookById() {
        Optional<Book> book = Optional.of(new Book(
                "TestBook",
                1,
                "77",
                LocalDate.of(2000, Month.JANUARY, 5)));

        Mockito.when(bookRepository.findById(book.get().getId())).thenReturn(book);
        Mockito.when(bookRepository.existsById(book.get().getId())).thenReturn(true);

        //When
        underTest.getBookById(book.get().getId());

        //Then
        verify(bookRepository).findById(book.get().getId());
    }
}