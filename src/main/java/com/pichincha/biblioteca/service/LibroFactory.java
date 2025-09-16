package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;

public abstract class LibroFactory {
  
  public abstract ILibro crearLibro(String titulo, String autor, FormatoLibro formato);
  
  public ILibro crearLibroCompleto(String titulo, String autor, FormatoLibro formato) {
    ILibro libro = crearLibro(titulo, autor, formato);
    return libro;
  }
}