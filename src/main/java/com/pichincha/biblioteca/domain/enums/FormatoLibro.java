package com.pichincha.biblioteca.domain.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public enum FormatoLibro {
  FISICO("Físico", Set.of("papel", "tapa dura", "tapa blanda"), true),
  DIGITAL("Digital", Set.of("PDF", "EPUB", "MOBI", "Kindle"), false);

  private final String descripcion;
  private final Set<String> formatos;
  private final boolean requiereEspacio;

  FormatoLibro(String descripcion, Set<String> formatos, boolean requiereEspacio) {
    this.descripcion = descripcion;
    this.formatos = formatos;
    this.requiereEspacio = requiereEspacio;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Set<String> getFormatos() {
    return formatos;
  }

  public boolean requiereEspacioFisico() {
    return requiereEspacio;
  }

  public static Optional<FormatoLibro> fromDescripcion(String descripcion) {
    return Arrays.stream(values())
        .filter(formato -> formato.descripcion.equalsIgnoreCase(descripcion))
        .findFirst();
  }

  public boolean esDigital() {
    return this == DIGITAL;
  }

  public String getVentajas() {
    return switch (this) {
      case FISICO -> "Experiencia táctil, no requiere dispositivos, coleccionable";
      case DIGITAL -> "Portabilidad, búsqueda de texto, ajuste de fuente, económico";
    };
  }
}