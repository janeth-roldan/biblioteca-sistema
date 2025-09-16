package com.pichincha.biblioteca.domain;

import com.pichincha.biblioteca.domain.enums.EstadoLibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "libros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Libro implements ILibro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String titulo;

  @Column(nullable = false, length = 255)
  private String autor;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoLibro tipo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FormatoLibro formato;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private EstadoLibro estado = EstadoLibro.DISPONIBLE;

  @Column(name = "fecha_creacion")
  @Builder.Default
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;

  @Column(length = 1000)
  private String descripcion;

  @Column(name = "numero_paginas")
  private Integer numeroPaginas;

  @Column(length = 20)
  private String isbn;

  @PreUpdate
  public void preUpdate() {
    this.fechaActualizacion = LocalDateTime.now();
  }

  @Override
  public String getInfo() {
    return "Libro{id=%d, titulo='%s', autor='%s', tipo=%s, formato=%s, estado=%s}".formatted(
        id, titulo, autor, tipo.getDescripcion(), formato.getDescripcion(), estado.getDescripcion());
  }

  public String getInfoDetallada() {
    var sb = new StringBuilder();
    sb.append("=== INFORMACIÓN DEL LIBRO ===\n");
    sb.append("ID: %s\n".formatted(id != null ? id : "No asignado"));
    sb.append("Título: %s\n".formatted(titulo));
    sb.append("Autor: %s\n".formatted(autor));
    sb.append("Tipo: %s\n".formatted(tipo.getInfoCompleta()));
    sb.append("Formato: %s\n".formatted(formato.getVentajas()));
    sb.append("Estado: %s - %s\n".formatted(estado.getDescripcion(), estado.getDetalle()));
    
    if (numeroPaginas != null) {
      sb.append("Páginas: %d\n".formatted(numeroPaginas));
    }
    
    if (isbn != null && !isbn.isBlank()) {
      sb.append("ISBN: %s\n".formatted(isbn));
    }
    
    if (descripcion != null && !descripcion.isBlank()) {
      sb.append("Descripción: %s\n".formatted(descripcion));
    }
    
    sb.append("Creado: %s\n".formatted(fechaCreacion));
    
    if (fechaActualizacion != null) {
      sb.append("Actualizado: %s\n".formatted(fechaActualizacion));
    }
    
    return sb.toString();
  }

  public boolean puedeSerPrestado() {
    return estado.puedePrestar();
  }

  public boolean puedeSerDevuelto() {
    return estado.puedeDevolver();
  }

  public String getCategorizacion() {
    return switch (tipo) {
      case FICCION -> formato.esDigital() ? 
          "Literatura Digital" : "Literatura Física";
      case NO_FICCION -> formato.esDigital() ? 
          "Conocimiento Digital" : "Conocimiento Físico";
    };
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Libro libro = (Libro) obj;
    return Objects.equals(id, libro.id) &&
           Objects.equals(titulo, libro.titulo) &&
           Objects.equals(autor, libro.autor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, titulo, autor);
  }

  @Override
  public String toString() {
    return getInfo();
  }
}