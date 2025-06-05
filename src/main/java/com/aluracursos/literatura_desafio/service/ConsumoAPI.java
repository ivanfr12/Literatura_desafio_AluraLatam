package com.aluracursos.literatura_desafio.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    public String obtenerDatos(String url){
        // Contruye un cliente HTTP para enviar solicitudes
        HttpClient client = HttpClient.newHttpClient();

        // Construye la solicitud Get para la url
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Enviar la solicitud y recibe la respuesta
        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica si la solicitud fue exitosa
            if (response.statusCode() == 200){
                return response.body(); // Devuelve el cuerpo de la respuesta (JSON)
            }else {
                // Manejo basico de errores
                System.out.printf("Error en la solicitud: Codigo de estado " + response.statusCode());
                System.out.printf("Cuerpo del error: " + response.body());
                return null;
            }
        }catch (IOException | InterruptedException e){
            // Captura y maneja excepciones
            System.out.printf("Error al realizar la solicitud HTTP: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
