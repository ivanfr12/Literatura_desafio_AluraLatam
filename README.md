Literatura Desafío
#AluraLatam #Oracle

Este proyecto es una aplicación de consola desarrollada con Spring Boot que interactúa con la API de Gutendex (una API pública de libros de dominio público) y persiste los datos relevantes en una base de datos PostgreSQL. Permite a los usuarios buscar libros, listar libros y autores registrados, obtener estadísticas y más.

🚀 Funcionalidades

Buscar libro por título: Busca un libro en la API de Gutendex y, si lo encuentra, lo guarda en la base de datos (si no existe ya), junto con su autor.

Listar libros registrados: Muestra todos los libros guardados en la base de datos.

Listar autores registrados: Muestra todos los autores guardados en la base de datos.

Listar autores vivos en un determinado año: Permite al usuario ingresar un año y muestra qué autores registrados estaban vivos en ese período.

Listar libros por idioma: Permite al usuario ingresar un idioma (ej. es, en, fr) y muestra los libros disponibles en ese idioma en la base de datos, incluyendo la cantidad.

Mostrar estadísticas de libros: Proporciona estadísticas sobre el número de descargas de los libros en la base de datos (promedio, máximo, mínimo, total, suma).

Mostrar Top 10 de libros: Presenta los 10 libros más descargados de la base de datos.

🛠️ Tecnologías Utilizadas

Java 17+

Spring Boot 3.x

Spring Data JPA

Hibernate

PostgreSQL (como base de datos relacional)

Lombok (para reducir el código boilerplate en las entidades)

Jackson (para el procesamiento de JSON desde la API)

API de Gutendex (para la obtención de datos de libros)

🗄️ Configuración de la Base de Datos (PostgreSQL)

Para que la aplicación funcione correctamente, necesitas tener una instancia de PostgreSQL corriendo y configurar sus credenciales en el archivo src/main/resources/application.properties.


Instala PostgreSQL: Si no lo tienes, puedes descargarlo e instalarlo desde el sitio oficial de PostgreSQL.


Crea una base de datos: Abre psql o una herramienta como pgAdmin y crea una nueva base de datos para este proyecto. Por ejemplo:
CREATE DATABASE literatura_db;

Configura application.properties: 
Abre el archivo src/main/resources/application.properties y ajusta las siguientes propiedades con tus credenciales y nombre de base de datos:

spring.datasource.url=jdbc:postgresql://localhost:5432/literatura_db

spring.datasource.username=your_username # Reemplaza con tu usuario de PostgreSQL

spring.datasource.password=your_password # Reemplaza con tu contraseña de PostgreSQL

spring.jpa.hibernate.ddl-auto=update # Esto creará/actualizará las tablas automáticamente

spring.jpa.show-sql=true # Muestra las consultas SQL en la consola

spring.jpa.properties.hibernate.format_sql=true

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

Asegúrate de que el spring.jpa.hibernate.ddl-auto esté en update para que Hibernate cree automáticamente las tablas autores y libros al iniciar la aplicación.

⚙️ Cómo Ejecutar el Proyecto
git clone https://github.com/ivanfr12/Literatura_desafio_AluraLatam

cd LiteraturaDesafio


Abre el proyecto en tu IDE: Importa el proyecto como un proyecto Maven en IntelliJ IDEA, Eclipse o VS Code.

Instala las dependencias de Lombok: Asegúrate de tener el plugin de Lombok instalado en tu IDE para que reconozca los métodos generados automáticamente.

Configura la base de datos: Sigue los pasos de la sección "Configuración de la Base de Datos (PostgreSQL)".

Ejecuta la aplicación:

Desde tu IDE: Ejecuta la clase LiteraturaDesafioApplication.java como una aplicación Spring Boot.

Desde la terminal (Maven):
mvn spring-boot:run

La aplicación se iniciará y mostrará el menú principal en la consola.

📊 Diagrama de Flujo Principal (Texto)

Este diagrama representa el flujo principal de la aplicación desde el inicio hasta el fin, incluyendo las interacciones con el usuario y la persistencia de datos.


Inicio de la Aplicación
        │
        ▼
 +-----------------------------------------------------+
 │ Mostrar Menú de Opciones al Usuario                 │
 │ (1: Buscar, 2: Listar Libros, etc.)                  │
 +-----------------------------------------------------+
        │
        ▼
 +-----------------------+
 │ Leer Opción del Usuario │
 +-----------------------+
        │
        ▼
 +-----------------------+
 │ Validar Opción        │
 +-----------------------+
        │
        ├─── No ───────
        │             │
        ▼             │
 +---------------------+   Sí
 │ Mensaje: Opción     │<───────────────────
 │ Inválida            │                      │
 +---------------------+                      │
        │                                    │
        ▼                                    │
 +-------------------------------------------+       │
 │ Pausar y Volver a Mostrar Menú            │       │
 +-------------------------------------------+       │
        ▲                                    │
        │                                    │
 +------------------------------------------------------------------------------------------+
 │ Opción 1: Buscar libro por título                                                         │
 +------------------------------------------------------------------------------------------+
        │
        ▼
 +----------------------------+
 │ Pedir Título al Usuario    │
 +----------------------------+
        │
        ▼
 +----------------------------+
 │ Consumir API Gutendex con  │
 │ Título                     │
 +----------------------------+
        │
        ▼
 +----------------------------+
 │ Recibir y Procesar JSON    │
 +----------------------------+
        │
        ├─── Libro Encontrado ───
        │                      │
        ▼                      │
 +---------------------+   No
 │ Libro ya en BD?     │<───────────────────
 +---------------------+                      │
        │                                    │
        ├─── Sí ───────                        │
        │             │                      │
        ▼             ▼                      │
 +---------------------+---------------------+
 │ Mensaje: Libro ya   │ Crear Entidades      │
 │ existe              │ (Libro y Autor(es))  │
 +---------------------+---------------------+
        │                     │
        ▼                     ▼
 +---------------------+   +-----------------+
 │ Fin Búsqueda        │   │ Persistir Autor │
 │ (no se guarda)      │   │ en Base de Datos│
 +---------------------+   +-----------------+
        │                     │
        │                     ▼
        │                   +-----------------+
        │                   │ Persistir Libro │
        │                   │ en Base de Datos│
        │                   +-----------------+
        │                           │
        ▼                           ▼
 +-----------------------------------------------------+
 │ Mostrar Detalles del Libro (Encontrado y/o Guardado) │
 +-----------------------------------------------------+
        │
        ▼
 +-------------------------------------------+
 │ Pausar y Volver a Mostrar Menú            │
 +-------------------------------------------+
        ▲
        │
 +----------------------------------------------------------------------------------------------------------------------------------+
 │ Opciones 2-7: Listar Libros, Autores, Autores Vivos, Libros por Idioma, Estadísticas, Top 10                              │
 +----------------------------------------------------------------------------------------------------------------------------------+
        │
        ▼
 +-------------------------------------------+
 │ Realizar Consulta a Base de Datos          │
 │ (usando Repositories)                      │
 +-------------------------------------------+
        │
        ▼
 +-------------------------------------------+
 │ Mostrar Resultados al Usuario             │
 +-------------------------------------------+
        │
        ▼
 +-------------------------------------------+
 │ Pausar y Volver a Mostrar Menú            │
 +-------------------------------------------+
        ▲
        │
 +-------------------------------------------------------------------------------------------------------------------------------------+
 │ Opción 0: Salir                                                                                                                   │
 +-------------------------------------------------------------------------------------------------------------------------------------+
        │
        ▼
 +------------------+
 │ Mensaje: Adiós    │
 +------------------+
        │
        ▼
 +------------------+
 │ Fin Aplicación   │
 +------------------+


