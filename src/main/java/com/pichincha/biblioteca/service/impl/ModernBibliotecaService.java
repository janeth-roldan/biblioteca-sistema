package com.pichincha.biblioteca.service.impl;

import com.pichincha.biblioteca.domain.*;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.repository.LibroRepository;
import com.pichincha.biblioteca.service.*;
import com.pichincha.biblioteca.util.LibroUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModernBibliotecaService {

  private final LibroRepository libroRepository;
  private final List<LibroEventListener> eventListeners = new CopyOnWriteArrayList<>();
  private final Validador cadenaValidacion = configurarCadenaValidacion();

  // Configuración de la cadena de validación
  private Validador configurarCadenaValidacion() {
    var validadorTitulo = new ValidadorTitulo();
    var validadorAutor = new ValidadorAutor();
    validadorTitulo.setSiguienteValidador(validadorAutor);
    return validadorTitulo;
  }

  // Gestión de eventos
  public void addEventListener(LibroEventListener listener) {
    eventListeners.add(listener);
  }

  public void removeEventListener(LibroEventListener listener) {
    eventListeners.remove(listener);
  }

  private void notificarEvento(LibroEvent event) {
    eventListeners.forEach(listener -> {
      try {
        listener.onLibroEvent(event);
      } catch (Exception e) {
        log.error("Error notificando evento: {}", e.getMessage(), e);
      }
    });
  }

  // Operaciones CRUD modernas
  public ILibro agregarLibro(String titulo, String autor, TipoLibro tipo, FormatoLibro formato) {
    return ejecutarConValidacion(titulo, autor, () -> {
      var libro = ModernLibroFactory.crearLibro(tipo, formato, titulo, autor);
      var libroEntity = convertirAEntity(libro);
      var libroGuardado = libroRepository.save(libroEntity);
      
      var event = new LibroEvent.LibroAgregado(this, LibroInfo.fromLibro(libroGuardado));
      notificarEvento(event);
      
      log.info("✅ Libro agregado: {}", libroGuardado.getInfo());
      return libroGuardado;
    });
  }

  public List<ILibro> buscarLibros(String criterio, SearchStrategy estrategia) {
    var todosLosLibros = libroRepository.findAll()
        .stream()
        .<ILibro>map(libro -> libro)
        .toList();
    
    return estrategia.buscar(todosLosLibros, criterio)
        .collect(Collectors.toList());
  }

  public List<ILibro> buscarConMultiplesEstrategias(String criterio, SearchStrategy... estrategias) {
    return Arrays.stream(estrategias)
        .reduce(SearchStrategy::or)
        .map(estrategia -> buscarLibros(criterio, estrategia))
        .orElse(Collections.emptyList());
  }

  public List<ILibro> filtrarLibros(Predicate<ILibro> filtro) {
    return libroRepository.findAll()
        .stream()
        .<ILibro>map(libro -> libro)
        .filter(filtro)
        .toList();
  }

  public Optional<ILibro> prestarLibro(Long libroId, String usuario) {
    return libroRepository.findById(libroId)
        .filter(libro -> libro.getEstado() == EstadoLibro.DISPONIBLE)
        .map(libro -> {
          libro.setEstado(EstadoLibro.PRESTADO);
          var libroActualizado = libroRepository.save(libro);
          
          var event = new LibroEvent.LibroPrestado(this, LibroInfo.fromLibro(libroActualizado), usuario);
          notificarEvento(event);
          
          log.info("📖 Libro prestado a {}: {}", usuario, libroActualizado.getInfo());
          return (ILibro) libroActualizado;
        });
  }

  public Optional<ILibro> devolverLibro(Long libroId, String usuario) {
    return libroRepository.findById(libroId)
        .filter(libro -> libro.getEstado() == EstadoLibro.PRESTADO)
        .map(libro -> {
          libro.setEstado(EstadoLibro.DISPONIBLE);
          var libroActualizado = libroRepository.save(libro);
          
          var event = new LibroEvent.LibroDevuelto(this, LibroInfo.fromLibro(libroActualizado), usuario);
          notificarEvento(event);
          
          log.info("📚 Libro devuelto por {}: {}", usuario, libroActualizado.getInfo());
          return (ILibro) libroActualizado;
        });
  }

  // Consultas avanzadas usando Streams y características modernas
  public Map<TipoLibro, List<LibroInfo>> obtenerLibrosPorTipo() {
    return LibroUtil.agruparPorTipo(
        libroRepository.findAll()
            .stream()
            .<ILibro>map(libro -> libro)
            .toList()
    );
  }

  public Map<FormatoLibro, Long> obtenerEstadisticasPorFormato() {
    return LibroUtil.contarPorFormato(
        libroRepository.findAll()
            .stream()
            .<ILibro>map(libro -> libro)
            .toList()
    );
  }

  public String generarReporteCompleto() {
    var todosLosLibros = libroRepository.findAll()
        .stream()
        .<ILibro>map(libro -> libro)
        .toList();
    
    return LibroUtil.generarReporteResumen(todosLosLibros);
  }

  public List<ILibro> obtenerLibrosDisponibles() {
    return filtrarLibros(LibroUtil.disponibles());
  }

  public List<ILibro> obtenerLibrosPorTipoYFormato(TipoLibro tipo, FormatoLibro formato) {
    return filtrarLibros(LibroUtil.porTipo(tipo).and(LibroUtil.porFormato(formato)));
  }

  // Métodos utilitarios
  private <T> T ejecutarConValidacion(String titulo, String autor, java.util.function.Supplier<T> operacion) {
    try {
      cadenaValidacion.validar(titulo, autor);
      return operacion.get();
    } catch (Exception e) {
      log.error("❌ Error en operación: {}", e.getMessage());
      throw new RuntimeException("Error procesando libro: " + e.getMessage(), e);
    }
  }

  private Libro convertirAEntity(ILibro libro) {
    return Libro.builder()
        .titulo(libro.getTitulo())
        .autor(libro.getAutor())
        .tipo(libro.getTipo())
        .formato(libro.getFormato())
        .estado(libro.getEstado())
        .build();
  }

  // Integración con sistema legacy usando Adapter
  public ILibro integrarLibroLegacy(LegacyLibro legacyLibro) {
    var adapter = new LegacyLibroAdapter(legacyLibro);
    var libroEntity = convertirAEntity(adapter);
    var libroGuardado = libroRepository.save(libroEntity);
    
    log.info("🔄 Libro legacy integrado: {}", libroGuardado.getInfo());
    return libroGuardado;
  }
}