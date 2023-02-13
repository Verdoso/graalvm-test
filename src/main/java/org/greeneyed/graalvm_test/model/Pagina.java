package org.greeneyed.graalvm_test.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagina<T> {
  private List<T> results;
  private int total;
}