package com.chewy.util;//package example.util;

import io.micronaut.core.annotation.Introspected;

import java.util.List;
import java.util.stream.Collectors;

@Introspected
public class QueryUtils {

  /**
   * Multi valued query params are passed in comma separated form.  This is a helper to transform
   * the list of query param values into in operator format.
   * @param pieces list of values for in operator
   * @return properly formatted string for use with psql in operator ('...', '.....' ...)
   */
  public static String formatInOperator(List<String> pieces) {
    return pieces.stream()
        .map(p -> "'" + p + "'")
        .collect(Collectors.joining(", ", "(", ")"));
  }

}