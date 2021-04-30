package com.example.bookmanagement.BookPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity //Marca algo que queremos que seja representado em uma tabela. Não pode ser final. Tem que ter PK e um no-arg constructor.
@Table //Específica o nome da tabela e/ou do schema. Se for ser igual ao da entity, não prcisa colocar.
@Data // Anotação do Lombok que provê as funcionalidades @Getter, @Setter, @ToString, @EqualsAndHashCode e @RequiredArgsConstructor.
@AllArgsConstructor //Anotação do Lombok que provê um construtor para cada atributo na classe.
@NoArgsConstructor // Idem, construtor sem atributos.

// OBS: SE O LOMBOK DER PAU COM JPA POR CAUSA DE EQUALS E HASH CODE, CONFERIR AQUI https://www.baeldung.com/intro-to-project-lombok

public class Book{ //Representa um objeto.

    @Id //Define a PK.
    @SequenceGenerator(
            name="books_sequence",
            sequenceName = "books_sequence",
            allocationSize = 1
    )
    @GeneratedValue(   //Define como a PK é gerada. O sequence abaixo é o default para Postgres.
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private long id;
    private String title;
    private int edition;
    private String code;
    private LocalDate dateOfPrint;
    @Transient
    private int age;

    public Book(String title, int edition, String code, LocalDate dateOfPrint) {
        this.title = title;
        this.edition = edition;
        this.dateOfPrint = dateOfPrint;
        this.code = code;
    }

    public Book(long id, String title, int edition, String code, LocalDate dateOfPrint) {
        this.id = id;
        this.title = title;
        this.edition = edition;
        this.dateOfPrint = dateOfPrint;
        this.code = code;
    }

    public Book(long id, String title, int edition, String code) {
        this.id = id;
        this.title = title;
        this.edition = edition;
        this.code = code;
    }
}