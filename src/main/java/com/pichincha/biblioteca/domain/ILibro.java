package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

public interface ILibro {
  Long getId();
  String getTitulo();
  String getAutor();
  TipoLibro getTipo();
  FormatoLibro getFormato();
  EstadoLibro getEstado();
  void setEstado(EstadoLibro estado);
  String getInfo();
}