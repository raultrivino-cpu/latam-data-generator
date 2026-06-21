# LATAM Data Generator

Aplicación REST desarrollada en Java con Spring Boot para generar datos ficticios, almacenarlos en base de datos, exportarlos a un archivo CSV y permitir la consulta de ejecuciones anteriores.

Este proyecto fue desarrollado como parte de una prueba técnica orientada a generación de datos de prueba y automatización.

## Tecnologías utilizadas

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Lombok
* Datafaker
* OpenCSV
* Maven

## Arquitectura del sistema

El proyecto está organizado por responsabilidades:

```text
src/main/java/com/rtrivino/latamdatagenerator
├── controller
├── domain
├── dto
├── entity
├── enums
├── exception
├── factory
├── mapper
├── repository
├── service
└── strategy
```

### Capas principales

#### Controller

Expone los endpoints REST para generar datos, consultar ejecuciones y descargar archivos CSV.

#### Service

Contiene la lógica principal del sistema. Orquesta la generación de datos, persistencia, exportación CSV y consulta de ejecuciones.

#### Domain

Contiene el modelo de dominio usado para representar los datos generados:

* `GeneratedDataRecord`
* `NaturalPerson`
* `Company`

#### Entity

Representa las tablas de base de datos mediante JPA:

* `GenerationExecutionEntity`
* `GeneratedRecordEntity`

#### DTO

Define los objetos de respuesta expuestos por la API, evitando exponer directamente las entidades JPA.

#### Mapper

Convierte objetos de dominio a entidades y entidades a DTOs.

#### Factory

Centraliza la creación de registros generados, decidiendo si se crea una persona natural o una empresa.

#### Strategy

Implementa las reglas de generación de documentos según el tipo de registro:

* Empresa
* Menor de edad
* Mayor de edad

## Patrones de diseño aplicados

### Factory Pattern

La clase `GeneratedDataRecordFactory` centraliza la creación de registros de datos. Esta clase decide si se genera una persona natural o una empresa.

### Strategy Pattern

La generación de documentos se separó en diferentes estrategias:

* `CompanyDocumentGenerationStrategy`
* `MinorDocumentGenerationStrategy`
* `AdultDocumentGenerationStrategy`

Esto permite extender o modificar reglas de documentos sin afectar el servicio principal.

## Principios SOLID aplicados

### Single Responsibility Principle

Cada clase tiene una responsabilidad clara:

* `CsvExportService` genera archivos CSV.
* `GeneratedDataService` orquesta la generación.
* `GeneratedRecordMapper` transforma datos entre capas.
* Las estrategias generan documentos según reglas específicas.

### Open/Closed Principle

La generación de documentos puede extenderse agregando nuevas estrategias sin modificar la lógica principal.

### Dependency Injection

Los componentes principales se inyectan mediante Spring y constructor injection usando `@RequiredArgsConstructor`.

## Reglas de negocio implementadas

El sistema genera registros con los siguientes datos:

* Nombre
* Apellido
* Edad
* Documento de identificación
* Ciudad
* País
* Idioma
* Tipo de registro

Reglas implementadas:

* Las empresas tienen apellido vacío.
* Las empresas generan documentos que inician por `9`.
* Las personas menores de edad generan documentos desde `11000000`.
* Las personas mayores de edad generan documentos de más de 8 y menos de 12 dígitos.
* Los documentos no se repiten dentro de una ejecución.
* La combinación de nombre y apellido no se repite dentro de una ejecución.
* Los usuarios generados tienen edad mayor a 10 y menor a 80.
* Si el país es diferente a Colombia, el idioma no puede ser Español.
* País, ciudad e idioma se generan de forma consistente mediante el enum `Country`.

## Configuración

El archivo de configuración se encuentra en:

```text
src/main/resources/application.properties
```

Configuración principal:

```properties
spring.application.name=latam-data-generator

server.port=8080

spring.datasource.url=jdbc:h2:file:./data/latam-data-generator-db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

app.files.output-directory=output
```

## Cómo ejecutar el proyecto

Desde la raíz del proyecto ejecutar:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en:

```text
http://localhost:8080
```

## Consola H2

La consola H2 queda disponible en:

```text
http://localhost:8080/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:file:./data/latam-data-generator-db
User: sa
Password:
```

## Endpoints disponibles

### Generar datos

```http
POST /api/generated-data?quantity=10
```

Ejemplo:

```text
http://localhost:8080/api/generated-data?quantity=10
```

Respuesta esperada:

```json
{
  "executionId": "uuid",
  "recordsGenerated": 10,
  "fileName": "generated-data-uuid.csv",
  "downloadUrl": "/api/generated-data/executions/uuid/file",
  "createdAt": "2026-06-20T10:00:00",
  "records": []
}
```

### Consultar ejecuciones

```http
GET /api/generated-data/executions
```

### Consultar ejecución por ID

```http
GET /api/generated-data/executions/{executionId}
```

### Descargar archivo CSV

```http
GET /api/generated-data/executions/{executionId}/file
```

El archivo se genera físicamente en la carpeta:

```text
output/
```

## Manejo de errores

El proyecto incluye un `GlobalExceptionHandler` para manejar errores de forma controlada.

Ejemplo de respuesta de error:

```json
{
  "timestamp": "2026-06-20T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Quantity must be greater than zero"
}
```

## Flujo general del sistema

```text
Cliente REST
   ↓
GeneratedDataController
   ↓
GeneratedDataService
   ↓
GeneratedDataRecordFactory
   ↓
DocumentGenerationStrategySelector
   ↓
GeneratedRecordMapper
   ↓
GenerationExecutionRepository
   ↓
CsvExportService
   ↓
CSV generado en carpeta output
```

## Ejecución recomendada para pruebas manuales

1. Levantar la aplicación.
2. Ejecutar:

```http
POST http://localhost:8080/api/generated-data?quantity=10
```

3. Copiar el `executionId` de la respuesta.
4. Consultar la ejecución:

```http
GET http://localhost:8080/api/generated-data/executions/{executionId}
```

5. Descargar el archivo:

```http
GET http://localhost:8080/api/generated-data/executions/{executionId}/file
```

## Mejoras futuras

* Agregar pruebas unitarias y de integración.
* Agregar envío del CSV por correo.
* Permitir ejecución paralela.
* Agregar Docker.
* Integrar el servicio como fuente de datos para pruebas automatizadas con Serenity y Cucumber.
