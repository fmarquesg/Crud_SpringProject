package com.example.bookmanagement.SecurityPackage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

public class ApplicationSecurityConfigIntegTest {

        @LocalServerPort
        private int port;

        // Define a URI Base
        @PostConstruct
        public void init() {
            baseURI = "http://localhost:" + port + "/";
        }

        @Test
        public void testIfStatusCodeReturnedIs200(){
            given().auth()
                    .basic("authuser", "authpass")
                    .when()
                    .get(baseURI + "books")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        public void testIfStatusCodeReturnedIs401(){
            given().auth()
                    .basic("", "")
                    .when()
                    .get(baseURI + "books")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        public void testIfStatusCodeReturnedIs404(){
            given().auth()
                    .basic("authuser", "authpass")
                    .when()
                    .get(baseURI + "books1")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
}

