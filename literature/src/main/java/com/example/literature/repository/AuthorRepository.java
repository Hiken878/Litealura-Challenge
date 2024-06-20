package com.example.literature.repository;

import com.example.literature.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameContains(String name);
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer birthYear, Integer deathYear);
}
