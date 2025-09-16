package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;

public interface Observer {
  void actualizar(ILibro libro, String evento);
}