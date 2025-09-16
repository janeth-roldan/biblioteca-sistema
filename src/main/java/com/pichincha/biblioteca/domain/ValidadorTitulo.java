package com.pichincha.biblioteca.domain;

public class ValidadorTitulo extends Validador {

  @Override
  public void validar(String titulo, String autor) throws IllegalArgumentException {
    if (titulo == null || titulo.trim().isEmpty()) {
      throw new IllegalArgumentException("El título no puede estar vacío");
    }
    if (titulo.length() < 2) {
      throw new IllegalArgumentException("El título debe tener al menos 2 caracteres");
    }
    validarSiguiente(titulo, autor);
  }
}