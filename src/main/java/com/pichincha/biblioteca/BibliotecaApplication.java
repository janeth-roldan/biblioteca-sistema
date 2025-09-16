package com.pichincha.biblioteca;

import com.pichincha.biblioteca.domain.LegacyLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.service.LibroEventListener;
import com.pichincha.biblioteca.service.SearchStrategy;
import com.pichincha.biblioteca.service.impl.ModernBibliotecaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BibliotecaApplication {

  private final ModernBibliotecaService bibliotecaService;

  public static void main(String[] args) {
    System.out.println("""
        🏛️  SISTEMA DE BIBLIOTECA MODERNO
        ================================
        Implementado con Java 17 y patrones de diseño modernos
        """);
    
    SpringApplication.run(BibliotecaApplication.class, args);
  }

  @Bean
  ApplicationRunner demo() {
    return args -> {
      // Configurar observadores de eventos
      configurarEventListeners();
      
      // Demostrar funcionalidades del sistema
      demostrarFuncionalidades();
      
      // Mostrar menú interactivo
      mostrarMenuInteractivo();
    };
  }

  private void configurarEventListeners() {
    // Listener para logging detallado
    LibroEventListener loggerListener = event -> 
        log.info("🔔 Evento de biblioteca: {}", event);
    
    // Listener para estadísticas
    LibroEventListener estadisticasListener = event -> {
      if (event instanceof com.pichincha.biblioteca.service.LibroEvent.LibroPrestado prestado) {
        log.info("📊 Préstamo registrado para usuario: {}", prestado.getUsuario());
      }
    };
    
    bibliotecaService.addEventListener(loggerListener);
    bibliotecaService.addEventListener(estadisticasListener);
  }

  private void demostrarFuncionalidades() {
    log.info("🚀 Iniciando demostración del sistema...");
    
    // 1. Agregar libros usando Factory moderno
    agregarLibrosDemo();
    
    // 2. Integrar libro legacy usando Adapter
    integrarLibroLegacyDemo();
    
    // 3. Demostrar búsquedas con Strategy pattern
    demostrarBusquedasDemo();
    
    // 4. Demostrar préstamos con Observer pattern
    demostrarPrestamosDemo();
    
    // 5. Mostrar reportes y estadísticas
    mostrarReportesDemo();
  }

  private void agregarLibrosDemo() {
    log.info("📚 === AGREGANDO LIBROS ===");
    
    // Usando el patrón Factory moderno
    bibliotecaService.agregarLibro("Don Quijote", "Miguel de Cervantes", TipoLibro.FICCION, FormatoLibro.FISICO);
    bibliotecaService.agregarLibro("Sapiens", "Yuval Noah Harari", TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
    bibliotecaService.agregarLibro("1984", "George Orwell", TipoLibro.FICCION, FormatoLibro.DIGITAL);
    bibliotecaService.agregarLibro("El Arte de la Guerra", "Sun Tzu", TipoLibro.NO_FICCION, FormatoLibro.FISICO);
  }

  private void integrarLibroLegacyDemo() {
    log.info("🔄 === INTEGRANDO LIBRO LEGACY ===");
    
    var legacyLibro = new LegacyLibro(999L, "El Principito", "Antoine de Saint-Exupéry", 
        "Ficción", "Físico", "Disponible");
    
    bibliotecaService.integrarLibroLegacy(legacyLibro);
  }

  private void demostrarBusquedasDemo() {
    log.info("🔍 === DEMOSTRANDO BÚSQUEDAS ===");
    
    // Búsqueda por título
    var librosPorTitulo = bibliotecaService.buscarLibros("Don", SearchStrategy.porTitulo());
    log.info("Libros encontrados por título 'Don': {}", librosPorTitulo.size());
    
    // Búsqueda combinada
    var librosCombinada = bibliotecaService.buscarLibros("George", SearchStrategy.combinada());
    log.info("Libros encontrados con búsqueda combinada 'George': {}", librosCombinada.size());
    
    // Búsqueda con filtros avanzados
    var librosDisponibles = bibliotecaService.obtenerLibrosDisponibles();
    log.info("Libros disponibles: {}", librosDisponibles.size());
    
    var librosFiccionFisica = bibliotecaService.obtenerLibrosPorTipoYFormato(TipoLibro.FICCION, FormatoLibro.FISICO);
    log.info("Libros de ficción física: {}", librosFiccionFisica.size());
  }

  private void demostrarPrestamosDemo() {
    log.info("📖 === DEMOSTRANDO PRÉSTAMOS ===");
    
    var librosDisponibles = bibliotecaService.obtenerLibrosDisponibles();
    if (!librosDisponibles.isEmpty()) {
      var primerLibro = librosDisponibles.get(0);
      
      // Prestar libro
      bibliotecaService.prestarLibro(primerLibro.getId(), "Juan Pérez")
          .ifPresentOrElse(
              libro -> log.info("✅ Préstamo exitoso: {}", libro.getTitulo()),
              () -> log.warn("❌ No se pudo prestar el libro")
          );
      
      // Devolver libro
      bibliotecaService.devolverLibro(primerLibro.getId(), "Juan Pérez")
          .ifPresentOrElse(
              libro -> log.info("✅ Devolución exitosa: {}", libro.getTitulo()),
              () -> log.warn("❌ No se pudo devolver el libro")
          );
    }
  }

  private void mostrarReportesDemo() {
    log.info("📊 === REPORTES Y ESTADÍSTICAS ===");
    
    // Reporte completo
    var reporte = bibliotecaService.generarReporteCompleto();
    System.out.println(reporte);
    
    // Estadísticas por tipo
    var librosPorTipo = bibliotecaService.obtenerLibrosPorTipo();
    librosPorTipo.forEach((tipo, libros) -> 
        log.info("Tipo {}: {} libros", tipo.getDescripcion(), libros.size())
    );
    
    // Estadísticas por formato
    var estadisticasFormato = bibliotecaService.obtenerEstadisticasPorFormato();
    estadisticasFormato.forEach((formato, cantidad) -> 
        log.info("Formato {}: {} libros", formato.getDescripcion(), cantidad)
    );
  }

  private void mostrarMenuInteractivo() {
    var scanner = new Scanner(System.in);
    
    while (true) {
      mostrarMenu();
      var opcion = scanner.nextLine();
      
      switch (opcion) {
        case "1" -> agregarLibroInteractivo(scanner);
        case "2" -> buscarLibroInteractivo(scanner);
        case "3" -> prestarLibroInteractivo(scanner);
        case "4" -> devolverLibroInteractivo(scanner);
        case "5" -> mostrarTodosLosLibros();
        case "6" -> mostrarReportes();
        case "0" -> {
          log.info("👋 ¡Gracias por usar el Sistema de Biblioteca!");
          return;
        }
        default -> log.warn("⚠️ Opción no válida. Intente nuevamente.");
      }
    }
  }

  private void mostrarMenu() {
    System.out.println("""
        
        📚 MENÚ PRINCIPAL
        =================
        1. Agregar libro
        2. Buscar libro
        3. Prestar libro
        4. Devolver libro
        5. Mostrar todos los libros
        6. Ver reportes
        0. Salir
        
        Seleccione una opción: """);
  }

  private void agregarLibroInteractivo(Scanner scanner) {
    try {
      System.out.print("Título: ");
      var titulo = scanner.nextLine();
      
      System.out.print("Autor: ");
      var autor = scanner.nextLine();
      
      System.out.print("Tipo (1=Ficción, 2=No Ficción): ");
      var tipo = scanner.nextLine().equals("1") ? TipoLibro.FICCION : TipoLibro.NO_FICCION;
      
      System.out.print("Formato (1=Físico, 2=Digital): ");
      var formato = scanner.nextLine().equals("1") ? FormatoLibro.FISICO : FormatoLibro.DIGITAL;
      
      var libro = bibliotecaService.agregarLibro(titulo, autor, tipo, formato);
      System.out.println("✅ Libro agregado: " + libro.getInfo());
      
    } catch (Exception e) {
      System.out.println("❌ Error: " + e.getMessage());
    }
  }

  private void buscarLibroInteractivo(Scanner scanner) {
    System.out.print("Término de búsqueda: ");
    var termino = scanner.nextLine();
    
    System.out.print("Tipo de búsqueda (1=Título, 2=Autor, 3=Combinada): ");
    var tipoBusqueda = scanner.nextLine();
    
    var estrategia = switch (tipoBusqueda) {
      case "1" -> SearchStrategy.porTitulo();
      case "2" -> SearchStrategy.porAutor();
      case "3" -> SearchStrategy.combinada();
      default -> SearchStrategy.combinada();
    };
    
    var resultados = bibliotecaService.buscarLibros(termino, estrategia);
    
    if (resultados.isEmpty()) {
      System.out.println("📭 No se encontraron libros.");
    } else {
      System.out.println("📚 Libros encontrados:");
      resultados.forEach(libro -> System.out.println("  - " + libro.getInfo()));
    }
  }

  private void prestarLibroInteractivo(Scanner scanner) {
    mostrarLibrosDisponibles();
    
    System.out.print("ID del libro a prestar: ");
    try {
      var libroId = Long.parseLong(scanner.nextLine());
      
      System.out.print("Nombre del usuario: ");
      var usuario = scanner.nextLine();
      
      bibliotecaService.prestarLibro(libroId, usuario)
          .ifPresentOrElse(
              libro -> System.out.println("✅ Libro prestado: " + libro.getTitulo()),
              () -> System.out.println("❌ No se pudo prestar el libro (no disponible o ID incorrecto)")
          );
          
    } catch (NumberFormatException e) {
      System.out.println("❌ ID no válido.");
    }
  }

  private void devolverLibroInteractivo(Scanner scanner) {
    System.out.print("ID del libro a devolver: ");
    try {
      var libroId = Long.parseLong(scanner.nextLine());
      
      System.out.print("Nombre del usuario: ");
      var usuario = scanner.nextLine();
      
      bibliotecaService.devolverLibro(libroId, usuario)
          .ifPresentOrElse(
              libro -> System.out.println("✅ Libro devuelto: " + libro.getTitulo()),
              () -> System.out.println("❌ No se pudo devolver el libro (no prestado o ID incorrecto)")
          );
          
    } catch (NumberFormatException e) {
      System.out.println("❌ ID no válido.");
    }
  }

  private void mostrarTodosLosLibros() {
    var libros = bibliotecaService.filtrarLibros(libro -> true);
    
    if (libros.isEmpty()) {
      System.out.println("📭 No hay libros en la biblioteca.");
    } else {
      System.out.println("📚 Todos los libros:");
      libros.forEach(libro -> System.out.println("  - " + libro.getInfo()));
    }
  }

  private void mostrarLibrosDisponibles() {
    var librosDisponibles = bibliotecaService.obtenerLibrosDisponibles();
    
    if (librosDisponibles.isEmpty()) {
      System.out.println("📭 No hay libros disponibles.");
    } else {
      System.out.println("📚 Libros disponibles:");
      librosDisponibles.forEach(libro -> 
          System.out.println("  ID: " + libro.getId() + " - " + libro.getInfo())
      );
    }
  }

  private void mostrarReportes() {
    System.out.println(bibliotecaService.generarReporteCompleto());
  }
}