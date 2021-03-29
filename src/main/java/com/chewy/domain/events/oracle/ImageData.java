package com.chewy.domain.events.oracle;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Data
@Introspected
public class ImageData {
  private String data;
}