package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

public record LibroInfo(
    Long id,
    String titulo,
    String autor,
    TipoLibro tipo,
    FormatoLibro formato,
    EstadoLibro estado
) {
  
  public LibroInfo {
    if (titulo == null || titulo.isBlank()) {
      throw new IllegalArgumentException("El título no puede estar vacío");
    }
    if (autor == null || autor.isBlank()) {
      throw new IllegalArgumentException("El autor no puede estar vacío");
    }
  }

  public static LibroInfo fromLibro(ILibro libro) {
    return new LibroInfo(
        libro.getId(),
        libro.getTitulo(),
        libro.getAutor(),
        libro.getTipo(),
        libro.getFormato(),
        libro.getEstado()
    );
  }

  public String resumenCompleto() {
    return """
        Libro: %s
        Autor: %s
        Tipo: %s
        Formato: %s
        Estado: %s
        """.formatted(titulo, autor, tipo.getDescripcion(), 
                     formato.getDescripcion(), estado.getDescripcion());
  }

  public boolean estaDisponible() {
    return estado == EstadoLibro.DISPONIBLE;
  }

  public boolean esCompatibleCon(TipoLibro tipoRequerido, FormatoLibro formatoRequerido) {
    return tipo == tipoRequerido && formato == formatoRequerido;
  }
}