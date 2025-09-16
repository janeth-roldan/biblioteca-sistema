package com.pichincha.biblioteca.exception;

/**
 * Excepción específica para errores de validación.
 */
public final class ValidacionException extends BibliotecaException {

  public ValidacionException(String mensaje) {
    super(mensaje, "VALIDACION_ERROR");
  }

  public ValidacionException(String mensaje, Object contexto) {
    super(mensaje, "VALIDACION_ERROR", contexto);
  }
}