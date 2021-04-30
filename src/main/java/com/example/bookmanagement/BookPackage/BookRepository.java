package com.example.bookmanagement.BookPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository //Cuida do acesso aos dados no DB.
public interface BookRepository extends JpaRepository<Book, Long>{ //Passa o objeto e o tipo de ID nos parâmetros.

    Optional<Book> findByCode(String code);
}
