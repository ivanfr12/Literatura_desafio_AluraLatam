package com.aluracursos.literatura_desafio.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro (@JsonAlias("id") Long id,
                          @JsonAlias("title") String titulo,
                          @JsonAlias("authors")List<DatosAutor> autores, // Lista de objetos DatosAutor
                          @JsonAlias("translators") List<DatosAutor> traductores,
                          @JsonAlias("languages") List<String> idiomas,
                          @JsonAlias("download_count") Long numeroDeDescargas,
                          @JsonAlias("summaries") List<String> summaries
                         ){
}
