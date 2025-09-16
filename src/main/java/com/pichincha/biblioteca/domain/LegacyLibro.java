package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LegacyLibro {
  private Long codigo;
  private String nombre;
  private String escritor;
  private String categoria;
  private String tipoFormato;
  private String estadoActual;

  public String obtenerInformacion() {
    return String.format("Legacy[codigo=%d, nombre='%s', escritor='%s']", codigo, nombre, escritor);
  }
}