package com.pichincha.biblioteca.exception;

/**
 * Excepción específica para operaciones de préstamo inválidas.
 */
public final class PrestamoInvalidoException extends BibliotecaException {

  public PrestamoInvalidoException(String mensaje, Long libroId) {
    super(mensaje, "PRESTAMO_INVALIDO", libroId);
  }

  public PrestamoInvalidoException(String mensaje, Object contexto, Throwable causa) {
    super(mensaje, "PRESTAMO_INVALIDO", contexto, causa);
  }
}