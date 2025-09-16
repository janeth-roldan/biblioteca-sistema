package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.Libro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.service.LibroFactory;

public class NoFiccionFactory extends LibroFactory {

  @Override
  public ILibro crearLibro(String titulo, String autor, FormatoLibro formato) {
    return Libro.builder()
        .titulo(titulo)
        .autor(autor)
        .tipo(TipoLibro.NO_FICCION)
        .formato(formato)
        .build();
  }
}