package com.pichincha.biblioteca.domain;

public class ValidadorAutor extends Validador {

  @Override
  public void validar(String titulo, String autor) throws IllegalArgumentException {
    if (autor == null || autor.trim().isEmpty()) {
      throw new IllegalArgumentException("El autor no puede estar vacío");
    }
    if (autor.length() < 2) {
      throw new IllegalArgumentException("El autor debe tener al menos 2 caracteres");
    }
    if (!autor.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s.]+$")) {
      throw new IllegalArgumentException("El autor solo puede contener letras, espacios y puntos");
    }
    validarSiguiente(titulo, autor);
  }
}