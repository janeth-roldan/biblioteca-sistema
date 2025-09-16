package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.service.SearchStrategy;
import java.util.List;

public class SearchByTitle implements SearchStrategy {

  @Override
  public List<ILibro> buscar(List<ILibro> libros, String termino) {
    return libros.stream()
        .filter(libro -> libro.getTitulo() != null && 
                libro.getTitulo().toLowerCase().contains(termino.toLowerCase()))
        .toList();
  }
}