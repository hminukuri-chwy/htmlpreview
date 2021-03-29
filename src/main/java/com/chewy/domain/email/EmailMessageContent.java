package com.chewy.domain.email;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Introspected
public class EmailMessageContent {

  String plainTextPart;
  String htmlPart;


  //Some type of byte array maybe for PDF attachments??

}