package com.pichincha.biblioteca.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Record que representa la información de un préstamo de libro.
 * Utiliza las características modernas de Java 21.
 */
public record InfoPrestamo(
    Long libroId,
    String tituloLibro,
    String usuario,
    LocalDate fechaPrestamo,
    LocalDate fechaDevolucion,
    boolean activo
) {

  public InfoPrestamo {
    if (libroId == null || libroId <= 0) {
      throw new IllegalArgumentException("El ID del libro debe ser válido");
    }
    if (tituloLibro == null || tituloLibro.isBlank()) {
      throw new IllegalArgumentException("El título del libro no puede estar vacío");
    }
    if (usuario == null || usuario.isBlank()) {
      throw new IllegalArgumentException("El usuario no puede estar vacío");
    }
    if (fechaPrestamo == null) {
      throw new IllegalArgumentException("La fecha de préstamo no puede ser null");
    }
    if (fechaDevolucion == null) {
      throw new IllegalArgumentException("La fecha de devolución no puede ser null");
    }
    if (fechaDevolucion.isBefore(fechaPrestamo)) {
      throw new IllegalArgumentException("La fecha de devolución debe ser posterior a la fecha de préstamo");
    }
  }

  public static InfoPrestamo crear(Long libroId, String tituloLibro, String usuario, int diasPrestamo) {
    LocalDate fechaPrestamo = LocalDate.now();
    LocalDate fechaDevolucion = fechaPrestamo.plusDays(diasPrestamo);
    return new InfoPrestamo(libroId, tituloLibro, usuario, fechaPrestamo, fechaDevolucion, true);
  }

  public long diasRestantes() {
    return ChronoUnit.DAYS.between(LocalDate.now(), fechaDevolucion);
  }

  public boolean estaVencido() {
    return LocalDate.now().isAfter(fechaDevolucion);
  }

  public String getEstadoPrestamo() {
    if (!activo) {
      return "Devuelto";
    }
    
    long diasRestantes = diasRestantes();
    
    return switch ((int) Math.signum(diasRestantes)) {
      case 1 -> "Vigente (%d días restantes)".formatted(diasRestantes);
      case 0 -> "Vence hoy";
      case -1 -> "Vencido (%d días)".formatted(Math.abs(diasRestantes));
      default -> "Estado desconocido";
    };
  }

  public InfoPrestamo marcarComoDevuelto() {
    return new InfoPrestamo(libroId, tituloLibro, usuario, fechaPrestamo, fechaDevolucion, false);
  }

  public InfoPrestamo renovar(int diasAdicionales) {
    if (!activo) {
      throw new IllegalStateException("No se puede renovar un préstamo ya devuelto");
    }
    LocalDate nuevaFechaDevolucion = fechaDevolucion.plusDays(diasAdicionales);
    return new InfoPrestamo(libroId, tituloLibro, usuario, fechaPrestamo, nuevaFechaDevolucion, activo);
  }

  @Override
  public String toString() {
    return "InfoPrestamo{libro='%s', usuario='%s', estado='%s'}".formatted(
        tituloLibro, usuario, getEstadoPrestamo());
  }
}