package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

import java.util.Map;

/**
 * Record que encapsula las estadísticas de la biblioteca.
 * Utiliza características modernas de Java 21.
 */
public record EstadisticasBiblioteca(
    int totalLibros,
    Map<TipoLibro, Integer> librosPorTipo,
    Map<FormatoLibro, Integer> librosPorFormato,
    Map<EstadoLibro, Integer> librosPorEstado,
    int prestamosActivos,
    double porcentajeDisponibilidad
) {

  public EstadisticasBiblioteca {
    if (totalLibros < 0) {
      throw new IllegalArgumentException("El total de libros no puede ser negativo");
    }
    if (prestamosActivos < 0) {
      throw new IllegalArgumentException("Los préstamos activos no pueden ser negativos");
    }
    if (porcentajeDisponibilidad < 0 || porcentajeDisponibilidad > 100) {
      throw new IllegalArgumentException("El porcentaje de disponibilidad debe estar entre 0 y 100");
    }
  }

  public String generarReporte() {
    var sb = new StringBuilder();
    
    sb.append("=== ESTADÍSTICAS DE LA BIBLIOTECA ===\n");
    sb.append("Total de libros: %d\n".formatted(totalLibros));
    sb.append("Préstamos activos: %d\n".formatted(prestamosActivos));
    sb.append("Disponibilidad: %.1f%%\n\n".formatted(porcentajeDisponibilidad));
    
    sb.append("--- Distribución por Tipo ---\n");
    librosPorTipo.forEach((tipo, cantidad) -> 
        sb.append("%s: %d libros\n".formatted(tipo.getDescripcion(), cantidad)));
    
    sb.append("\n--- Distribución por Formato ---\n");
    librosPorFormato.forEach((formato, cantidad) -> 
        sb.append("%s: %d libros\n".formatted(formato.getDescripcion(), cantidad)));
    
    sb.append("\n--- Distribución por Estado ---\n");
    librosPorEstado.forEach((estado, cantidad) -> 
        sb.append("%s: %d libros\n".formatted(estado.getDescripcion(), cantidad)));
    
    return sb.toString();
  }

  public boolean necesitaMasLibros() {
    return totalLibros < 100; // Criterio arbitrario
  }

  public String getRecomendacion() {
    if (porcentajeDisponibilidad >= 80) {
      return "Excelente disponibilidad de libros";
    } else if (porcentajeDisponibilidad >= 60) {
      return "Buena disponibilidad, considerar adquirir más libros populares";
    } else if (porcentajeDisponibilidad >= 40) {
      return "Disponibilidad media, revisar política de préstamos";
    } else if (porcentajeDisponibilidad >= 20) {
      return "Baja disponibilidad, urgente adquirir más ejemplares";
    } else {
      return "Disponibilidad crítica, revisar inmediatamente el inventario";
    }
  }
}