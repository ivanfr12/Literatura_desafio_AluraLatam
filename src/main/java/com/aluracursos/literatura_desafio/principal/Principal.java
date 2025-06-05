package com.aluracursos.literatura_desafio.principal;

import com.aluracursos.literatura_desafio.model.*;
import com.aluracursos.literatura_desafio.repository.AutorRepository;
import com.aluracursos.literatura_desafio.repository.LibroRepository;
import com.aluracursos.literatura_desafio.service.ConsumoAPI;
import com.aluracursos.literatura_desafio.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component // Marca esta clase como componente de Spring para que pueda ser inyectada
public class Principal {
        private Scanner teclado = new Scanner(System.in); // leer la entrada del usuario
        // Crear una instancia del servicio para consumir la API
        private ConsumoAPI consumoAPI = new ConsumoAPI();
        private ConvierteDatos conversor = new ConvierteDatos(); // Instancia el conversor
        // Define la URL de la API de Gutendex
        private final String URL_BASE = "https://gutendex.com/books/"; // url base de la api
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

        public void exhibirMenu(){
            var opcion = -1;
            while (opcion != 0){
                var menu = """
                        --------------------------------------------------------------------------------------
                        Elija la opción a través de su número:
                        1- Buscar libro por titulo
                        2- Listar libros registrados
                        3- Listar autores registrados
                        4- Listar autores vivos en un determinado año
                        5- Listar libros por idioma
                        6- Mostrar estadisticas de Libros
                        7- Mostrar top 10 de libros
                        0- Salir
                        --------------------------------------------------------------------------------------
                        """;
                System.out.printf(menu);
                // Manejo de entrada no numerica
                while (!teclado.hasNext()){
                    System.out.printf("Entrada invalida. Por Favor, ingrese un número.");
                    teclado.nextLine(); // Consumir entrada invalida
                    System.out.printf(menu); // Mostrar el menú de nuevo.
                }
                opcion = teclado.nextInt();
                teclado.nextLine(); // Consumir el salto de linea despues de leer el entero

                switch (opcion){
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarEstadisticasLibros();
                        break;
                    case 7:
                        mostrarTop10Libros();
                        break;
                    case 0:
                        System.out.printf("Cerrando la aplicación. ¡Hasta luego!");
                        break;
                    default:
                        System.out.printf("Opción invalida. Por Favor, elija una opción del 0 al 5.");
                }
                System.out.println("\nPresione Entrar para continuar....");
                teclado.nextLine(); // Pausa hasta que el usuario presione Enter
            }
            teclado.close();// Cerrar el scanner cuando se salga del bucle principal
        }


    // Metodos para cada opcion del menu

    private void buscarLibroPorTitulo(){
        System.out.printf("Ingrese el titulo de libro que desea buscar: ");
        var tituloLibro = teclado.nextLine();
        // Codifica el titulo para la URL (Manejo de espacios y caracteres especiales)
        String tituloCodificado = tituloLibro.replace(" ", "%20");
        var url = URL_BASE + "?search=" + tituloCodificado; // Añade el parametro de busqueda

        String jsonResponse = consumoAPI.obtenerDatos(url);

        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            Datos datos = conversor.obtenerDatos(jsonResponse, Datos.class);

            // Filtra los libros que coincidan exactamente o parcialmente
            Optional<DatosLibro> libroEncontrado = datos.resultados().stream()
                    .filter(l -> l.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                    .findFirst();

            if (libroEncontrado.isPresent()){
                DatosLibro libro = libroEncontrado.get();
                guardarLibro(libro);
                System.out.printf("\n--- Libro encontrado ---\n");
                System.out.printf("Titulo: " + libro.titulo());
                if (!libro.autores().isEmpty()){
                    System.out.printf(" Autor: " + libro.autores().get(0).nombre());
                }
                System.out.printf(" Idiomas: " + String.join(", ", libro.idiomas()));
                System.out.printf(" Número de descargas: " + libro.numeroDeDescargas());
                // Mostrar mas detalles si los mapeaste (summaries, traductores)
                if (libro.summaries() != null && !libro.summaries().isEmpty()){
                    System.out.printf(" Resumen: " + libro.summaries().get(0)
                            .substring(0, Math.min(libro.summaries().get(0).length(), 200)) + "..."); // Mostrar un fragmento
                }
            }else {
                System.out.println("Libro no encontrado con ese título en la API.");
            }
        }else {
            System.out.println("No se obtuvo respuesta de la API o la respuesta está vacía.");
        }
    }

    // NUEVO METODO PARA GUARDAR LIBROS Y AUTOR EN LA BD
    private void guardarLibro(DatosLibro datosLibro){
        // 1. Verifica si el libro ya existe en la BD
        Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("--- El libro '" + datosLibro.titulo() + "' ya está registrado en la base de datos. ---");
            return; // Sale del método si ya existe
        }

        // 2. Convierte DatosLibro (del JSON) a Libro (entidad JPA)
        Libro libro = new Libro(datosLibro);

        // 3. Maneja el Autor (busca si ya existe o crea uno nuevo y lo asocia al libro)
        if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
            DatosAutor datosAutor = datosLibro.autores().get(0); // Toma el primer autor

            Optional<Autor> autorExistente = autorRepository.findByNombreContainingIgnoreCase(datosAutor.nombre());

            Autor autor;
            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
                System.out.println("--- Autor '" + autor.getNombre() + "' ya existe en la base de datos. Asociando libro. ---");
            } else {
                autor = new Autor(datosAutor);
                autor = autorRepository.save(autor);
                System.out.println("--- Nuevo autor '" + autor.getNombre() + "' guardado en la base de datos. ---");
            }
            libro.setAutor(autor); // Asigna el autor al libro
            // Asegura la bidireccionalidad si el autor ya existía
            if (autor.getLibros() == null) {
                autor.setLibros(new ArrayList<>());
            }
            autor.getLibros().add(libro); // Agrega el libro a la lista de libros del autor
        } else {
            System.out.println("--- Advertencia: Libro sin autor encontrado. ---");
        }

        // 4. Guarda el libro (y el autor si es nuevo, gracias a CascadeType.ALL en Autor)
        try {
            libroRepository.save(libro);
            System.out.println("--- Libro '" + libro.getTitulo() + "' guardado exitosamente en la base de datos. ---");
        } catch (Exception e) {
            System.err.println("--- Error al guardar el libro y/o autor: " + e.getMessage() + " ---");
            e.printStackTrace();
        }
    }

    private void listarLibrosRegistrados(){
        List<Libro> libros = libroRepository.findAll(); // Obtiene todos los libros de la base de datos
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n--- Libros registrados en la base de datos ---");
            libros.forEach(System.out::println); // Usa el toString de Libro
        }
    }
    private void listarAutoresRegistrados(){
        List<Autor> autores = autorRepository.findAll(); // Obtiene todos los autores de la base de datos
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n--- Autores registrados en la base de datos ---");
            autores.forEach(System.out::println); // Usa el toString de Autor
        }
    }

    private void listarAutoresVivosPorAno() {
            System.out.println("Ingrese el año para verificar autores vivos:");
            while (!teclado.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, ingrese un año válido (número).");
                teclado.nextLine();
            }
            var ano = teclado.nextInt();
            teclado.nextLine();

            // Usa la consulta personalizada del repositorio
        List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAno(ano);

        if (autoresVivos.isEmpty()){
            System.out.println("No se encontraron autores vivos en el año " + ano + " en la base de datos");
        }else {
            System.out.println("\n--- Autores vivos en el año " + ano + "(de la base de datos) ---");
            autoresVivos.forEach(a -> System.out.println("- " + a.getNombre() +
                    " (Nacimiento: " + a.getFechaNacimiento() + ", Fallecimiento: " + (a.getFechaNacimiento() != null
            ? a.getFechaNacimiento() : "Vivo" + ")")));
        }
    }

    private void listarLibrosPorIdioma() {
            System.out.println("Ingrese el idioma para buscar libros (ej. en, es, fr, pt):");
            var idioma = teclado.nextLine().toLowerCase();

            // Usa la consulta del repositorio para filtrar por idioma
            List<Libro> librosPorIdioma = libroRepository.findByIdiomasContains(idioma);

            if (librosPorIdioma.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma '" + idioma + "' en la base de datos.");
            } else {
                System.out.println("\n--- Libros en idioma '" + idioma + "' (de la base de datos) ---");
                // ESTA LÍNEA MUESTRA LA CANTIDAD!
                System.out.println("Cantidad de libros en '" + idioma + "': " + librosPorIdioma.size());
                System.out.println("------------------------------------------");
                librosPorIdioma.forEach(System.out::println);
            }
        }

    private void mostrarEstadisticasLibros() {
        List<Libro> libros = libroRepository.findAll(); // Obtiene todos los libros de la base de datos

        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getNumeroDescargas) // Mapea a un stream de doubles para el cálculo
                .summaryStatistics(); // Calcula las estadísticas

        System.out.println("\n--- Estadísticas de Descargas de Libros ---");
        System.out.println("Promedio de descargas: " + String.format("%.2f", stats.getAverage())); // Formato para 2 decimales
        System.out.println(" Máximo de descargas: " + stats.getMax());
        System.out.println(" Mínimo de descargas: " + stats.getMin());
        System.out.println(" Total de libros considerados: " + stats.getCount());
        System.out.println(" Suma total de descargas: " + stats.getSum());
        System.out.println("------------------------------------------");
    }

    private void mostrarTop10Libros() {
        // Utiliza el método del repositorio
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();

        if (top10Libros.isEmpty()) {
            System.out.println("No hay suficientes libros para mostrar el top 10.");
        } else {
            System.out.println("\n--- Top 10 Libros Más Descargados ---");
            top10Libros.forEach(l -> System.out.println("Título: " + l.getTitulo() + " - Descargas: " + l.getNumeroDescargas()));
        }
    }
}
