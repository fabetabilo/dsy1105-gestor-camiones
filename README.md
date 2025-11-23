# Logística Austral – Microservicio de Gestión de Camiones Usados

Microservicio backend (Spring Boot) para administrar el catálogo de camiones usados que se pondrán a la venta en la empresa Logística Austral: creación, consulta, actualización y eliminación de registros de camiones, incluyendo metadatos básicos y una ruta pública a la imagen asociada.

## Objetivo
Centralizar la información de camiones disponibles para venta: estado, disponibilidad, características técnicas (capacidad, tracción, año, etc.) y referencia a la imagen en un directorio público del servidor.

## Stack Tecnológico
- Java JDK 21
- Spring Boot 4.0.0 (Web MVC + Data JPA)
- Spring Web (API REST)
- Spring Data JPA (persistencia)
- MySQL (motor de base de datos)
- Lombok (reducción de boilerplate en entidades y servicios)

> Nota: La versión 4.0.0 de Spring Boot es de línea futura / milestone. Para entornos productivos conviene estabilizar en una rama 3.3.x si surgen incompatibilidades.

## Dependencias principales (pom.xml)
Incluidas vía Spring Initializr y ajustadas posteriormente:
- `spring-boot-starter-webmvc`
- `spring-boot-starter-data-jpa`
- `mysql-connector-j` (scope runtime)
- `lombok`

## Modelo Principal
Entidad `Camion` (`src/main/java/.../model/Camion.java`):
```java
@Entity
@Table(name = "camion")
public class Camion {
		@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		private String patente;          // Única, obligatoria
		private String marca;
		private String modelo;
		private Integer annio;
		private String tipo;
		private Integer capacidad;       // En Kg (interpretación)
		private Boolean disponibilidad;  // 1 = disponible, 0 = no
		private String estado;           // Ej: Disponible, En reparación
		private String descripcion;
		private String traccion;         // Ej: 6x4
		private Integer precio;          // Moneda local (entero)
		private String imagenUri;        // Ruta pública en servidor
}
```

## Configuración
Archivo `application.properties`:
```properties
spring.application.name=log-austral
spring.datasource.url=jdbc:mysql://localhost:3306/db_log_austral
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
# Dialecto removido para autodescubrimiento con Hibernate 7
```
Si necesitas forzar el dialecto: `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect`.

## Requisitos Previos
1. JDK 21 instalado y en el `PATH`.
2. MySQL en ejecución y base de datos creada: `db_log_austral`.
3. Usuario MySQL con permisos de CRUD.
4. (Opcional) Postman para pruebas de endpoints.
5. Maven Wrapper incluido (`mvnw`, no necesitas instalar Maven globalmente).

## Instalación y Ejecución
Clonar el repo y arrancar el servicio:
```powershell
git clone <URL_DEL_REPO>
cd log-austral
.\mvnw clean spring-boot:run
```
El servidor se inicia (por defecto) en: `http://localhost:8080`.

## Carga de Datos de Demo
Archivo `demo.sql` incluido en raíz del proyecto.
Ejecutar en MySQL / HeidiSQL:
```sql
USE db_log_austral;
-- Contiene 10 INSERT individuales en la tabla camion
SOURCE demo.sql; -- (o copiar/pegar su contenido)
```

## Endpoints REST
Base path: `/api/v1/camion`

| Método | Ruta                          | Descripción                                                      | Respuestas clave |
|--------|-------------------------------|------------------------------------------------------------------|------------------|
| GET    | `/api/v1/camion`              | Listar todos los camiones                                        | 200 / 204        |
| GET    | `/api/v1/camion/{id}`         | Obtener un camión por ID                                          | 200 / 404        |
| POST   | `/api/v1/camion`              | Crear nuevo camión (JSON simple)                                 | 201 / 400        |
| POST   | `/api/v1/camion/with-image`   | Crear camión con imagen (multipart: `camion` + `file`)           | 201 / 400 / 500  |
| POST   | `/api/v1/camion/{id}/imagen`  | Subir/actualizar imagen de un camión existente                   | 200 / 404 / 400 / 500 |
| PUT    | `/api/v1/camion/{id}`         | Actualizar campos (usa datos del JSON enviado)                   | 200 / 404 / 400 |
| DELETE | `/api/v1/camion/{id}`         | Eliminar un camión                                               | 204 / 404        |

Nota PUT: Lógica ya corregida; ahora aplica los valores del body. Si `imagenUri` viene `null` se conserva la existente.

### Subida de Camión con Imagen (Multipart)
Archivo de ejemplos: `demoJSON.txt` (en la raíz del repo) contiene varios cuerpos JSON usados para la key `camion` en Postman.

POST `http://HOST:8080/api/v1/camion/with-image`

Body → form-data (NO usar raw):
| Key     | Tipo    | Content-Type       | Descripción                                           |
|---------|---------|--------------------|-------------------------------------------------------|
| camion  | Text    | application/json   | JSON de la entidad (sin `imagenUri`, se genera luego) |
| file    | File    | auto (imagen)      | Archivo de imagen (.jpg, .png, .webp)                 |

Ejemplo valor para key `camion` (extraído de `demoJSON.txt`):
```json
{
	"patente": "AAA111",
	"marca": "Mercedes-Benz",
	"modelo": "Actros 1845",
	"annio": 2020,
	"tipo": "Tracto",
	"capacidad": 32000,
	"disponibilidad": true,
	"estado": "Disponible",
	"descripcion": "Actros para larga distancia",
	"traccion": "6x4",
	"precio": 84500000
}
```

Ejemplo respuesta 201:
```json
{
	"id": 1,
	"patente": "AAA111",
	"marca": "Mercedes-Benz",
	"modelo": "Actros 1845",
	"annio": 2020,
	"tipo": "Tracto",
	"capacidad": 32000,
	"disponibilidad": true,
	"estado": "Disponible",
	"descripcion": "Actros para larga distancia",
	"traccion": "6x4",
	"precio": 84500000,
	"imagenUri": "/camiones/a71a39be-1fd3-4279-8ede-43ad21668edc.png"
}
```

Acceso a la imagen: `http://HOST:8080/camiones/a71a39be-1fd3-4279-8ede-43ad21668edc.png`

Ejemplo cURL multipart:
```bash
curl -X POST http://HOST:8080/api/v1/camion/with-image \
	-F 'camion={"patente":"AAA111","marca":"Mercedes-Benz","modelo":"Actros 1845","annio":2020,"tipo":"Tracto","capacidad":32000,"disponibilidad":true,"estado":"Disponible","descripcion":"Actros para larga distancia","traccion":"6x4","precio":84500000};type=application/json' \
	-F 'file=@./actros1845.png'
```

Actualizar solo imagen de un camión existente:
```bash
curl -X POST http://HOST:8080/api/v1/camion/1/imagen \
	-F 'file=@./nueva.png'
```

Si se desea actualizar datos SIN cambiar imagen: usar PUT y omitir `imagenUri`.

### Ejemplo JSON (POST simple sin imagen)
```json
{
	"patente": "ZZZ999",
	"marca": "Scania",
	"modelo": "R450",
	"annio": 2021,
	"tipo": "Tracto",
	"capacidad": 34000,
	"disponibilidad": true,
	"estado": "Disponible",
	"descripcion": "Unidad en excelente estado",
	"traccion": "6x4",
	"precio": 91000000,
	"imagenUri": "/public/camiones/zzr450.jpg"
}
```

### Ejemplo cURL
```bash
curl -X POST http://localhost:8080/api/v1/camion \ 
	-H "Content-Type: application/json" \ 
	-d '{
		"patente":"ZZZ999",
		"marca":"Scania",
		"modelo":"R450",
		"annio":2021,
		"tipo":"Tracto",
		"capacidad":34000,
		"disponibilidad":true,
		"estado":"Disponible",
		"descripcion":"Unidad en excelente estado",
		"traccion":"6x4",
		"precio":91000000,
		"imagenUri":"/public/camiones/zzr450.jpg"
	}'
```

## Pruebas Manuales (Postman)
1. Crear colección "Logística Austral".
2. Agregar requests para cada endpoint.
3. Validar códigos de estado esperados: 201 para creación, 200 para lecturas, 204 en borrado.

## Imágenes Públicas
`imagenUri` se construye automáticamente al subir archivos y apunta al directorio público configurado (ej. `/opt/spring/public/camiones`). Spring sirve estos recursos añadiendo la ubicación en `spring.web.resources.static-locations`. Para que se vea externamente, bastará acceder a `http://HOST:8080/camiones/<nombre>.ext`.

## Mejoras Futuras
- Corrección lógica del método PUT para aplicar datos del body.
- Manejo de validaciones (Bean Validation + mensajes claros).
- Paginación y filtros en listado.
- Validación adicional de tipo MIME y tamaño de imagen.
- Cambiar a versión estable de Spring Boot.

## Licencia
Uso interno académico

---
