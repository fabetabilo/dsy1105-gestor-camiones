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

| Método | Ruta                | Descripción                          | Respuestas clave |
|--------|---------------------|--------------------------------------|------------------|
| GET    | `/api/v1/camion`    | Listar todos los camiones            | 200 / 204        |
| GET    | `/api/v1/camion/{id}` | Obtener un camión por ID            | 200 / 404        |
| POST   | `/api/v1/camion`    | Crear nuevo camión                   | 201 / 400        |
| PUT    | `/api/v1/camion/{id}` | Actualizar campos de un camión (ver nota) | 200 / 404 / 400 |
| DELETE | `/api/v1/camion/{id}` | Eliminar un camión                  | 204 / 404        |

> Nota sobre PUT: La implementación actual reasigna los mismos valores existentes (no aplica los enviados en el cuerpo). Recomendado mejorar para usar los datos del `@RequestBody`.

### Ejemplo JSON (POST)
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
El campo `imagenUri` referencia una ruta pública servida (por ejemplo, mapeada desde un directorio estático). Debes exponer el directorio `/public/camiones` si decides manejar archivos estáticos.

## Mejoras Futuras
- Corrección lógica del método PUT para aplicar datos del body.
- Manejo de validaciones (Bean Validation + mensajes claros).
- Paginación y filtros en listado.
- Gestión de subida de imágenes (multipart) en lugar de ruta manual.
- Cambiar a versión estable de Spring Boot.

## Licencia
Uso interno académico

---
