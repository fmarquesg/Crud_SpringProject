package com.example.bookmanagement.BookPackage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class BookConfig {

    //Indica que um bean dentro da Spring Application deve run.
    @Bean
    CommandLineRunner commandLineRunner(BookRepository repository){
        return args -> {
            Book whiteTiger = new Book(
                    "White Tiger",
                    1,
                    "11",
                    LocalDate.of(2000, Month.JANUARY, 5)
            );
            Book oscarWao = new Book(
                    "The Brief Wondrous Life of Oscar Wao",
                    2,
                    "12",
                    LocalDate.of(2010, Month.FEBRUARY, 10)
            );

            repository.saveAll(
                    List.of(whiteTiger, oscarWao)
            );
        };
    }
}
