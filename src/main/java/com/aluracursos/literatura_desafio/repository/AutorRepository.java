package com.aluracursos.literatura_desafio.repository;

import com.aluracursos.literatura_desafio.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Spring Data JPA puede generar consultas automaticamente por el nombre del metodo
    Optional<Autor> findByNombreContainingIgnoreCase(String nombre);

    // Consulta personalizada para encontrar autores vivos en un año especifico
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :ano AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :ano)")
    List<Autor> findAutoresVivosEnAno(Integer ano);
}
