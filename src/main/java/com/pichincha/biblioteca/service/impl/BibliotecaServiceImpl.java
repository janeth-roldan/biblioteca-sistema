package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.*;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.repository.LibroRepository;
import com.pichincha.biblioteca.service.BibliotecaService;
import com.pichincha.biblioteca.service.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BibliotecaServiceImpl implements BibliotecaService {

  private final LibroRepository libroRepository;
  private final Map<Long, PrestamoDecorator> librosConPrestamo = new HashMap<>();
  private final Observer prestamoObserver = new PrestamoObserver();
  private final Validador cadenaValidacion = configurarCadenaValidacion();

  private Validador configurarCadenaValidacion() {
    ValidadorTitulo validadorTitulo = new ValidadorTitulo();
    ValidadorAutor validadorAutor = new ValidadorAutor();
    validadorTitulo.setSiguienteValidador(validadorAutor);
    return validadorTitulo;
  }

  @Override
  public ILibro agregarLibro(String titulo, String autor, TipoLibro tipo, FormatoLibro formato) {
    try {
      cadenaValidacion.validar(titulo, autor);
      
      Libro libro = new LibroBuilder()
          .titulo(titulo)
          .autor(autor)
          .tipo(tipo)
          .formato(formato)
          .build();
      
      Libro libroGuardado = libroRepository.save(libro);
      log.info("Libro agregado exitosamente: {}", libroGuardado.getInfo());
      return libroGuardado;
      
    } catch (Exception e) {
      log.error("Error al agregar libro: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public List<ILibro> buscarPorTitulo(String titulo) {
    List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);
    return libros.stream().map(libro -> (ILibro) libro).toList();
  }

  @Override
  public List<ILibro> buscarPorAutor(String autor) {
    List<Libro> libros = libroRepository.findByAutorContainingIgnoreCase(autor);
    return libros.stream().map(libro -> (ILibro) libro).toList();
  }

  @Override
  public List<ILibro> listarTodosLosLibros() {
    List<Libro> libros = libroRepository.findAll();
    return libros.stream().map(libro -> (ILibro) libro).toList();
  }

  @Override
  public void prestarLibro(Long id, String usuario) {
    try {
      Libro libro = libroRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
      
      PrestamoDecorator prestamoDecorator = new PrestamoDecorator(libro);
      prestamoDecorator.agregarObservador(prestamoObserver);
      prestamoDecorator.prestar(usuario);
      
      libroRepository.save(libro);
      librosConPrestamo.put(id, prestamoDecorator);
      
      log.info("Libro prestado exitosamente: {}", prestamoDecorator.getInfo());
      
    } catch (Exception e) {
      log.error("Error al prestar libro: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public void devolverLibro(Long id) {
    try {
      PrestamoDecorator prestamoDecorator = librosConPrestamo.get(id);
      if (prestamoDecorator == null) {
        throw new IllegalArgumentException("No se encontró préstamo para el libro con ID: " + id);
      }
      
      prestamoDecorator.devolver();
      Libro libro = libroRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
      libroRepository.save(libro);
      
      librosConPrestamo.remove(id);
      log.info("Libro devuelto exitosamente: {}", prestamoDecorator.getInfo());
      
    } catch (Exception e) {
      log.error("Error al devolver libro: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public ILibro buscarPorId(Long id) {
    return libroRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado con ID: " + id));
  }
}