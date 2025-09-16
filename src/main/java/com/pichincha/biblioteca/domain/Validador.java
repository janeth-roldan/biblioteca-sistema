package com.pichincha.biblioteca.domain;

public abstract class Validador {
  protected Validador siguienteValidador;

  public void setSiguienteValidador(Validador siguienteValidador) {
    this.siguienteValidador = siguienteValidador;
  }

  public abstract void validar(String titulo, String autor) throws IllegalArgumentException;

  protected void validarSiguiente(String titulo, String autor) {
    if (siguienteValidador != null) {
      siguienteValidador.validar(titulo, autor);
    }
  }
}