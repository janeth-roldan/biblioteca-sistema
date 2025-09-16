package com.pichincha.biblioteca.controller;

import com.pichincha.biblioteca.domain.*;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.helper.EstadisticasHelper;
import com.pichincha.biblioteca.service.BibliotecaService;
import com.pichincha.biblioteca.service.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoController implements CommandLineRunner {

  private final BibliotecaService bibliotecaService;
  private final EstadisticasHelper estadisticasHelper;

  @Override
  public void run(String... args) throws Exception {
    log.info("=== SISTEMA DE GESTIÓN DE BIBLIOTECA CON JAVA 21 ===");
    log.info("Demostrando todos los patrones de diseño y características modernas");
    
    demostrarCaracteristicasJava21();
    demostrarPatronesDeDiseno();
    demostrarSistemaCompleto();
    demostrarEstadisticasAvanzadas();
  }

  private void demostrarCaracteristicasJava21() {
    log.info("\n--- CARACTERÍSTICAS DE JAVA 21 ---");
    
    // Records
    var infoPrestamo = InfoPrestamo.crear(1L, "Ejemplo", "Usuario Test", 14);
    log.info("Record InfoPrestamo: {}", infoPrestamo);
    log.info("Estado del préstamo: {}", infoPrestamo.getEstadoPrestamo());
    
    // Pattern Matching en Switch (enums)
    var ventajas = FormatoLibro.DIGITAL.getVentajas();
    log.info("Ventajas formato digital: {}", ventajas);
    
    // Text Blocks y String formatting
    var textoMultilinea = """
        Este es un ejemplo de Text Block en Java 21:
        - Mantiene el formato
        - Permite múltiples líneas
        - Es más legible
        """;
    log.info("Text Block: {}", textoMultilinea);
    
    // Sealed classes (simuladas con excepciones)
    log.info("Sealed classes implementadas en el sistema de excepciones");
  }

  private void demostrarPatronesDeDiseno() {
    log.info("\n--- PATRONES DE DISEÑO MODERNOS ---");
    
    // Factory Method con características modernas
    demostrarFactoryMethod();
    
    // Abstract Factory
    demostrarAbstractFactory();
    
    // Builder con validaciones
    demostrarBuilder();
    
    // Adapter
    demostrarAdapter();
  }

  private void demostrarFactoryMethod() {
    log.info("\n--- Factory Method ---");
    
    var ficcionFactory = new FiccionFactory();
    var noFiccionFactory = new NoFiccionFactory();
    
    var libroFiccion = ficcionFactory.crearLibro(
        "El Hobbit", "J.R.R. Tolkien", FormatoLibro.FISICO);
    var libroNoFiccion = noFiccionFactory.crearLibro(
        "Sapiens", "Yuval Noah Harari", FormatoLibro.DIGITAL);
    
    log.info("Factory - Ficción: {}", libroFiccion.getInfo());
    log.info("Factory - No Ficción: {}", libroNoFiccion.getInfo());
  }

  private void demostrarAbstractFactory() {
    log.info("\n--- Abstract Factory ---");
    
    var fisicoFactory = new LibroFisicoFactory();
    var digitalFactory = new LibroDigitalFactory();
    
    var libroFisicoFiccion = fisicoFactory.crearLibroFiccion("1984", "George Orwell");
    var libroDigitalNoFiccion = digitalFactory.crearLibroNoFiccion(
        "Thinking, Fast and Slow", "Daniel Kahneman");
    
    log.info("Abstract Factory - Físico: {}", libroFisicoFiccion.getInfo());
    log.info("Abstract Factory - Digital: {}", libroDigitalNoFiccion.getInfo());
  }

  private void demostrarBuilder() {
    log.info("\n--- Builder Pattern ---");
    
    var libro = new LibroBuilder()
        .titulo("Clean Code")
        .autor("Robert C. Martin")
        .tipo(TipoLibro.NO_FICCION)
        .formato(FormatoLibro.FISICO)
        .build();
    
    log.info("Builder: {}", libro.getInfo());
    log.info("Categorización: {}", libro.getCategorizacion());
  }

  private void demostrarAdapter() {
    log.info("\n--- Adapter Pattern ---");
    
    var legacyLibro = new LegacyLibro(
        1L, "Don Quijote", "Miguel de Cervantes", 
        "Ficción", "Físico", "Disponible");
    
    var adapter = new LegacyLibroAdapter(legacyLibro);
    
    log.info("Legacy: {}", legacyLibro.obtenerInformacion());
    log.info("Adapter: {}", adapter.getInfo());
  }

  private void demostrarSistemaCompleto() {
    log.info("\n--- SISTEMA COMPLETO ---");
    
    try {
      // Agregar libros usando validaciones modernas
      var libro1 = bibliotecaService.agregarLibro(
          "Cien años de soledad", "Gabriel García Márquez", 
          TipoLibro.FICCION, FormatoLibro.FISICO);
      
      var libro2 = bibliotecaService.agregarLibro(
          "El Arte de la Guerra", "Sun Tzu", 
          TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
      
      var libro3 = bibliotecaService.agregarLibro(
          "Fundación", "Isaac Asimov", 
          TipoLibro.FICCION, FormatoLibro.DIGITAL);
      
      // Mostrar información detallada
      log.info("Información detallada del libro 1:\n{}", libro1.getInfoDetallada());
      
      // Demostrar búsquedas con Strategy
      demostrarBusquedas();
      
      // Demostrar préstamos con Decorator y Observer
      demostrarPrestamos(libro1, libro2);
      
    } catch (Exception e) {
      log.error("Error en demostración: {}", e.getMessage());
    }
  }

  private void demostrarBusquedas() {
    log.info("\n--- Búsquedas con Strategy Pattern ---");
    
    var searchByTitle = new SearchByTitle();
    var searchByAuthor = new SearchByAuthor();
    
    var todosLosLibros = bibliotecaService.listarTodosLosLibros();
    
    var resultadosTitulo = searchByTitle.buscar(todosLosLibros, "Cien");
    resultadosTitulo.forEach(libro -> 
        log.info("Encontrado por título: {}", libro.getInfo()));
    
    var resultadosAutor = searchByAuthor.buscar(todosLosLibros, "García");
    resultadosAutor.forEach(libro -> 
        log.info("Encontrado por autor: {}", libro.getInfo()));
  }

  private void demostrarPrestamos(ILibro libro1, ILibro libro2) {
    log.info("\n--- Préstamos con Decorator + Observer ---");
    
    // Crear información de préstamo usando records
    var infoPrestamo1 = InfoPrestamo.crear(libro1.getId(), libro1.getTitulo(), "Juan Pérez", 14);
    var infoPrestamo2 = InfoPrestamo.crear(libro2.getId(), libro2.getTitulo(), "María López", 21);
    
    log.info("Info préstamo 1: {}", infoPrestamo1);
    log.info("Info préstamo 2: {}", infoPrestamo2);
    
    // Realizar préstamos
    bibliotecaService.prestarLibro(libro1.getId(), "Juan Pérez");
    bibliotecaService.prestarLibro(libro2.getId(), "María López");
    
    // Devolver un libro
    bibliotecaService.devolverLibro(libro1.getId());
  }

  private void demostrarEstadisticasAvanzadas() {
    log.info("\n--- ESTADÍSTICAS AVANZADAS CON JAVA 21 ---");
    
    var todosLosLibros = bibliotecaService.listarTodosLosLibros();
    var prestamosEjemplo = List.of(
        InfoPrestamo.crear(1L, "Libro 1", "Usuario 1", 14),
        InfoPrestamo.crear(2L, "Libro 2", "Usuario 2", 21),
        InfoPrestamo.crear(3L, "Libro 3", "Usuario 3", 7)
    );
    
    var estadisticas = estadisticasHelper.generarEstadisticas(todosLosLibros, prestamosEjemplo);
    log.info("Estadísticas:\n{}", estadisticas.generarReporte());
    
    var reporteCompleto = estadisticasHelper.generarReporteCompleto(todosLosLibros, prestamosEjemplo);
    log.info("Reporte completo:\n{}", reporteCompleto);
    
    // Demostrar características de enums mejoradas
    demostrarEnumsModernos();
  }

  private void demostrarEnumsModernos() {
    log.info("\n--- Enumeraciones Modernas ---");
    
    // TipoLibro con información completa
    log.info("Ficción - Info completa: {}", TipoLibro.FICCION.getInfoCompleta());
    log.info("No Ficción - Info completa: {}", TipoLibro.NO_FICCION.getInfoCompleta());
    
    // FormatoLibro con ventajas
    log.info("Digital - Ventajas: {}", FormatoLibro.DIGITAL.getVentajas());
    log.info("Físico - Ventajas: {}", FormatoLibro.FISICO.getVentajas());
    
    // EstadoLibro con acciones permitidas
    log.info("Disponible - Acciones: {}", EstadoLibro.DISPONIBLE.getAccionesPermitidas());
    log.info("Prestado - Acciones: {}", EstadoLibro.PRESTADO.getAccionesPermitidas());
    
    // Búsqueda por descripción
    var tipoOpcional = TipoLibro.fromDescripcion("Ficción");
    tipoOpcional.ifPresent(tipo -> 
        log.info("Tipo encontrado: {}", tipo.getInfoCompleta()));
  }
}