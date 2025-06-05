package com.aluracursos.literatura_desafio.repository;

import com.aluracursos.literatura_desafio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Consulta para encontrar libros por titulos (ignore case)
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    // Consulta para encontrar libros por idioma
    List<Libro> findByIdiomasContains(String idioma);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();

}
