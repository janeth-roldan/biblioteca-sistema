package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.LibroInfo;

import java.time.LocalDateTime;
import java.util.EventObject;

public sealed class LibroEvent extends EventObject 
    permits LibroEvent.LibroPrestado, LibroEvent.LibroDevuelto, LibroEvent.LibroAgregado {

  private final LocalDateTime timestamp;
  private final LibroInfo libroInfo;

  protected LibroEvent(Object source, LibroInfo libroInfo) {
    super(source);
    this.timestamp = LocalDateTime.now();
    this.libroInfo = libroInfo;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public LibroInfo getLibroInfo() {
    return libroInfo;
  }

  public static final class LibroPrestado extends LibroEvent {
    private final String usuario;

    public LibroPrestado(Object source, LibroInfo libroInfo, String usuario) {
      super(source, libroInfo);
      this.usuario = usuario;
    }

    public String getUsuario() {
      return usuario;
    }

    @Override
    public String toString() {
      return "📖 Libro prestado: '%s' a %s el %s"
          .formatted(getLibroInfo().titulo(), usuario, getTimestamp());
    }
  }

  public static final class LibroDevuelto extends LibroEvent {
    private final String usuario;

    public LibroDevuelto(Object source, LibroInfo libroInfo, String usuario) {
      super(source, libroInfo);
      this.usuario = usuario;
    }

    public String getUsuario() {
      return usuario;
    }

    @Override
    public String toString() {
      return "📚 Libro devuelto: '%s' por %s el %s"
          .formatted(getLibroInfo().titulo(), usuario, getTimestamp());
    }
  }

  public static final class LibroAgregado extends LibroEvent {
    public LibroAgregado(Object source, LibroInfo libroInfo) {
      super(source, libroInfo);
    }

    @Override
    public String toString() {
      return "➕ Nuevo libro agregado: '%s' de %s el %s"
          .formatted(getLibroInfo().titulo(), getLibroInfo().autor(), getTimestamp());
    }
  }
}