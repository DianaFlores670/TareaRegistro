Autor: DIANA CECILIA FLORES CHACON

Carrera de Informatica - Facultad de Ciencias Puras y Naturales - UMSA

# 📚 Proyecto: Sistema de Registro Universitario

Este proyecto consiste en el desarrollo de un sistema de registro universitario que permite gestionar estudiantes, materias, inscripciones, docentes y evaluaciones docentes. Está construido siguiendo buenas prácticas de desarrollo backend con Spring Boot, utilizando autenticación con JWT, cache con Redis y documentación automática de APIs con Swagger.

---

##Tecnologías Utilizadas

- *Spring Boot*: Framework principal para el backend.
- *PostgreSQL*: Sistema de gestión de base de datos relacional.
- *Redis*: Almacenamiento en caché para mejorar el rendimiento.
- *JWT (JSON Web Token)*: Autenticación y autorización segura.
- *Swagger*: Documentación interactiva de la API REST.
- *Maven*: Sistema de gestión de dependencias y construcción.

---

## ⚙ Ejecución del Proyecto

### 1. Clonar el repositorio

bash
git clone https://github.com/DianaFlores670/TareaRegistro.git
cd Practica-2
 
 ## 2. Contruccion y ejecucion del proyecto
 Puedes correr el proyecto de dos maneras:

#### Opción A: Desde tu IDE (IntelliJ, Eclipse)
Ejecuta la clase principal:
bash
com.registrouniversitario.UniversidadApplication
 
#### Opción B: Desde la terminal
bash
mvn clean install
java -jar target/registrouniversitario-0.0.1-SNAPSHOT.jar
 
Asegúrate de tener PostgreSQL y Redis configurados correctamente.
## 3. Seguridad con JWT
Todos los endpoints están protegidos mediante autenticación con JWT. Para acceder, es necesario iniciar sesión con credenciales válidas. Se definen los siguientes roles:
-   ADMIN
-   DOCENTE
-   ESTUDIANTE
### Endpoint de autenticación
bash
POST /api/auth/login
 
### Cuerpo de ejemplo:
bash
{
  "username": "usuario_prueba",
  "password": "contrasena_segura"
}
 
### Respuesta:
bash
{
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
 
Este token debe enviarse en el header 'Autorization' como:  
Bearer eyJhbGciOiJIUzI1NiIsInR...
## 4. Endpoint disposnibles
### 4.1. Estudiantes
-   GET /api/estudiantes → Obtiene la lista de todos los estudiantes.
-   GET /api/estudiantes/inscripcion/{numeroInscripcion} → Obtiene un estudiante por su número de inscripción.
-   GET /api/estudiantes/{id}/materias → Obtiene las materias asociadas a un estudiante por su ID.
-   GET /api/estudiantes/{id}/lock → Obtiene un estudiante con bloqueo por su ID.
-   POST /api/estudiantes → Crea un nuevo estudiante.
-   PUT /api/estudiantes/{id} → Actualiza un estudiante existente por su ID.
-   PUT /api/estudiantes/{id}/baja → Da de baja a un estudiante por su ID.
-   GET /api/estudiantes/activos → Obtiene la lista de estudiantes activos.
### 4.2. Materias
-   GET /api/materias → Obtiene la lista de todas las materias.
-   GET /api/materias/{id} → Obtiene una materia por su ID.
-   POST /api/materias → Crea una nueva materia.
-   PUT /api/materias/{id} → Actualiza una materia por su ID.
-   DELETE /api/materias/{id} → Elimina una materia por su ID.
-   GET /api/materias/docente/{docenteId} → Obtiene las materias de un docente por su ID.
### 4.3. Docentes
-   GET /api/docentes → Obtiene la lista de todos los docentes.
-   GET /api/docentes/{id} → Obtiene un docente por su ID.
-   GET /api/docentes/{id}/materias → Obtiene las materias asociadas a un docente por su ID.
-   POST /api/docentes → Crea un nuevo docente.
-   PUT /api/docentes/{id} → Actualiza un docente existente por su ID.
-   DELETE /api/docentes/{id} → Elimina un docente por su ID.
### 4.4. EvaluacionDocente
-   POST /api/evaluaciones-docente → Crea una nueva evaluación docente.
-   GET /api/evaluaciones-docente/docente/{docenteId} → Obtiene las evaluaciones de un docente por su ID.
-   GET /api/evaluaciones-docente/{id} → Obtiene una evaluación docente por su ID.
-   DELETE /api/evaluaciones-docente/{id} → Elimina una evaluación docente por su ID.
### 4.5. Inscripciones
-   POST /api/inscripciones → Crea una nueva inscripción.
-   GET /api/inscripciones/estudiante/{id} → Obtiene la inscripción de un estudiante por su ID.
-   PUT /api/inscripciones/{id} → Actualiza una inscripción por su ID.
-   DELETE /api/inscripciones/{id} → Elimina una inscripción por su ID.
-   GET /api/inscripciones → Obtiene la lista de todas las inscripciones.
## 5. Estructura del proyecto
## Estructura del Proyecto

plaintext
src/
└── main/
    ├── java/
    │   └── com/universidad/
    │       ├── controller/           # Controladores REST de la aplicación
    │       ├── dto/                  # Objetos de transferencia de datos (DTOs)
    │       ├── model/                # Entidades y modelos del dominio
    │       ├── registro/             # Módulo de autenticación/autorización (login, roles, JWT)
    │       ├── repository/           # Repositorios JPA para acceso a base de datos
    │       ├── service/              # Lógica de negocio del sistema
    │       ├── validation/           # Validaciones personalizadas
    │       └── UniversidadApplication.java  # Clase principal del proyecto
    ├── resources/                    # Archivos de configuración y recursos estáticos
└── target/                           # Archivos generados por Maven

## 6. Modelo Relacional de Base de Datos
-   usuarios(id, activo, apellido, nombre, email, password, username)
    
-   roles(id, nombre)
    
-   usuario_roles(usuario_id, rol_id)
    
-   persona(id_persona, apellido, nombre, email, fecha_nacimiento, version)
    
-   estudiante(numero_inscripcion, id_persona, estado, fecha_hora, fecha_modificacion, usuario_alta, usuario_baja, usuario_modificacion)
    
-   docente(id, id_persona, departamento, especialidad, nro_empleado, nombre)
    
-   materia(id_materia, codigo_unico, creditos, nombre_materia, version, id_docente)
    
-   inscripcion(id, id_estudiante, id_materia, fecha)
    
-   evaluacion_docente(id, id_estudiante, id_docente, comentario, fecha, puntacion)
    
-   estudiante_materia(id_estudiante, id_materia)
    
-   materia_prerequisito(id_materia, id_prerequisito)
    
-   unidad_tematica(id, id_materia, descripcion, titulo)
    
-   spring_session(...) y spring_session_attributes(...): Para manejo de sesiones (Redis)
## 7. Documentacion con Swagger
Una vez ejecutado el proyecto, puedes acceder a la documentación automática:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Desde allí puedes probar los endpoints y revisar sus parámetros, respuestas y códigos de estado HTTP.

## 8. Pruebas
-   Todos los endpoints han sido probados usando Swagger.
    
-   JWT protegido correctamente.
    
-   Validaciones con anotaciones (@NotNull, @Email, etc.).
    
-   Manejo de excepciones implementado con @RestControllerAdvice.
## 9. Base de Datos de ejemplo
El archivo univesidad.sql incluido en el repositorio contiene datos de prueba para:

-   Usuarios con diferentes roles
    
-   Estudiantes registrados
    
-   Materias y docentes asignados
    
-   Inscripciones activas
## 10. Enlace del proyecto en github
https://github.com/JuanCamachoFernandez/Practica-2

## 11. Estado del Proyecto
✔ Módulos funcionales implementados  
✔ JWT, validaciones, manejo de errores y cache configurados  
✔ Documentación completa  
✔ Código organizado y probado
