package com.pichincha.biblioteca.util;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.LibroInfo;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class LibroUtil {

  private LibroUtil() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static final String PATRON_AUTOR_VALIDO = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s.'-]+$";
  public static final int LONGITUD_MINIMA_TITULO = 2;
  public static final int LONGITUD_MINIMA_AUTOR = 2;

  public static boolean esTituloValido(String titulo) {
    return titulo != null && !titulo.isBlank() && titulo.length() >= LONGITUD_MINIMA_TITULO;
  }

  public static boolean esAutorValido(String autor) {
    return autor != null && 
           !autor.isBlank() && 
           autor.length() >= LONGITUD_MINIMA_AUTOR &&
           autor.matches(PATRON_AUTOR_VALIDO);
  }

  public static Predicate<ILibro> porTipo(TipoLibro tipo) {
    return libro -> libro.getTipo() == tipo;
  }

  public static Predicate<ILibro> porFormato(FormatoLibro formato) {
    return libro -> libro.getFormato() == formato;
  }

  public static Predicate<ILibro> porEstado(EstadoLibro estado) {
    return libro -> libro.getEstado() == estado;
  }

  public static Predicate<ILibro> disponibles() {
    return porEstado(EstadoLibro.DISPONIBLE);
  }

  public static Map<TipoLibro, List<LibroInfo>> agruparPorTipo(List<ILibro> libros) {
    return libros.stream()
        .map(LibroInfo::fromLibro)
        .collect(Collectors.groupingBy(LibroInfo::tipo));
  }

  public static Map<FormatoLibro, Long> contarPorFormato(List<ILibro> libros) {
    return libros.stream()
        .collect(Collectors.groupingBy(
            ILibro::getFormato,
            Collectors.counting()
        ));
  }

  public static String generarReporteResumen(List<ILibro> libros) {
    int totalLibros = libros.size();
    long disponibles = libros.stream()
        .mapToLong(libro -> libro.getEstado() == EstadoLibro.DISPONIBLE ? 1 : 0)
        .sum();
    long prestados = totalLibros - disponibles;

    var porTipo = agruparPorTipo(libros);
    var porFormato = contarPorFormato(libros);

    return """
        📚 REPORTE DE BIBLIOTECA
        ========================
        Total de libros: %d
        Disponibles: %d
        Prestados: %d
        
        📖 Por tipo:
        %s
        
        💾 Por formato:
        %s
        """.formatted(
            totalLibros, disponibles, prestados,
            porTipo.entrySet().stream()
                .map(entry -> "  %s: %d".formatted(
                    entry.getKey().getDescripcion(), 
                    entry.getValue().size()))
                .collect(Collectors.joining("\n")),
            porFormato.entrySet().stream()
                .map(entry -> "  %s: %d".formatted(
                    entry.getKey().getDescripcion(), 
                    entry.getValue()))
                .collect(Collectors.joining("\n"))
        );
  }
}