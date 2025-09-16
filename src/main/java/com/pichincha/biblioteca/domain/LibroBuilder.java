package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

public class LibroBuilder {
  private Long id;
  private String titulo;
  private String autor;
  private TipoLibro tipo;
  private FormatoLibro formato;
  private EstadoLibro estado = EstadoLibro.DISPONIBLE;

  public LibroBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public LibroBuilder titulo(String titulo) {
    this.titulo = titulo;
    return this;
  }

  public LibroBuilder autor(String autor) {
    this.autor = autor;
    return this;
  }

  public LibroBuilder tipo(TipoLibro tipo) {
    this.tipo = tipo;
    return this;
  }

  public LibroBuilder formato(FormatoLibro formato) {
    this.formato = formato;
    return this;
  }

  public LibroBuilder estado(EstadoLibro estado) {
    this.estado = estado;
    return this;
  }

  public Libro build() {
    return Libro.builder()
        .id(id)
        .titulo(titulo)
        .autor(autor)
        .tipo(tipo)
        .formato(formato)
        .estado(estado)
        .build();
  }
}