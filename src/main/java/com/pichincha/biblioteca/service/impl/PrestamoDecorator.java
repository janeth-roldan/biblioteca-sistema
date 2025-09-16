package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.service.Observer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDecorator implements ILibro {
  private final ILibro libro;
  private final List<Observer> observadores = new ArrayList<>();
  private LocalDate fechaPrestamo;
  private LocalDate fechaDevolucion;
  private String usuario;

  public PrestamoDecorator(ILibro libro) {
    this.libro = libro;
  }

  public void agregarObservador(Observer observador) {
    observadores.add(observador);
  }

  public void removerObservador(Observer observador) {
    observadores.remove(observador);
  }

  private void notificarObservadores(String evento) {
    observadores.forEach(observer -> observer.actualizar(libro, evento));
  }

  public void prestar(String usuario) {
    if (libro.getEstado() == EstadoLibro.PRESTADO) {
      throw new IllegalStateException("El libro ya está prestado");
    }
    this.usuario = usuario;
    this.fechaPrestamo = LocalDate.now();
    this.fechaDevolucion = LocalDate.now().plusDays(14);
    libro.setEstado(EstadoLibro.PRESTADO);
    notificarObservadores("PRESTADO");
  }

  public void devolver() {
    if (libro.getEstado() == EstadoLibro.DISPONIBLE) {
      throw new IllegalStateException("El libro no está prestado");
    }
    this.usuario = null;
    this.fechaPrestamo = null;
    this.fechaDevolucion = null;
    libro.setEstado(EstadoLibro.DISPONIBLE);
    notificarObservadores("DEVUELTO");
  }

  @Override
  public Long getId() {
    return libro.getId();
  }

  @Override
  public String getTitulo() {
    return libro.getTitulo();
  }

  @Override
  public String getAutor() {
    return libro.getAutor();
  }

  @Override
  public TipoLibro getTipo() {
    return libro.getTipo();
  }

  @Override
  public FormatoLibro getFormato() {
    return libro.getFormato();
  }

  @Override
  public EstadoLibro getEstado() {
    return libro.getEstado();
  }

  @Override
  public void setEstado(EstadoLibro estado) {
    libro.setEstado(estado);
  }

  @Override
  public String getInfo() {
    String info = libro.getInfo();
    if (getEstado() == EstadoLibro.PRESTADO && usuario != null) {
      info += String.format(" [Prestado a: %s, Fecha préstamo: %s, Fecha devolución: %s]", 
          usuario, fechaPrestamo, fechaDevolucion);
    }
    return info;
  }

  public String getUsuario() {
    return usuario;
  }

  public LocalDate getFechaPrestamo() {
    return fechaPrestamo;
  }

  public LocalDate getFechaDevolucion() {
    return fechaDevolucion;
  }
}