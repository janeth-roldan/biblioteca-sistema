package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;

public class LegacyLibroAdapter implements ILibro {
  private final LegacyLibro legacyLibro;

  public LegacyLibroAdapter(LegacyLibro legacyLibro) {
    this.legacyLibro = legacyLibro;
  }

  @Override
  public Long getId() {
    return legacyLibro.getCodigo();
  }

  @Override
  public String getTitulo() {
    return legacyLibro.getNombre();
  }

  @Override
  public String getAutor() {
    return legacyLibro.getEscritor();
  }

  @Override
  public TipoLibro getTipo() {
    return "Ficción".equals(legacyLibro.getCategoria()) ? 
        TipoLibro.FICCION : TipoLibro.NO_FICCION;
  }

  @Override
  public FormatoLibro getFormato() {
    return "Físico".equals(legacyLibro.getTipoFormato()) ? 
        FormatoLibro.FISICO : FormatoLibro.DIGITAL;
  }

  @Override
  public EstadoLibro getEstado() {
    return "Disponible".equals(legacyLibro.getEstadoActual()) ? 
        EstadoLibro.DISPONIBLE : EstadoLibro.PRESTADO;
  }

  @Override
  public void setEstado(EstadoLibro estado) {
    legacyLibro.setEstadoActual(estado.getDescripcion());
  }

  @Override
  public String getInfo() {
    return legacyLibro.obtenerInformacion();
  }
}