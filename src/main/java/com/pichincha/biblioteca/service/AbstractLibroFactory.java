package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

public abstract class AbstractLibroFactory {
  
  public abstract ILibro crearLibroFiccion(String titulo, String autor);
  public abstract ILibro crearLibroNoFiccion(String titulo, String autor);
  
  public ILibro crearLibro(String titulo, String autor, TipoLibro tipo) {
    return switch (tipo) {
      case FICCION -> crearLibroFiccion(titulo, autor);
      case NO_FICCION -> crearLibroNoFiccion(titulo, autor);
    };
  }
}