package org.greeneyed.graalvm_test.services;

import org.greeneyed.epl.librarian.services.EplCSVProcessor.LibroCSV;
import org.greeneyed.graalvm_test.model.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperService {

  @Mapping(ignore = true, target = "listaGeneros")
  @Mapping(ignore = true, target = "inCalibre")
  @Mapping(ignore = true, target = "listaAutores")
  @Mapping(ignore = true, target = "listaMagnetIds")
  @Mapping(ignore = true, target = "descartado")
  @Mapping(ignore = true, target = "listaAutoresNormalizados")
  @Mapping(ignore = true, target = "listaGenerosNormalizados")
  Libro from(LibroCSV libroCSV);
}
