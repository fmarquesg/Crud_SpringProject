package com.example.bookmanagement.BookPackage;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;



@ExtendWith(SpringExtension.class)
// Cria uma porta random pra não conflitar com o servidor.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

public class BookControllerIntegTest {

    // Define a variável da porta random feita ali em cima em um int pra usar nos testes.
    @LocalServerPort
    private int port;

    @PostConstruct
    void setUp() throws Exception {
    baseURI = "http://localhost:" + port + "/"; }

    // Moca o serviço
    @MockBean
    private BookService bookService;

    @Test
    public void getBooksWhenUsernameAndPasswordAreIncorrectShouldReturnStatus401() {
        Response response =
                given()
                        .port(port)
                        .auth()
                        .basic("authuser1", "authpass2")
                        .contentType(ContentType.JSON)
                        .when()
                        .get(baseURI + "books")
                        .then()
                        .extract()
                        .response();

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void getBooksByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatus401() {
        Book book = new Book(1L, "TestBook", 1, "77", LocalDate.of(2000, Month.JANUARY, 5));
        Mockito.when(bookService.getBookById(1L)).thenReturn(java.util.Optional.of(book));

        Response response =
                given()
                        .port(port)
                        .auth()
                        .basic("authuser1", "authpass2")
                        .contentType(ContentType.JSON)
                        .when()
                        .get(baseURI + "books" + book.getId())
                        .then()
                        .extract()
                        .response();

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    public void getBooksWhenUsernameAndPasswordAreCorrectShouldReturnStatus200() {
        Book book = new Book(1L, "TestBook", 1, "77", LocalDate.of(2000, Month.JANUARY, 5));
        List<Book> books = new ArrayList<Book>();
        books.add(new Book(1L, "TestBook", 1, "77", LocalDate.of(2000, Month.JANUARY, 5)));
        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        Response response =
                given()
                        .port(port)
                        .auth()
                        .basic("authuser", "authpass")
                        .contentType(ContentType.JSON)
                        .when()
                        .get(baseURI + "books")
                        .then()
                        .extract()
                        .response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1L, response.jsonPath().getLong("id[0]"));
    }

    @Test
    public void getBooksbyIdWhenUsernameAndPasswordAreCorrectShouldReturnStatus200() {
        Book book = new Book(1L,"TestBook", 1, "77", LocalDate.of(2000, Month.JANUARY, 5));
        Mockito.when(bookService.getBookById(1L)).thenReturn(java.util.Optional.of(book));

        Response response =
                given()
                        .auth()
                        .basic("authuser", "authpass")
                        .contentType(ContentType.JSON)
                        .when()
                        .get(baseURI + "books/" + book.getId())
                        .then()
                        .extract()
                        .response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1L, response.jsonPath().getLong("id"));
    }

    @Test
    public void verifyIfPathIsIncorrectShouldReturn404() {
        Response response =
                given()
                        .port(port)
                        .auth()
                        .basic("authuser", "authpass")
                        .contentType(ContentType.JSON)
                        .when()
                        .get(baseURI + "books1")
                        .then()
                        .extract()
                        .response();

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void updateWhenUserAndPasswordAreRightShouldReturn200() {
        Book book = new Book(1L,"TestBook", 1, "77", LocalDate.of(2000, Month.JANUARY, 5));
        String newTitle="Fantasy Book";
        String newCode="33";

        Response response=
                given()
                        .auth()
                        .basic("authuser", "authpass")
                        .header("Content-type", "application/json")
                        .when()
                        .put(baseURI +"books/" + book.getId() +  "?title=" + newTitle + "&code=" + newCode)
                        .then()
                        .extract()
                        .response();

        Assertions.assertEquals(200,response.statusCode());
    }

    @Test
    public void addNewBookWhenPasswordAndUserAreRightShouldReturn200(){
        JSONObject request = new JSONObject();
        request.put("id", 5);
        request.put("title", "TestBook");
        request.put("edition", 1);
        request.put("code", 28);

        Book book = new Book(1L, "TestBook", 1, "77");
        Mockito.when(bookService.addNewBook(book)).thenReturn(book);

        Response response =
                    given()
                        .auth()
                        .basic("authuser", "authpass")
                        .header("Content-Type", "application/json")
                        .body(request.toJSONString())
                        .when()
                        .post(baseURI + "books")
                        .then().log().all()
                        .extract()
                        .response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Book " + book.getTitle() + " posted", response.getBody().asString());
    }


    @Test
    public void deleteWhenUserAndPasswordAreRightShouldReturn200() {
        Book book = new Book(1L, "TestBook", 1, "77");
        Mockito.when(bookService.deleteBook(book.getId())).thenReturn(book.getId());

        Response response = given()
                .auth()
                .basic("authuser","authpass")
                .when()
                .delete(baseURI + "books/" + book.getId())
                .then()
                .extract()
                .response();
    System.out.println(baseURI + "books/" + book.getId());
            Assertions.assertEquals(200, response.statusCode());
    }
}





