package com.aluracursos.literatura_desafio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos{
    private ObjectMapper objectMapper = new ObjectMapper(); // Objeto principal para mapeo Json

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase){
        try {
            // Mapea la cadena Json a un objeto de la clase espeficada
            return objectMapper.readValue(json, clase);
        }catch (JsonProcessingException e){
            // Manejo de errores si el json no puede ser procesado
            System.out.println("Error al procesar JSON: " + e.getMessage());
            throw new RuntimeException(e); // Propaga la excepcion
        }
    }
}
