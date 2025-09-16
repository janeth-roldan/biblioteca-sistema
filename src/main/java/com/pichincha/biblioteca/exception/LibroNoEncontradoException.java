package com.pichincha.biblioteca.exception;

/**
 * Excepción específica para cuando no se encuentra un libro.
 */
public final class LibroNoEncontradoException extends BibliotecaException {

  public LibroNoEncontradoException(Long libroId) {
    super("Libro no encontrado", "LIBRO_NO_ENCONTRADO", libroId);
  }

  public LibroNoEncontradoException(String criterio, Object valor) {
    super("Libro no encontrado con %s: %s".formatted(criterio, valor), 
          "LIBRO_NO_ENCONTRADO", valor);
  }
}