package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.Libro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.service.AbstractLibroFactory;

public class LibroDigitalFactory extends AbstractLibroFactory {

  @Override
  public ILibro crearLibroFiccion(String titulo, String autor) {
    return Libro.builder()
        .titulo(titulo)
        .autor(autor)
        .tipo(TipoLibro.FICCION)
        .formato(FormatoLibro.DIGITAL)
        .build();
  }

  @Override
  public ILibro crearLibroNoFiccion(String titulo, String autor) {
    return Libro.builder()
        .titulo(titulo)
        .autor(autor)
        .tipo(TipoLibro.NO_FICCION)
        .formato(FormatoLibro.DIGITAL)
        .build();
  }
}