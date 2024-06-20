package com.example.literature.main;

import com.example.literature.model.Author;
import com.example.literature.model.Book;
import com.example.literature.model.BookData;
import com.example.literature.model.Results;
import com.example.literature.repository.AuthorRepository;
import com.example.literature.repository.BookRepository;
import com.example.literature.service.ConsumptionAPI;
import com.example.literature.service.ConvertData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private ConvertData convertData = new ConvertData();
    private ConsumptionAPI consumptionApi = new ConsumptionAPI();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    List<Book> books;
    List<Author> authors;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        final var menu = """
                \n\t**** Porfavor Seleccione una Opción ****
                \t1 - Buscar Libro por Título
                \t2 - Lista de Libros Registrados
                \t3 - Lista de Autores Registrados
                \t4 - Buscar Autores Vivos por Año
                \t5 - Buscar Libros por Idioma
                \n\t0 - Salir
                """;
        var option = -1;
        System.out.println("****************************************");
        while (option != 0) {
            System.out.println(menu);
            System.out.print("*** Ingrese la opcion que desee consultar ---> " );
            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listAuthorsAliveInYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.out.println("FINALIZANDO LA APLICACIÓN...");
                    break;
                default:
                    System.out.println("Opción Invalida");
                    break;
            }
        }
        System.out.println("****************************************");
    }

    private void searchBookByTitle() {
        System.out.print("Ingrese el titulo a buscar: ");
        String inTitle = sc.nextLine();
        var json = consumptionApi.getData(inTitle.replace(" ", "%20"));
        //System.out.println("json: " + json);
        var data = convertData.getData(json, Results.class);
        //System.out.println("data: " + data);
        if (data.results().isEmpty()) {
            System.out.println("Libro Encontrado: ");
        } else {
            BookData bookData = data.results().getFirst();
            //System.out.println("bookData: " + bookData);
            Book book = new Book(bookData);
            //System.out.println("book: " + book);
            Author author = new Author().getFirstAuthor(bookData);
            //System.out.println("author: " + author);
            saveData(book, author);
        }
    }

    private void saveData(Book book, Author author) {
        Optional<Book> bookFound = bookRepository.findByTitleContains(book.getTitle());
        //System.out.println("bookFound: " + bookFound);
        if (bookFound.isPresent()) {
            System.out.println("Este libro ya fue registrado");
        } else {
            try {
                bookRepository.save(book);
                System.out.println("Libro registrado");
            } catch (Exception e) {
                System.out.println("Error...: " + e.getMessage());
            }
        }

        Optional<Author> authorFound = authorRepository.findByNameContains(author.getName());
        //System.out.println("authorFound: " + authorFound);
        if (authorFound.isPresent()) {
            System.out.println("Este Autor ya ha sido Registrado");
        } else {
            try {
                authorRepository.save(author);
                System.out.println("Autor Registrado");
            } catch (Exception e) {
                System.out.println("Error con autor...: " + e.getMessage());
            }
        }
    }

    private void listRegisteredBooks() {
        System.out.println("Lista de Libros Registrados\n---------------------");
        books = bookRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void listRegisteredAuthors() {
        System.out.println("Lista de Autores Registrados\n-----------------------");
        authors = authorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void listAuthorsAliveInYear() {
        System.out.print("Listar autores vivos por año determinado...Por favor, introduzca el año:");
        Integer year = Integer.valueOf(sc.nextLine());
        authors = authorRepository
                .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        if (authors.isEmpty()) {
            System.out.println("No hay Autores vivos en ese año");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .forEach(System.out::println);
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Lista de libros por Idioma\n----------------------");
        System.out.println("""
                \n\t---- Seleccione un Idioma ----
                \ten - English (INGLES)
                \tes - Spanish (ESPAÑOL)
                \tfr - French  (FRANCES)
                \tpt - Portuguese (PORTUGUES)
                """);
        String lang = sc.nextLine();
        books = bookRepository.findByLanguageContains(lang);
        if (books.isEmpty()) {
            System.out.println("Libros por idioma seleccionado no encontrados");
        } else {
            books.stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .forEach(System.out::println);
        }
    }
}
