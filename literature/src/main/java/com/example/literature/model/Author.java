package com.example.literature.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;

    public Author() {
    }

    public Author(AuthorData authorData) {
        this.name = authorData.name();
        this.birthYear = Integer.valueOf(authorData.birthYear());
        this.deathYear = Integer.valueOf(authorData.deathYear());
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirth_year() {
        return birthYear;
    }

    public void setBirth_year(Integer birth_year) {
        this.birthYear = birth_year;
    }

    public Integer getDeath_year() {
        return deathYear;
    }

    public void setDeath_year(Integer death_year) {
        this.deathYear = death_year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Author getFirstAuthor(BookData bookData) {
        AuthorData authorData = bookData.author().getFirst();
        return new Author(authorData);
    }

    @Override
    public String toString() {
        return "**** Datos de Autor ****" +
                "\n\tNombre: " + name +
                "\n\tAño de Nacimiento: " + birthYear +
                "\n\tAño de Fallecimiento " + deathYear;
    }
}
