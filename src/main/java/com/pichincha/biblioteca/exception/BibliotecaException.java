package com.pichincha.biblioteca.exception;

/**
 * Excepción base para operaciones de biblioteca.
 * Utiliza características modernas de Java 21.
 */
public sealed class BibliotecaException extends Exception 
    permits LibroNoEncontradoException, PrestamoInvalidoException, ValidacionException {

  private final String codigoError;
  private final Object contexto;

  public BibliotecaException(String mensaje, String codigoError) {
    this(mensaje, codigoError, null, null);
  }

  public BibliotecaException(String mensaje, String codigoError, Object contexto) {
    this(mensaje, codigoError, contexto, null);
  }

  public BibliotecaException(String mensaje, String codigoError, Object contexto, Throwable causa) {
    super(mensaje, causa);
    this.codigoError = codigoError;
    this.contexto = contexto;
  }

  public String getCodigoError() {
    return codigoError;
  }

  public Object getContexto() {
    return contexto;
  }

  public String getMensajeCompleto() {
    var sb = new StringBuilder();
    sb.append("[%s] %s".formatted(codigoError, getMessage()));
    
    if (contexto != null) {
      sb.append(" - Contexto: %s".formatted(contexto));
    }
    
    if (getCause() != null) {
      sb.append(" - Causa: %s".formatted(getCause().getMessage()));
    }
    
    return sb.toString();
  }
}