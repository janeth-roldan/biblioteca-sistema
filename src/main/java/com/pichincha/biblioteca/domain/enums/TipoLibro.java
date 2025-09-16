package com.pichincha.biblioteca.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum TipoLibro {
  FICCION("Ficción", "Narrativa de eventos imaginarios"),
  NO_FICCION("No Ficción", "Obras basadas en hechos reales");

  private final String descripcion;
  private final String detalle;

  TipoLibro(String descripcion, String detalle) {
    this.descripcion = descripcion;
    this.detalle = detalle;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getDetalle() {
    return detalle;
  }

  public static Optional<TipoLibro> fromDescripcion(String descripcion) {
    return Arrays.stream(values())
        .filter(tipo -> tipo.descripcion.equalsIgnoreCase(descripcion))
        .findFirst();
  }

  public boolean esFiccion() {
    return this == FICCION;
  }

  public String getInfoCompleta() {
    return "%s: %s".formatted(descripcion, detalle);
  }
}