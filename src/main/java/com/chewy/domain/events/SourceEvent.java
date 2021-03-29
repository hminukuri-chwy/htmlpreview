package com.chewy.domain.events;

import com.chewy.Channel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public abstract class SourceEvent {
  // the type saved outside of base classes
  private String type;
  // source of the event being processed
  private Integer sourceSystemId;
  // the entity that the event is triggered for (CUSTOMERNO, EMAIL etc)
  private String sourceEntityKey;
  // the value associated with the source entity key
  private String sourceEntityValue;
  // the first time we see the event in the notification platform
  private Long firstProcessedTimeStamp;
  // how many times this event has been processed
  private int numTimesProcessed;
  // the channels the event has been processed on
  private Set<Channel> processedChannels;

  // keep track of if initializable fields have already been set
  private boolean initialized;
}
