package com.pichincha.biblioteca.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EstadoLibro {
  DISPONIBLE("Disponible", "El libro está disponible para préstamo", true),
  PRESTADO("Prestado", "El libro está actualmente prestado", false),
  RESERVADO("Reservado", "El libro está reservado para un usuario específico", false),
  MANTENIMIENTO("Mantenimiento", "El libro está en proceso de mantenimiento", false),
  PERDIDO("Perdido", "El libro se ha reportado como perdido", false);

  private final String descripcion;
  private final String detalle;
  private final boolean disponibleParaPrestamo;

  EstadoLibro(String descripcion, String detalle, boolean disponibleParaPrestamo) {
    this.descripcion = descripcion;
    this.detalle = detalle;
    this.disponibleParaPrestamo = disponibleParaPrestamo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getDetalle() {
    return detalle;
  }

  public boolean isDisponibleParaPrestamo() {
    return disponibleParaPrestamo;
  }

  public static Optional<EstadoLibro> fromDescripcion(String descripcion) {
    return Arrays.stream(values())
        .filter(estado -> estado.descripcion.equalsIgnoreCase(descripcion))
        .findFirst();
  }

  public boolean puedePrestar() {
    return this == DISPONIBLE;
  }

  public boolean puedeDevolver() {
    return this == PRESTADO;
  }

  public String getAccionesPermitidas() {
    return switch (this) {
      case DISPONIBLE -> "Prestar, Reservar";
      case PRESTADO -> "Devolver, Renovar";
      case RESERVADO -> "Cancelar reserva, Prestar al usuario reservado";
      case MANTENIMIENTO -> "Completar mantenimiento";
      case PERDIDO -> "Reportar como encontrado, Dar de baja";
    };
  }
}