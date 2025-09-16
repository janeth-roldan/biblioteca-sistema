package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public final class ModernLibroFactory {

  private static final Map<String, BiFunction<String, String, ILibro>> FACTORY_CACHE = 
      new ConcurrentHashMap<>();

  private ModernLibroFactory() {
    throw new UnsupportedOperationException("Factory class");
  }

  public static ILibro crearLibro(TipoLibro tipo, FormatoLibro formato, String titulo, String autor) {
    var key = generarClave(tipo, formato);
    
    return FACTORY_CACHE.computeIfAbsent(key, k -> crearFactoryFunction(tipo, formato))
        .apply(titulo, autor);
  }

  private static String generarClave(TipoLibro tipo, FormatoLibro formato) {
    return "%s_%s".formatted(tipo.name(), formato.name());
  }

  private static BiFunction<String, String, ILibro> crearFactoryFunction(TipoLibro tipo, FormatoLibro formato) {
    return switch (tipo) {
      case FICCION -> switch (formato) {
        case FISICO -> (titulo, autor) -> crearLibroFiccionFisico(titulo, autor);
        case DIGITAL -> (titulo, autor) -> crearLibroFiccionDigital(titulo, autor);
      };
      case NO_FICCION -> switch (formato) {
        case FISICO -> (titulo, autor) -> crearLibroNoFiccionFisico(titulo, autor);
        case DIGITAL -> (titulo, autor) -> crearLibroNoFiccionDigital(titulo, autor);
      };
    };
  }

  private static ILibro crearLibroFiccionFisico(String titulo, String autor) {
    return new LibroFiccionFisico(titulo, autor);
  }

  private static ILibro crearLibroFiccionDigital(String titulo, String autor) {
    return new LibroFiccionDigital(titulo, autor);
  }

  private static ILibro crearLibroNoFiccionFisico(String titulo, String autor) {
    return new LibroNoFiccionFisico(titulo, autor);
  }

  private static ILibro crearLibroNoFiccionDigital(String titulo, String autor) {
    return new LibroNoFiccionDigital(titulo, autor);
  }

  // Clases internas que extienden el comportamiento específico
  private static class LibroFiccionFisico extends LibroBase {
    public LibroFiccionFisico(String titulo, String autor) {
      super(titulo, autor, TipoLibro.FICCION, FormatoLibro.FISICO);
    }

    @Override
    public String getInfo() {
      return super.getInfo() + " [Ficción Física - Edición de colección]";
    }
  }

  private static class LibroFiccionDigital extends LibroBase {
    public LibroFiccionDigital(String titulo, String autor) {
      super(titulo, autor, TipoLibro.FICCION, FormatoLibro.DIGITAL);
    }

    @Override
    public String getInfo() {
      return super.getInfo() + " [Ficción Digital - Con marcadores interactivos]";
    }
  }

  private static class LibroNoFiccionFisico extends LibroBase {
    public LibroNoFiccionFisico(String titulo, String autor) {
      super(titulo, autor, TipoLibro.NO_FICCION, FormatoLibro.FISICO);
    }

    @Override
    public String getInfo() {
      return super.getInfo() + " [No Ficción Física - Con referencias bibliográficas]";
    }
  }

  private static class LibroNoFiccionDigital extends LibroBase {
    public LibroNoFiccionDigital(String titulo, String autor) {
      super(titulo, autor, TipoLibro.NO_FICCION, FormatoLibro.DIGITAL);
    }

    @Override
    public String getInfo() {
      return super.getInfo() + " [No Ficción Digital - Con enlaces externos]";
    }
  }
}

// Clase base para las implementaciones específicas
abstract class LibroBase implements ILibro {
  private final String titulo;
  private final String autor;
  private final TipoLibro tipo;
  private final FormatoLibro formato;
  private EstadoLibro estado;

  protected LibroBase(String titulo, String autor, TipoLibro tipo, FormatoLibro formato) {
    this.titulo = titulo;
    this.autor = autor;
    this.tipo = tipo;
    this.formato = formato;
    this.estado = EstadoLibro.DISPONIBLE;
  }

  @Override
  public Long getId() { return null; } // Se asigna al persistir

  @Override
  public String getTitulo() { return titulo; }

  @Override
  public String getAutor() { return autor; }

  @Override
  public TipoLibro getTipo() { return tipo; }

  @Override
  public FormatoLibro getFormato() { return formato; }

  @Override
  public EstadoLibro getEstado() { return estado; }

  @Override
  public void setEstado(EstadoLibro estado) { this.estado = estado; }

  @Override
  public String getInfo() {
    return "Libro[titulo='%s', autor='%s', tipo=%s, formato=%s, estado=%s]"
        .formatted(titulo, autor, tipo.getDescripcion(), formato.getDescripcion(), estado.getDescripcion());
  }
}