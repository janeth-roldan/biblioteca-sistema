package com.pichincha.biblioteca;

import com.pichincha.biblioteca.domain.LibroInfo;
import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import com.pichincha.biblioteca.service.LibroEvent;
import com.pichincha.biblioteca.service.LibroEventListener;
import com.pichincha.biblioteca.service.SearchStrategy;
import com.pichincha.biblioteca.service.impl.ModernBibliotecaService;
import com.pichincha.biblioteca.util.LibroUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("🧪 Tests del Sistema de Biblioteca Moderno")
class ModernBibliotecaTest {

  @Autowired
  private ModernBibliotecaService bibliotecaService;

  private final List<LibroEvent> eventosCapturados = new ArrayList<>();
  private final AtomicInteger contadorEventos = new AtomicInteger(0);

  @BeforeEach
  void setUp() {
    eventosCapturados.clear();
    contadorEventos.set(0);
    
    // Configurar listener para capturar eventos
    LibroEventListener testListener = event -> {
      eventosCapturados.add(event);
      contadorEventos.incrementAndGet();
    };
    
    bibliotecaService.addEventListener(testListener);
  }

  @Nested
  @DisplayName("📚 Tests de Gestión de Libros")
  class TestGestionLibros {

    @Test
    @DisplayName("Debería agregar libro correctamente usando Factory moderno")
    void deberiaAgregarLibroCorrectamente() {
      // Given
      var titulo = "Clean Code";
      var autor = "Robert C. Martin";
      var tipo = TipoLibro.NO_FICCION;
      var formato = FormatoLibro.DIGITAL;

      // When
      var libro = bibliotecaService.agregarLibro(titulo, autor, tipo, formato);

      // Then
      assertThat(libro).isNotNull();
      assertThat(libro.getTitulo()).isEqualTo(titulo);
      assertThat(libro.getAutor()).isEqualTo(autor);
      assertThat(libro.getTipo()).isEqualTo(tipo);
      assertThat(libro.getFormato()).isEqualTo(formato);
      assertThat(libro.getEstado()).isEqualTo(EstadoLibro.DISPONIBLE);
      
      // Verificar que se disparó el evento
      assertThat(contadorEventos.get()).isEqualTo(1);
      assertThat(eventosCapturados).hasSize(1);
      assertThat(eventosCapturados.get(0)).isInstanceOf(LibroEvent.LibroAgregado.class);
    }

    @Test
    @DisplayName("Debería validar datos de entrada usando Chain of Responsibility")
    void deberiaValidarDatosEntrada() {
      // Given & When & Then
      assertThatThrownBy(() -> 
          bibliotecaService.agregarLibro("", "Autor Válido", TipoLibro.FICCION, FormatoLibro.FISICO))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("título");

      assertThatThrownBy(() -> 
          bibliotecaService.agregarLibro("Título Válido", "", TipoLibro.FICCION, FormatoLibro.FISICO))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("autor");

      assertThatThrownBy(() -> 
          bibliotecaService.agregarLibro("Título Válido", "123@#$", TipoLibro.FICCION, FormatoLibro.FISICO))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("autor");
    }
  }

  @Nested
  @DisplayName("🔍 Tests de Búsqueda con Strategy Pattern")
  class TestBusquedaStrategy {

    @BeforeEach
    void agregarLibrosPrueba() {
      bibliotecaService.agregarLibro("Java: The Complete Reference", "Herbert Schildt", TipoLibro.NO_FICCION, FormatoLibro.FISICO);
      bibliotecaService.agregarLibro("Effective Java", "Joshua Bloch", TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
      bibliotecaService.agregarLibro("Spring in Action", "Craig Walls", TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
      eventosCapturados.clear(); // Limpiar eventos de setup
    }

    @Test
    @DisplayName("Debería buscar por título usando Strategy")
    void deberiaBuscarPorTitulo() {
      // When
      var resultados = bibliotecaService.buscarLibros("Java", SearchStrategy.porTitulo());

      // Then
      assertThat(resultados).hasSize(2);
      assertThat(resultados)
          .extracting("titulo")
          .allMatch(titulo -> titulo.toString().toLowerCase().contains("java"));
    }

    @Test
    @DisplayName("Debería buscar por autor usando Strategy")
    void deberiaBuscarPorAutor() {
      // When
      var resultados = bibliotecaService.buscarLibros("Bloch", SearchStrategy.porAutor());

      // Then
      assertThat(resultados).hasSize(1);
      assertThat(resultados.get(0).getAutor()).contains("Joshua Bloch");
    }

    @Test
    @DisplayName("Debería combinar estrategias de búsqueda")
    void deberiaCombinarEstrategias() {
      // When
      var resultados = bibliotecaService.buscarLibros("Spring", SearchStrategy.combinada());

      // Then
      assertThat(resultados).hasSize(1);
      assertThat(resultados.get(0).getTitulo()).contains("Spring in Action");
    }

    @Test
    @DisplayName("Debería usar estrategias compuestas")
    void deberiaUsarEstrategiasCompuestas() {
      // When
      var estrategiaCompuesta = SearchStrategy.porTitulo().or(SearchStrategy.porAutor());
      var resultados = bibliotecaService.buscarLibros("Java", estrategiaCompuesta);

      // Then
      assertThat(resultados).hasSizeGreaterThanOrEqualTo(2);
    }
  }

  @Nested
  @DisplayName("📖 Tests de Préstamos con Observer Pattern")
  class TestPrestamosObserver {

    private Long libroId;

    @BeforeEach
    void agregarLibroPrueba() {
      var libro = bibliotecaService.agregarLibro("Test Book", "Test Author", TipoLibro.FICCION, FormatoLibro.FISICO);
      libroId = libro.getId();
      eventosCapturados.clear(); // Limpiar eventos de setup
    }

    @Test
    @DisplayName("Debería prestar libro y notificar eventos")
    void deberiaPrestarLibroYNotificar() {
      // Given
      var usuario = "Juan Pérez";

      // When
      var resultado = bibliotecaService.prestarLibro(libroId, usuario);

      // Then
      assertThat(resultado).isPresent();
      assertThat(resultado.get().getEstado()).isEqualTo(EstadoLibro.PRESTADO);
      
      // Verificar evento de préstamo
      assertThat(contadorEventos.get()).isEqualTo(1);
      assertThat(eventosCapturados).hasSize(1);
      
      var evento = eventosCapturados.get(0);
      assertThat(evento).isInstanceOf(LibroEvent.LibroPrestado.class);
      
      var eventoPrestamo = (LibroEvent.LibroPrestado) evento;
      assertThat(eventoPrestamo.getUsuario()).isEqualTo(usuario);
      assertThat(eventoPrestamo.getLibroInfo().titulo()).isEqualTo("Test Book");
    }

    @Test
    @DisplayName("Debería devolver libro y notificar eventos")
    void deberiaDevol­verLibroYNotificar() {
      // Given
      var usuario = "Juan Pérez";
      bibliotecaService.prestarLibro(libroId, usuario);
      eventosCapturados.clear(); // Limpiar evento de préstamo

      // When
      var resultado = bibliotecaService.devolverLibro(libroId, usuario);

      // Then
      assertThat(resultado).isPresent();
      assertThat(resultado.get().getEstado()).isEqualTo(EstadoLibro.DISPONIBLE);
      
      // Verificar evento de devolución
      assertThat(contadorEventos.get()).isEqualTo(1);
      assertThat(eventosCapturados).hasSize(1);
      
      var evento = eventosCapturados.get(0);
      assertThat(evento).isInstanceOf(LibroEvent.LibroDevuelto.class);
      
      var eventoDevolucion = (LibroEvent.LibroDevuelto) evento;
      assertThat(eventoDevolucion.getUsuario()).isEqualTo(usuario);
    }

    @Test
    @DisplayName("No debería prestar libro ya prestado")
    void noDeberiaPrestarLibroYaPrestado() {
      // Given
      bibliotecaService.prestarLibro(libroId, "Usuario 1");

      // When
      var resultado = bibliotecaService.prestarLibro(libroId, "Usuario 2");

      // Then
      assertThat(resultado).isEmpty();
    }
  }

  @Nested
  @DisplayName("📊 Tests de Utilidades y Reportes")
  class TestUtilidadesReportes {

    @BeforeEach
    void agregarLibrosPrueba() {
      bibliotecaService.agregarLibro("Ficción Física 1", "Autor 1", TipoLibro.FICCION, FormatoLibro.FISICO);
      bibliotecaService.agregarLibro("Ficción Digital 1", "Autor 2", TipoLibro.FICCION, FormatoLibro.DIGITAL);
      bibliotecaService.agregarLibro("No Ficción Física 1", "Autor 3", TipoLibro.NO_FICCION, FormatoLibro.FISICO);
      bibliotecaService.agregarLibro("No Ficción Digital 1", "Autor 4", TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
    }

    @Test
    @DisplayName("Debería generar estadísticas por tipo")
    void deberiaGenerarEstadisticasPorTipo() {
      // When
      var estadisticas = bibliotecaService.obtenerLibrosPorTipo();

      // Then
      assertThat(estadisticas).hasSize(2);
      assertThat(estadisticas.get(TipoLibro.FICCION)).hasSize(2);
      assertThat(estadisticas.get(TipoLibro.NO_FICCION)).hasSize(2);
    }

    @Test
    @DisplayName("Debería generar estadísticas por formato")
    void deberiaGenerarEstadisticasPorFormato() {
      // When
      var estadisticas = bibliotecaService.obtenerEstadisticasPorFormato();

      // Then
      assertThat(estadisticas).hasSize(2);
      assertThat(estadisticas.get(FormatoLibro.FISICO)).isEqualTo(2L);
      assertThat(estadisticas.get(FormatoLibro.DIGITAL)).isEqualTo(2L);
    }

    @Test
    @DisplayName("Debería filtrar libros disponibles")
    void deberiaFiltrarLibrosDisponibles() {
      // Given
      var libros = bibliotecaService.obtenerLibrosDisponibles();
      var primerLibro = libros.get(0);
      bibliotecaService.prestarLibro(primerLibro.getId(), "Test User");

      // When
      var disponibles = bibliotecaService.obtenerLibrosDisponibles();

      // Then
      assertThat(disponibles).hasSize(3); // 4 - 1 prestado
      assertThat(disponibles).allMatch(libro -> libro.getEstado() == EstadoLibro.DISPONIBLE);
    }

    @Test
    @DisplayName("Debería generar reporte completo")
    void deberiaGenerarReporteCompleto() {
      // When
      var reporte = bibliotecaService.generarReporteCompleto();

      // Then
      assertThat(reporte).isNotNull();
      assertThat(reporte).contains("REPORTE DE BIBLIOTECA");
      assertThat(reporte).contains("Total de libros: 4");
      assertThat(reporte).contains("Disponibles: 4");
      assertThat(reporte).contains("Prestados: 0");
    }
  }

  @Nested
  @DisplayName("🧮 Tests de Utilidades LibroUtil")
  class TestLibroUtil {

    @Test
    @DisplayName("Debería validar títulos correctamente")
    void deberiaValidarTitulos() {
      assertThat(LibroUtil.esTituloValido("Título Válido")).isTrue();
      assertThat(LibroUtil.esTituloValido("AB")).isTrue(); // Mínimo 2 caracteres
      assertThat(LibroUtil.esTituloValido("A")).isFalse(); // Muy corto
      assertThat(LibroUtil.esTituloValido("")).isFalse(); // Vacío
      assertThat(LibroUtil.esTituloValido("   ")).isFalse(); // Solo espacios
      assertThat(LibroUtil.esTituloValido(null)).isFalse(); // Null
    }

    @Test
    @DisplayName("Debería validar autores correctamente")
    void deberiaValidarAutores() {
      assertThat(LibroUtil.esAutorValido("Gabriel García Márquez")).isTrue();
      assertThat(LibroUtil.esAutorValido("J.R.R. Tolkien")).isTrue();
      assertThat(LibroUtil.esAutorValido("José María Aznar")).isTrue();
      assertThat(LibroUtil.esAutorValido("AB")).isTrue(); // Mínimo 2 caracteres
      
      assertThat(LibroUtil.esAutorValido("A")).isFalse(); // Muy corto
      assertThat(LibroUtil.esAutorValido("")).isFalse(); // Vacío
      assertThat(LibroUtil.esAutorValido("Autor123")).isFalse(); // Con números
      assertThat(LibroUtil.esAutorValido("Autor@#$")).isFalse(); // Con símbolos
      assertThat(LibroUtil.esAutorValido(null)).isFalse(); // Null
    }

    @Test
    @DisplayName("Debería crear predicados funcionales")
    void deberiaCrearPredicadosFuncionales() {
      // Given
      var libro = bibliotecaService.agregarLibro("Test", "Author", TipoLibro.FICCION, FormatoLibro.DIGITAL);

      // When & Then
      assertThat(LibroUtil.porTipo(TipoLibro.FICCION).test(libro)).isTrue();
      assertThat(LibroUtil.porTipo(TipoLibro.NO_FICCION).test(libro)).isFalse();
      
      assertThat(LibroUtil.porFormato(FormatoLibro.DIGITAL).test(libro)).isTrue();
      assertThat(LibroUtil.porFormato(FormatoLibro.FISICO).test(libro)).isFalse();
      
      assertThat(LibroUtil.disponibles().test(libro)).isTrue();
      
      // Combinar predicados
      var predicadoCombinado = LibroUtil.porTipo(TipoLibro.FICCION).and(LibroUtil.porFormato(FormatoLibro.DIGITAL));
      assertThat(predicadoCombinado.test(libro)).isTrue();
    }
  }

  @Nested
  @DisplayName("📋 Tests de Records y Text Blocks")
  class TestRecordsTextBlocks {

    @Test
    @DisplayName("Debería crear LibroInfo correctamente con record")
    void deberiaCrearLibroInfoRecord() {
      // Given
      var libro = bibliotecaService.agregarLibro("Test Book", "Test Author", TipoLibro.FICCION, FormatoLibro.DIGITAL);

      // When
      var libroInfo = LibroInfo.fromLibro(libro);

      // Then
      assertThat(libroInfo.titulo()).isEqualTo("Test Book");
      assertThat(libroInfo.autor()).isEqualTo("Test Author");
      assertThat(libroInfo.tipo()).isEqualTo(TipoLibro.FICCION);
      assertThat(libroInfo.formato()).isEqualTo(FormatoLibro.DIGITAL);
      assertThat(libroInfo.estado()).isEqualTo(EstadoLibro.DISPONIBLE);
    }

    @Test
    @DisplayName("Debería generar resumen completo con text blocks")
    void deberiaGenerarResumenCompletoConTextBlocks() {
      // Given
      var libro = bibliotecaService.agregarLibro("Test Book", "Test Author", TipoLibro.FICCION, FormatoLibro.DIGITAL);
      var libroInfo = LibroInfo.fromLibro(libro);

      // When
      var resumen = libroInfo.resumenCompleto();

      // Then
      assertThat(resumen).contains("Libro: Test Book");
      assertThat(resumen).contains("Autor: Test Author");
      assertThat(resumen).contains("Tipo: Ficción");
      assertThat(resumen).contains("Formato: Digital");
      assertThat(resumen).contains("Estado: Disponible");
    }

    @Test
    @DisplayName("Debería validar datos en record constructor")
    void deberiaValidarDatosEnRecordConstructor() {
      assertThatThrownBy(() -> new LibroInfo(1L, "", "Author", TipoLibro.FICCION, FormatoLibro.DIGITAL, EstadoLibro.DISPONIBLE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("título");

      assertThatThrownBy(() -> new LibroInfo(1L, "Title", "", TipoLibro.FICCION, FormatoLibro.DIGITAL, EstadoLibro.DISPONIBLE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("autor");
    }
  }
}