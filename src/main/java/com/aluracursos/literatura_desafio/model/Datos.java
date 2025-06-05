package com.aluracursos.literatura_desafio.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Datos( @JsonAlias("count") Long count,
                     @JsonAlias("next") String next,
                     @JsonAlias("previous") String previous,
                     @JsonAlias("results") List<DatosLibro> resultados) {
}
