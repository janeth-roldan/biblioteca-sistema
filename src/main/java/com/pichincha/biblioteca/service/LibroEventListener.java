package com.pichincha.biblioteca.service;

import java.util.EventListener;

@FunctionalInterface
public interface LibroEventListener extends EventListener {
  void onLibroEvent(LibroEvent event);

  // Métodos de conveniencia para tipos específicos de eventos
  default void onLibroPrestado(LibroEvent.LibroPrestado event) {
    onLibroEvent(event);
  }

  default void onLibroDevuelto(LibroEvent.LibroDevuelto event) {
    onLibroEvent(event);
  }

  default void onLibroAgregado(LibroEvent.LibroAgregado event) {
    onLibroEvent(event);
  }
}