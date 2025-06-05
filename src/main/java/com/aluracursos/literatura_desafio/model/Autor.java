package com.aluracursos.literatura_desafio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity // Marca esta clase como una entidad JPA
@Table(name = "autores") // nombre de la tabla en la base de datos
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "libros")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GEneracion automatica de id por BD
    private Long id;

    @Column(unique = true, nullable = false) // columna unica y no nula
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // Relación One-to-Many con Libro.
    // 'mappedBy' indica que la relación es gestionada por el campo 'autor' en la clase Libro.
    // 'cascade = CascadeType.ALL' significa que si se guarda/elimina un autor, sus libros asociados también.
    // 'fetch = FetchType.EAGER' carga los libros del autor inmediatamente (considera LAZY para grandes conjuntos de datos)
    private List<Libro> libros = new ArrayList<>();

    // Constructor para crear un Autor a partir de DatosAutor de la API
    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }
    // Método para añadir un solo libro y mantener la bidireccionalidad
    public void addLibro(Libro libro){
        this.libros.add(libro);
        libro.setAutor(this);
    }
}
