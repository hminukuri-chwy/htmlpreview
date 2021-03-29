package com.chewy.domain.prefctr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

/**
 * Object holding information required to make a request to the NotificationPreference Service to
 * retrieve preference information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Introspected
public class PreferenceDetails {

  /*
  Classifier of what the preference is being looked up against
  i.e. customer number
   */
  private String idKey;
  /*
  The value of the corresponding classifier/key.
   */
  private String idValue;

}
