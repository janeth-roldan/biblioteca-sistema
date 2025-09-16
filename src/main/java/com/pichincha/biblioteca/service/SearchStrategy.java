package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

@FunctionalInterface
public interface SearchStrategy {
  
  Stream<ILibro> buscar(List<ILibro> libros, String criterio);

  // Estrategias predefinidas usando métodos estáticos
  static SearchStrategy porTitulo() {
    return (libros, criterio) -> libros.stream()
        .filter(libro -> libro.getTitulo().toLowerCase().contains(criterio.toLowerCase()));
  }

  static SearchStrategy porAutor() {
    return (libros, criterio) -> libros.stream()
        .filter(libro -> libro.getAutor().toLowerCase().contains(criterio.toLowerCase()));
  }

  static SearchStrategy porTituloExacto() {
    return (libros, criterio) -> libros.stream()
        .filter(libro -> libro.getTitulo().equalsIgnoreCase(criterio));
  }

  static SearchStrategy porAutorExacto() {
    return (libros, criterio) -> libros.stream()
        .filter(libro -> libro.getAutor().equalsIgnoreCase(criterio));
  }

  static SearchStrategy combinada() {
    return (libros, criterio) -> libros.stream()
        .filter(libro -> 
            libro.getTitulo().toLowerCase().contains(criterio.toLowerCase()) ||
            libro.getAutor().toLowerCase().contains(criterio.toLowerCase())
        );
  }

  static SearchStrategy conPredicate(Predicate<ILibro> filtro) {
    return (libros, criterio) -> libros.stream()
        .filter(filtro)
        .filter(libro -> 
            libro.getTitulo().toLowerCase().contains(criterio.toLowerCase()) ||
            libro.getAutor().toLowerCase().contains(criterio.toLowerCase())
        );
  }

  static SearchStrategy conComparador(BiPredicate<ILibro, String> comparador) {
    return (libros, criterio) -> libros.stream()
        .filter(libro -> comparador.test(libro, criterio));
  }

  // Método default para combinar estrategias
  default SearchStrategy and(SearchStrategy otra) {
    return (libros, criterio) -> {
      var resultado1 = this.buscar(libros, criterio).toList();
      return otra.buscar(resultado1, criterio);
    };
  }

  default SearchStrategy or(SearchStrategy otra) {
    return (libros, criterio) -> Stream.concat(
        this.buscar(libros, criterio),
        otra.buscar(libros, criterio)
    ).distinct();
  }
}