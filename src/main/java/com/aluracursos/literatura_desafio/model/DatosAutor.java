package com.aluracursos.literatura_desafio.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora propiedades Json que no tienen un campo en esta clase
public record DatosAutor (@JsonAlias("name") String nombre, // Mapea name del Json a nombre en Java
                          @JsonAlias("birth_year") Integer fechaNacimiento,
                          @JsonAlias("death_year") Integer fechaFallecimiento){
}
