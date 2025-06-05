package com.aluracursos.literatura_desafio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "libros")
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Genera constructor con todos los argumentos
@ToString(exclude = "autor")  // Excluye 'autor' del toString para evitar bucles
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "idiomas_libro", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    private List<String> idiomas;

    private Long numeroDescargas;

    // Constructor para crear un libro a partir de DatosLibro de la API
    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idiomas = datosLibro.idiomas();
        this.numeroDescargas = datosLibro.numeroDeDescargas();
    }
}
