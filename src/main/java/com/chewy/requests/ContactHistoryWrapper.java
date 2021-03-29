package com.chewy.requests;

import com.chewy.domain.notfctr.ContactHistoryRecordV1;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Introspected
public class ContactHistoryWrapper {
  @Builder.Default
  List<ContactHistoryRecordV1> contactRecords = null;
}