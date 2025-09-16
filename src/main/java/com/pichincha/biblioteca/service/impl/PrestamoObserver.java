package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.service.Observer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrestamoObserver implements Observer {

  @Override
  public void actualizar(ILibro libro, String evento) {
    log.info("Notificación: {} - Libro: '{}' de {}", 
        evento, libro.getTitulo(), libro.getAutor());
    
    switch (evento) {
      case "PRESTADO" -> log.info("El libro '{}' ha sido prestado y ya no está disponible", 
          libro.getTitulo());
      case "DEVUELTO" -> log.info("El libro '{}' ha sido devuelto y está disponible nuevamente", 
          libro.getTitulo());
      default -> log.info("Evento desconocido: {}", evento);
    }
  }
}