package com.pichincha.biblioteca.util;

import com.pichincha.biblioteca.domain.EstadisticasBiblioteca;
import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.InfoPrestamo;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utilidad que demuestra las características avanzadas de Java 21
 * en el contexto del sistema de biblioteca.
 */
public final class Java21FeaturesUtil {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private Java21FeaturesUtil() {
    // Utility class - no instances allowed
  }

  /**
   * Demuestra el uso de records con validaciones avanzadas
   */
  public static InfoPrestamo crearPrestamoConValidacion(Long libroId, String titulo, String usuario) {
    // Validaciones usando características modernas
    var diasPrestamo = calcularDiasPrestamo(titulo);
    return InfoPrestamo.crear(libroId, titulo, usuario, diasPrestamo);
  }

  /**
   * Utiliza pattern matching para determinar días de préstamo
   */
  private static int calcularDiasPrestamo(String titulo) {
    // Simulación de lógica de negocio usando características modernas
    return titulo.length() > 20 ? 21 : 14; // Libros largos: más tiempo
  }

  /**
   * Demuestra el uso de sealed interfaces con análisis de tipos
   */
  public static String analizarLibro(ILibro libro) {
    var analisis = new StringBuilder();
    
    // Pattern matching con enums
    var categoria = switch (libro.getTipo()) {
      case FICCION -> "Literatura Creativa";
      case NO_FICCION -> "Conocimiento Factual";
    };
    
    var accesibilidad = switch (libro.getFormato()) {
      case FISICO -> "Experiencia táctil";
      case DIGITAL -> "Acceso ubicuo";
    };
    
    var disponibilidad = switch (libro.getEstado()) {
      case DISPONIBLE -> "Listo para préstamo";
      case PRESTADO -> "En uso por otro usuario";
      case RESERVADO -> "Apartado para usuario específico";
      case MANTENIMIENTO -> "En proceso de restauración";
      case PERDIDO -> "Requiere reposición";
    };
    
    analisis.append("=== ANÁLISIS AVANZADO DEL LIBRO ===\n");
    analisis.append("Título: %s\n".formatted(libro.getTitulo()));
    analisis.append("Categoría: %s\n".formatted(categoria));
    analisis.append("Accesibilidad: %s\n".formatted(accesibilidad));
    analisis.append("Disponibilidad: %s\n".formatted(disponibilidad));
    
    return analisis.toString();
  }

  /**
   * Demuestra programación funcional avanzada con streams
   */
  public static Map<TipoLibro, List<ILibro>> clasificarLibrosPorTipo(List<ILibro> libros) {
    return libros.stream()
        .filter(libro -> libro.getEstado() != EstadoLibro.PERDIDO)
        .collect(Collectors.groupingBy(ILibro::getTipo));
  }

  /**
   * Utiliza predicates funcionales para filtros complejos
   */
  public static List<ILibro> filtrarLibrosAvanzado(List<ILibro> libros, 
                                                  TipoLibro tipo, 
                                                  FormatoLibro formato, 
                                                  boolean soloDisponibles) {
    Predicate<ILibro> filtroTipo = libro -> libro.getTipo() == tipo;
    Predicate<ILibro> filtroFormato = libro -> libro.getFormato() == formato;
    Predicate<ILibro> filtroDisponible = libro -> libro.getEstado() == EstadoLibro.DISPONIBLE;
    
    var filtroFinal = filtroTipo.and(filtroFormato);
    if (soloDisponibles) {
      filtroFinal = filtroFinal.and(filtroDisponible);
    }
    
    return libros.stream()
        .filter(filtroFinal)
        .toList();
  }

  /**
   * Demuestra el uso de text blocks para generar reportes
   */
  public static String generarReporteConTextBlocks(EstadisticasBiblioteca estadisticas) {
    var reporte = """
        ╔══════════════════════════════════════╗
        ║     REPORTE DE BIBLIOTECA            ║
        ║     Sistema Avanzado Java 21         ║
        ╠══════════════════════════════════════╣
        ║ Total de libros: %-18d ║
        ║ Préstamos activos: %-16d ║
        ║ Disponibilidad: %-19.1f%% ║
        ╚══════════════════════════════════════╝
        """.formatted(
            estadisticas.totalLibros(),
            estadisticas.prestamosActivos(),
            estadisticas.porcentajeDisponibilidad()
        );
    
    return reporte;
  }

  /**
   * Utiliza streams paralelos para procesamiento eficiente
   */
  public static Map<String, Long> contarLibrosPorAutor(List<ILibro> libros) {
    return libros.parallelStream()
        .collect(Collectors.groupingBy(
            ILibro::getAutor,
            Collectors.counting()
        ));
  }

  /**
   * Demuestra manejo avanzado de Optional
   */
  public static String obtenerRecomendacionLibro(List<ILibro> libros, String preferenciaUsuario) {
    return libros.stream()
        .filter(libro -> libro.getTitulo().toLowerCase().contains(preferenciaUsuario.toLowerCase()))
        .filter(libro -> libro.getEstado() == EstadoLibro.DISPONIBLE)
        .findFirst()
        .map(libro -> "Te recomendamos: %s por %s".formatted(libro.getTitulo(), libro.getAutor()))
        .orElse("No encontramos libros disponibles con tu preferencia: " + preferenciaUsuario);
  }

  /**
   * Genera datos de prueba usando características modernas
   */
  public static List<InfoPrestamo> generarPrestamosDeEjemplo(List<ILibro> libros, int cantidad) {
    var usuarios = List.of("Ana García", "Carlos López", "María Rodríguez", "Juan Pérez", "Laura Martín");
    var random = ThreadLocalRandom.current();
    
    return libros.stream()
        .limit(Math.min(cantidad, libros.size()))
        .map(libro -> {
          var usuario = usuarios.get(random.nextInt(usuarios.size()));
          var diasPrestamo = random.nextInt(7, 22); // Entre 7 y 21 días
          return InfoPrestamo.crear(libro.getId(), libro.getTitulo(), usuario, diasPrestamo);
        })
        .toList();
  }

  /**
   * Utiliza switch expressions para análisis de tendencias
   */
  public static String analizarTendenciaPrestamos(List<InfoPrestamo> prestamos) {
    var prestamosActivos = prestamos.stream().filter(InfoPrestamo::activo).count();
    var totalPrestamos = (long) prestamos.size();
    
    if (totalPrestamos == 0) {
      return "Sin datos de préstamos para analizar";
    }
    
    var porcentajeActivos = (double) prestamosActivos / totalPrestamos * 100;
    
    return switch ((int) porcentajeActivos / 25) {
      case 0 -> "Tendencia muy baja: Solo %d%% de préstamos activos".formatted((int) porcentajeActivos);
      case 1 -> "Tendencia baja: %d%% de préstamos activos".formatted((int) porcentajeActivos);
      case 2 -> "Tendencia moderada: %d%% de préstamos activos".formatted((int) porcentajeActivos);
      case 3 -> "Tendencia alta: %d%% de préstamos activos".formatted((int) porcentajeActivos);
      default -> "Tendencia muy alta: %d%% de préstamos activos".formatted((int) porcentajeActivos);
    };
  }

  /**
   * Demuestra el uso de var con tipos complejos
   */
  public static String generarResumenCompleto(List<ILibro> libros, List<InfoPrestamo> prestamos) {
    var librosPorTipo = clasificarLibrosPorTipo(libros);
    var conteoAutores = contarLibrosPorAutor(libros);
    var tendencia = analizarTendenciaPrestamos(prestamos);
    
    var resumen = new StringBuilder();
    resumen.append("=== RESUMEN EJECUTIVO ===\n");
    resumen.append("Fecha: %s\n".formatted(LocalDate.now().format(FORMATTER)));
    resumen.append("Total de libros: %d\n".formatted(libros.size()));
    resumen.append("Total de autores únicos: %d\n".formatted(conteoAutores.size()));
    resumen.append("Tendencia de préstamos: %s\n".formatted(tendencia));
    
    resumen.append("\n--- Distribución por Tipo ---\n");
    librosPorTipo.forEach((tipo, lista) -> 
        resumen.append("%s: %d libros\n".formatted(tipo.getDescripcion(), lista.size())));
    
    return resumen.toString();
  }
}