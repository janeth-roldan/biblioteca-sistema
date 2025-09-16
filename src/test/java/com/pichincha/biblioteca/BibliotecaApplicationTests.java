package com.pichincha.biblioteca;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.InfoPrestamo;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.helper.EstadisticasHelper;
import com.pichincha.biblioteca.service.BibliotecaService;
import com.pichincha.biblioteca.util.Java21FeaturesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BibliotecaApplicationTests {

  @Autowired
  private BibliotecaService bibliotecaService;

  @Autowired
  private EstadisticasHelper estadisticasHelper;

  @Test
  void contextLoads() {
    assertNotNull(bibliotecaService);
    assertNotNull(estadisticasHelper);
  }

  @Test
  void testAgregarLibroConJava21Features() {
    var libro = bibliotecaService.agregarLibro(
        "Test Book Java 21", "Test Author", TipoLibro.FICCION, FormatoLibro.FISICO);
    
    assertNotNull(libro);
    assertEquals("Test Book Java 21", libro.getTitulo());
    assertEquals("Test Author", libro.getAutor());
    assertEquals(TipoLibro.FICCION, libro.getTipo());
    assertEquals(FormatoLibro.FISICO, libro.getFormato());
    
    // Probar características modernas
    assertTrue(libro.puedeSerPrestado());
    assertNotNull(libro.getCategorizacion());
    assertNotNull(libro.getInfoDetallada());
  }

  @Test
  void testInfoPrestamoRecord() {
    // Test del record InfoPrestamo
    var infoPrestamo = InfoPrestamo.crear(1L, "Test Book", "Test User", 14);
    
    assertNotNull(infoPrestamo);
    assertEquals(1L, infoPrestamo.libroId());
    assertEquals("Test Book", infoPrestamo.tituloLibro());
    assertEquals("Test User", infoPrestamo.usuario());
    assertTrue(infoPrestamo.activo());
    assertEquals(LocalDate.now(), infoPrestamo.fechaPrestamo());
    assertEquals(LocalDate.now().plusDays(14), infoPrestamo.fechaDevolucion());
    
    // Test de métodos del record
    assertTrue(infoPrestamo.diasRestantes() >= 0);
    assertFalse(infoPrestamo.estaVencido());
    assertNotNull(infoPrestamo.getEstadoPrestamo());
    
    // Test de inmutabilidad
    var infoDevuelto = infoPrestamo.marcarComoDevuelto();
    assertFalse(infoDevuelto.activo());
    assertTrue(infoPrestamo.activo()); // Original no cambia
    
    // Test de renovación
    var infoRenovado = infoPrestamo.renovar(7);
    assertEquals(infoPrestamo.fechaDevolucion().plusDays(7), infoRenovado.fechaDevolucion());
  }

  @Test
  void testEnumsModernos() {
    // Test TipoLibro con características avanzadas
    var ficcion = TipoLibro.FICCION;
    assertTrue(ficcion.esFiccion());
    assertNotNull(ficcion.getInfoCompleta());
    
    var tipoOpcional = TipoLibro.fromDescripcion("Ficción");
    assertTrue(tipoOpcional.isPresent());
    assertEquals(ficcion, tipoOpcional.get());
    
    // Test FormatoLibro
    var digital = FormatoLibro.DIGITAL;
    assertTrue(digital.esDigital());
    assertFalse(digital.requiereEspacioFisico());
    assertNotNull(digital.getVentajas());
    assertFalse(digital.getFormatos().isEmpty());
  }

  @Test
  void testEstadisticasConJava21() {
    // Agregar algunos libros de prueba
    var libro1 = bibliotecaService.agregarLibro(
        "Libro Test 1", "Autor 1", TipoLibro.FICCION, FormatoLibro.FISICO);
    var libro2 = bibliotecaService.agregarLibro(
        "Libro Test 2", "Autor 2", TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
    
    var libros = bibliotecaService.listarTodosLosLibros();
    var prestamos = List.of(
        InfoPrestamo.crear(libro1.getId(), libro1.getTitulo(), "Usuario 1", 14),
        InfoPrestamo.crear(libro2.getId(), libro2.getTitulo(), "Usuario 2", 21)
    );
    
    // Test de estadísticas
    var estadisticas = estadisticasHelper.generarEstadisticas(libros, prestamos);
    assertNotNull(estadisticas);
    assertEquals(libros.size(), estadisticas.totalLibros());
    assertEquals(2, estadisticas.prestamosActivos());
    
    // Test de reporte
    var reporte = estadisticas.generarReporte();
    assertNotNull(reporte);
    assertFalse(reporte.isEmpty());
    
    // Test de recomendación
    var recomendacion = estadisticas.getRecomendacion();
    assertNotNull(recomendacion);
    assertFalse(recomendacion.isEmpty());
  }

  @Test
  void testJava21FeaturesUtil() {
    // Agregar libros para las pruebas
    var libro1 = bibliotecaService.agregarLibro(
        "Análisis Test", "Autor Prueba", TipoLibro.FICCION, FormatoLibro.DIGITAL);
    var libro2 = bibliotecaService.agregarLibro(
        "Conocimiento Test", "Autor Ciencia", TipoLibro.NO_FICCION, FormatoLibro.FISICO);
    
    var libros = List.of(libro1, libro2);
    
    // Test análisis de libro
    var analisis = Java21FeaturesUtil.analizarLibro(libro1);
    assertNotNull(analisis);
    assertTrue(analisis.contains("Literatura Creativa"));
    assertTrue(analisis.contains("Acceso ubicuo"));
    
    // Test clasificación por tipo
    var clasificacion = Java21FeaturesUtil.clasificarLibrosPorTipo(libros);
    assertNotNull(clasificacion);
    assertTrue(clasificacion.containsKey(TipoLibro.FICCION));
    assertTrue(clasificacion.containsKey(TipoLibro.NO_FICCION));
    
    // Test filtrado avanzado
    var librosDigitales = Java21FeaturesUtil.filtrarLibrosAvanzado(
        libros, TipoLibro.FICCION, FormatoLibro.DIGITAL, true);
    assertEquals(1, librosDigitales.size());
    assertEquals(libro1.getId(), librosDigitales.get(0).getId());
    
    // Test generación de préstamos de ejemplo
    var prestamosEjemplo = Java21FeaturesUtil.generarPrestamosDeEjemplo(libros, 2);
    assertEquals(2, prestamosEjemplo.size());
    
    // Test análisis de tendencias
    var tendencia = Java21FeaturesUtil.analizarTendenciaPrestamos(prestamosEjemplo);
    assertNotNull(tendencia);
    assertFalse(tendencia.isEmpty());
    
    // Test reporte con text blocks
    var estadisticas = estadisticasHelper.generarEstadisticas(libros, prestamosEjemplo);
    var reporteTextBlocks = Java21FeaturesUtil.generarReporteConTextBlocks(estadisticas);
    assertNotNull(reporteTextBlocks);
    assertTrue(reporteTextBlocks.contains("REPORTE DE BIBLIOTECA"));
    assertTrue(reporteTextBlocks.contains("Java 21"));
  }

  @Test
  void testValidacionTituloVacio() {
    assertThrows(IllegalArgumentException.class, () -> {
      bibliotecaService.agregarLibro("", "Test Author", TipoLibro.FICCION, FormatoLibro.FISICO);
    });
  }

  @Test
  void testValidacionAutorVacio() {
    assertThrows(IllegalArgumentException.class, () -> {
      bibliotecaService.agregarLibro("Test Book", "", TipoLibro.FICCION, FormatoLibro.FISICO);
    });
  }

  @Test
  void testBuscarPorTitulo() {
    bibliotecaService.agregarLibro("Unique Java21 Title", "Author", TipoLibro.FICCION, FormatoLibro.FISICO);
    
    var resultados = bibliotecaService.buscarPorTitulo("Java21");
    
    assertFalse(resultados.isEmpty());
    assertTrue(resultados.stream().anyMatch(libro -> libro.getTitulo().contains("Java21")));
  }

  @Test
  void testPrestarYDevolverLibro() {
    var libro = bibliotecaService.agregarLibro(
        "Prestable Java21 Book", "Author", TipoLibro.FICCION, FormatoLibro.FISICO);
    
    assertDoesNotThrow(() -> {
      bibliotecaService.prestarLibro(libro.getId(), "Test User");
      bibliotecaService.devolverLibro(libro.getId());
    });
  }

  @Test
  void testValidacionInfoPrestamoRecord() {
    // Test validaciones del record
    assertThrows(IllegalArgumentException.class, () -> {
      new InfoPrestamo(null, "Título", "Usuario", LocalDate.now(), LocalDate.now().plusDays(14), true);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      new InfoPrestamo(1L, "", "Usuario", LocalDate.now(), LocalDate.now().plusDays(14), true);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      new InfoPrestamo(1L, "Título", "", LocalDate.now(), LocalDate.now().plusDays(14), true);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      new InfoPrestamo(1L, "Título", "Usuario", LocalDate.now(), LocalDate.now().minusDays(1), true);
    });
  }
}