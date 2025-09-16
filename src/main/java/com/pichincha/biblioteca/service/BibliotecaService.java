package com.pichincha.biblioteca.service;

import com.pichincha.biblioteca.domain.ILibro;
import com.pichincha.biblioteca.domain.enums.FormatoLibro;
import com.pichincha.biblioteca.domain.enums.TipoLibro;
import java.util.List;

public interface BibliotecaService {
  
  ILibro agregarLibro(String titulo, String autor, TipoLibro tipo, FormatoLibro formato);
  List<ILibro> buscarPorTitulo(String titulo);
  List<ILibro> buscarPorAutor(String autor);
  List<ILibro> listarTodosLosLibros();
  void prestarLibro(Long id, String usuario);
  void devolverLibro(Long id);
  ILibro buscarPorId(Long id);
}