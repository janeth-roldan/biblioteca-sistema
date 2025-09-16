package com.pichincha.biblioteca.repository;

import com.pichincha.biblioteca.domain.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
  
  List<Libro> findByTituloContainingIgnoreCase(String titulo);
  List<Libro> findByAutorContainingIgnoreCase(String autor);
  List<Libro> findByTipo(String tipo);
  List<Libro> findByFormato(String formato);
  List<Libro> findByEstado(String estado);
}