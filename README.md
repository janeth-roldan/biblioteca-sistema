# Sistema de Gestión de Biblioteca con Java 21

## Descripción
Sistema avanzado de gestión de biblioteca desarrollado en **Java 21** con Spring Boot que implementa múltiples patrones de diseño y aprovecha al máximo las características modernas del lenguaje para demostrar buenas prácticas de programación y arquitectura de software.

## Características Principales
- **Java 21** con características de vanguardia
- Base de datos en memoria H2
- Implementación de 9 patrones de diseño
- Clean Architecture
- Principios SOLID
- Spring Boot 3.3.4
- Records para inmutabilidad
- Sealed Classes para control de herencia
- Pattern Matching moderno
- Text Blocks para mejor legibilidad
- Programación funcional avanzada

## Características de Java 21 Implementadas

### 1. Records
- **Clases**: `InfoPrestamo`, `EstadisticasBiblioteca`
- **Beneficios**: Inmutabilidad, menos boilerplate, validación en constructor
- **Uso**: Representación de datos de préstamos y estadísticas

### 2. Sealed Classes
- **Clases**: `BibliotecaException` y subclases
- **Beneficios**: Control estricto de herencia, mejor type safety
- **Uso**: Sistema de excepciones controlado

### 3. Pattern Matching en Switch
- **Ubicación**: Enumeraciones y métodos de análisis
- **Beneficios**: Código más expresivo y type-safe
- **Uso**: Lógica de negocio basada en tipos

### 4. Text Blocks
- **Uso**: Documentación, queries, mensajes multilínea
- **Beneficios**: Mejor legibilidad, formato preservado

### 5. String Templates (preparado para versiones futuras)
- **Implementación**: Usando `.formatted()` como alternativa
- **Beneficios**: Interpolación de strings más segura

## Patrones de Diseño Implementados

### 1. Singleton
- **Clase**: `DatabaseConfiguration`
- **Propósito**: Asegurar una única instancia de la configuración de base de datos
- **Ubicación**: `com.pichincha.biblioteca.configuration.DatabaseConfiguration`

### 2. Factory Method
- **Clases**: `LibroFactory`, `FiccionFactory`, `NoFiccionFactory`
- **Propósito**: Crear diferentes tipos de libros según su categoría
- **Ubicación**: `com.pichincha.biblioteca.service.*`

### 3. Abstract Factory
- **Clases**: `AbstractLibroFactory`, `LibroFisicoFactory`, `LibroDigitalFactory`
- **Propósito**: Crear familias de libros según su formato
- **Ubicación**: `com.pichincha.biblioteca.service.*`

### 4. Builder
- **Clase**: `LibroBuilder`
- **Propósito**: Construir objetos Libro de manera flexible y legible
- **Ubicación**: `com.pichincha.biblioteca.domain.LibroBuilder`

### 5. Strategy
- **Clases**: `SearchStrategy`, `SearchByTitle`, `SearchByAuthor`
- **Propósito**: Diferentes estrategias de búsqueda de libros
- **Ubicación**: `com.pichincha.biblioteca.service.*`

### 6. Observer
- **Clases**: `Observer`, `PrestamoObserver`
- **Propósito**: Notificar cambios en el estado de los libros
- **Ubicación**: `com.pichincha.biblioteca.service.*`

### 7. Decorator
- **Clase**: `PrestamoDecorator`
- **Propósito**: Añadir funcionalidad de préstamo sin modificar la clase Libro
- **Ubicación**: `com.pichincha.biblioteca.service.impl.PrestamoDecorator`

### 8. Chain of Responsibility
- **Clases**: `Validador`, `ValidadorTitulo`, `ValidadorAutor`
- **Propósito**: Validar datos de libros en una cadena de validaciones
- **Ubicación**: `com.pichincha.biblioteca.domain.*`

### 9. Adapter
- **Clases**: `LegacyLibro`, `LegacyLibroAdapter`
- **Propósito**: Adaptar sistemas legacy para integración con el sistema actual
- **Ubicación**: `com.pichincha.biblioteca.domain.*`

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── pichincha/
│   │           └── biblioteca/
│   │               ├── BibliotecaApplication.java
│   │               ├── configuration/
│   │               │   └── DatabaseConfiguration.java
│   │               ├── controller/
│   │               │   └── DemoController.java
│   │               ├── domain/
│   │               │   ├── enums/
│   │               │   ├── ILibro.java
│   │               │   ├── Libro.java
│   │               │   ├── LibroBuilder.java
│   │               │   ├── LegacyLibro.java
│   │               │   ├── LegacyLibroAdapter.java
│   │               │   └── Validador*.java
│   │               ├── repository/
│   │               │   └── LibroRepository.java
│   │               └── service/
│   │                   ├── *.java (interfaces)
│   │                   └── impl/
│   │                       └── *Impl.java
│   └── resources/
│       └── application.yml
└── test/
    ├── java/
    │   └── com/
    │       └── pichincha/
    │           └── biblioteca/
    │               └── BibliotecaApplicationTests.java
    └── resources/
        └── application-test.yml
```

## Funcionalidades

### Gestión de Libros
- ✅ Agregar libros con validaciones
- ✅ Buscar libros por título o autor
- ✅ Listar todos los libros
- ✅ Prestar libros con notificaciones
- ✅ Devolver libros

### Validaciones
- ✅ Título no vacío (mínimo 2 caracteres)
- ✅ Autor válido (solo letras, espacios y puntos)
- ✅ Chain of Responsibility para validaciones

### Características del Dominio
- **Libro**: id, titulo, autor, tipo, formato, estado
- **Tipos**: Ficción, No Ficción
- **Formatos**: Físico, Digital
- **Estados**: Disponible, Prestado

## Requisitos del Sistema
- **Java 21+** (OBLIGATORIO - utiliza características específicas de Java 21)
- Maven 3.9+
- Spring Boot 3.3.4
- H2 Database
- IDE compatible con Java 21 (IntelliJ IDEA 2023.2+, Eclipse 2023-09+, VS Code con Extension Pack for Java)

## Instalación de Java 21

### Windows
1. Descargar Java 21 desde [Oracle](https://www.oracle.com/java/technologies/downloads/#java21) o [OpenJDK](https://jdk.java.net/21/)
2. Instalar y configurar `JAVA_HOME`
3. Verificar: `java --version`

### Verificación de características de Java 21
```bash
# Verificar versión
java --version

# Debe mostrar algo como:
# openjdk 21.0.x 2023-xx-xx
# OpenJDK Runtime Environment (build 21.0.x+xx)
# OpenJDK 64-Bit Server VM (build 21.0.x+xx, mixed mode, sharing)
```

## Ejecución

### Verificar Java 21
```bash
# IMPORTANTE: Verificar que Java 21 esté instalado
java --version
```

### Compilar el proyecto
```bash
mvn clean compile
```

### Ejecutar tests (incluye tests de características de Java 21)
```bash
mvn test
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### Características demostradas al ejecutar
1. **Records**: `InfoPrestamo`, `EstadisticasBiblioteca`
2. **Sealed Classes**: Sistema de excepciones controlado
3. **Pattern Matching**: Análisis de tipos y enums
4. **Text Blocks**: Reportes y documentación
5. **Switch Expressions**: Lógica de negocio moderna
6. **Programación Funcional**: Streams avanzados y predicates
7. **Optional API**: Manejo seguro de valores opcionales

### Acceder a H2 Console
Una vez ejecutada la aplicación:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:biblioteca
- Usuario: sa
- Contraseña: (vacío)

## Demostración

Al ejecutar la aplicación, `DemoController` ejecuta automáticamente una demostración completa que muestra:

1. **Factory Method**: Creación de libros de ficción y no ficción
2. **Abstract Factory**: Creación de libros físicos y digitales
3. **Builder**: Construcción flexible de objetos Libro
4. **Adapter**: Integración de sistemas legacy
5. **Chain of Responsibility**: Validación de datos
6. **Strategy**: Búsquedas por título y autor
7. **Observer**: Notificaciones de préstamos y devoluciones
8. **Decorator**: Funcionalidad de préstamo añadida dinámicamente
9. **Singleton**: Configuración única de base de datos

## Principios Aplicados

### SOLID
- **S** - Single Responsibility: Cada clase tiene una responsabilidad específica
- **O** - Open/Closed: Extensible mediante patrones como Strategy y Factory
- **L** - Liskov Substitution: Interfaces y herencia respetan el principio
- **I** - Interface Segregation: Interfaces específicas y cohesivas
- **D** - Dependency Inversion: Dependencia de abstracciones, no concreciones

### Clean Code
- Nombres descriptivos y en inglés
- Métodos con máximo 20 líneas
- Máximo 100 caracteres por línea
- Uso de Java 21 modernas (switch expressions, records)
- Programación funcional
- Manejo adecuado de excepciones
- Logging estructurado

## Tecnologías Utilizadas
- **Java 21**: Lenguaje principal con características de vanguardia
  - Records para inmutabilidad y menos boilerplate
  - Sealed Classes para control estricto de herencia
  - Pattern Matching en Switch para lógica expresiva
  - Text Blocks para mejor legibilidad de strings multilínea
  - Switch Expressions modernas
  - Programación funcional avanzada con Streams
  - Optional API mejorado
- **Spring Boot 3.3.4**: Framework de aplicación compatible con Java 21
- **Spring Data JPA**: Persistencia de datos con soporte para Java 21
- **H2 Database**: Base de datos en memoria
- **Lombok**: Reducción de código boilerplate (compatible con Records)
- **JUnit 5**: Testing framework con soporte para Java 21
- **Maven 3.9+**: Gestión de dependencias con soporte para Java 21

## Estructura del Proyecto Optimizada para Java 21

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── pichincha/
│   │           └── biblioteca/
│   │               ├── BibliotecaApplication.java
│   │               ├── configuration/
│   │               │   └── DatabaseConfiguration.java (Singleton)
│   │               ├── controller/
│   │               │   └── DemoController.java (Demostración completa)
│   │               ├── domain/
│   │               │   ├── enums/ (Enums modernos con métodos avanzados)
│   │               │   ├── ILibro.java
│   │               │   ├── Libro.java (Entity mejorada)
│   │               │   ├── LibroBuilder.java (Builder pattern)
│   │               │   ├── InfoPrestamo.java (Record de Java 21)
│   │               │   ├── EstadisticasBiblioteca.java (Record avanzado)
│   │               │   ├── LegacyLibro.java
│   │               │   ├── LegacyLibroAdapter.java (Adapter pattern)
│   │               │   └── Validador*.java (Chain of Responsibility)
│   │               ├── exception/
│   │               │   ├── BibliotecaException.java (Sealed class)
│   │               │   └── *Exception.java (Subclases sealed)
│   │               ├── helper/
│   │               │   └── EstadisticasHelper.java (Programación funcional)
│   │               ├── repository/
│   │               │   └── LibroRepository.java
│   │               ├── service/
│   │               │   ├── *.java (Interfaces)
│   │               │   └── impl/
│   │               │       └── *Impl.java (Implementaciones modernas)
│   │               └── util/
│   │                   └── Java21FeaturesUtil.java (Demostración de características)
│   └── resources/
│       └── application.yml
└── test/
    ├── java/
    │   └── com/
    │       └── pichincha/
    │           └── biblioteca/
    │               └── BibliotecaApplicationTests.java (Tests de Java 21)
    └── resources/
        └── application-test.yml
```

## Autor
Sistema desarrollado siguiendo las mejores prácticas de desarrollo de software y patrones de diseño para demostrar conocimientos en arquitectura de software.