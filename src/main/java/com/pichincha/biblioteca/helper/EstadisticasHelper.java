package com.pichincha.biblioteca.helper;

import com.pichincha.biblioteca.domain.EstadisticasBiblioteca;
import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.InfoPrestamo;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Helper para generar estadísticas y análisis de la biblioteca.
 * Utiliza programación funcional moderna de Java 21.
 */
@Component
public class EstadisticasHelper {

  public EstadisticasBiblioteca generarEstadisticas(List<ILibro> libros, 
                                                   List<InfoPrestamo> prestamos) {
    if (libros.isEmpty()) {
      return new EstadisticasBiblioteca(0, Map.of(), Map.of(), Map.of(), 0, 0.0);
    }

    var librosPorTipo = libros.stream()
        .collect(Collectors.groupingBy(
            ILibro::getTipo,
            Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
        ));

    var librosPorFormato = libros.stream()
        .collect(Collectors.groupingBy(
            ILibro::getFormato,
            Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
        ));

    var librosPorEstado = libros.stream()
        .collect(Collectors.groupingBy(
            ILibro::getEstado,
            Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
        ));

    int prestamosActivos = (int) prestamos.stream()
        .filter(InfoPrestamo::activo)
        .count();

    int librosDisponibles = librosPorEstado.getOrDefault(EstadoLibro.DISPONIBLE, 0);
    double porcentajeDisponibilidad = libros.isEmpty() ? 0.0 : 
        (double) librosDisponibles / libros.size() * 100;

    return new EstadisticasBiblioteca(
        libros.size(),
        librosPorTipo,
        librosPorFormato,
        librosPorEstado,
        prestamosActivos,
        porcentajeDisponibilidad
    );
  }

  public List<ILibro> obtenerLibrosMasPopulares(List<ILibro> libros, 
                                               List<InfoPrestamo> historialPrestamos,
                                               int limite) {
    var contadorPrestamos = historialPrestamos.stream()
        .collect(Collectors.groupingBy(
            InfoPrestamo::libroId,
            Collectors.counting()
        ));

    return libros.stream()
        .sorted((libro1, libro2) -> {
          long prestamos1 = contadorPrestamos.getOrDefault(libro1.getId(), 0L);
          long prestamos2 = contadorPrestamos.getOrDefault(libro2.getId(), 0L);
          return Long.compare(prestamos2, prestamos1); // Orden descendente
        })
        .limit(limite)
        .toList();
  }

  public Map<String, Integer> obtenerAutoresMasLeidos(List<InfoPrestamo> historialPrestamos) {
    return historialPrestamos.stream()
        .collect(Collectors.groupingBy(
            prestamo -> extractAutorFromTitle(prestamo.tituloLibro()),
            Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
  }

  private String extractAutorFromTitle(String titulo) {
    // Simulación simple - en la realidad necesitaríamos más información
    return "Autor de " + titulo;
  }

  public List<InfoPrestamo> obtenerPrestamosVencidos(List<InfoPrestamo> prestamos) {
    return prestamos.stream()
        .filter(InfoPrestamo::activo)
        .filter(InfoPrestamo::estaVencido)
        .sorted(Comparator.comparing(InfoPrestamo::fechaDevolucion))
        .toList();
  }

  public Optional<InfoPrestamo> obtenerPrestamoMasAntiguo(List<InfoPrestamo> prestamos) {
    return prestamos.stream()
        .filter(InfoPrestamo::activo)
        .min(Comparator.comparing(InfoPrestamo::fechaPrestamo));
  }

  public Map<TipoLibro, Double> calcularTiempoPromediosPrestamo(List<InfoPrestamo> historialPrestamos,
                                                               List<ILibro> libros) {
    var librosPorId = libros.stream()
        .collect(Collectors.toMap(ILibro::getId, Function.identity()));

    return historialPrestamos.stream()
        .filter(prestamo -> !prestamo.activo()) // Solo préstamos completados
        .filter(prestamo -> librosPorId.containsKey(prestamo.libroId()))
        .collect(Collectors.groupingBy(
            prestamo -> librosPorId.get(prestamo.libroId()).getTipo(),
            Collectors.averagingLong(prestamo -> 
                prestamo.fechaDevolucion().toEpochDay() - prestamo.fechaPrestamo().toEpochDay())
        ));
  }

  public String generarReporteCompleto(List<ILibro> libros, 
                                     List<InfoPrestamo> historialPrestamos) {
    var estadisticas = generarEstadisticas(libros, 
        historialPrestamos.stream().filter(InfoPrestamo::activo).toList());
    
    var librosPopulares = obtenerLibrosMasPopulares(libros, historialPrestamos, 5);
    var prestamosVencidos = obtenerPrestamosVencidos(historialPrestamos);
    var tiemposPromedio = calcularTiempoPromediosPrestamo(historialPrestamos, libros);

    var sb = new StringBuilder();
    sb.append(estadisticas.generarReporte());
    sb.append("\n=== ANÁLISIS ADICIONAL ===\n");
    
    sb.append("\n--- Top 5 Libros Más Populares ---\n");
    librosPopulares.forEach(libro -> 
        sb.append("• %s\n".formatted(libro.getInfo())));
    
    if (!prestamosVencidos.isEmpty()) {
      sb.append("\n--- Préstamos Vencidos (%d) ---\n".formatted(prestamosVencidos.size()));
      prestamosVencidos.forEach(prestamo -> 
          sb.append("• %s\n".formatted(prestamo)));
    }

    if (!tiemposPromedio.isEmpty()) {
      sb.append("\n--- Tiempo Promedio de Préstamo por Tipo ---\n");
      tiemposPromedio.forEach((tipo, promedio) -> 
          sb.append("• %s: %.1f días\n".formatted(tipo.getDescripcion(), promedio)));
    }

    sb.append("\n--- Recomendación ---\n");
    sb.append(estadisticas.getRecomendacion());

    return sb.toString();
  }
}