package com.chewy.dao;

import com.chewy.helper.DatabaseHelper;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.sql.*;

@Slf4j
@Singleton
@Introspected
public class MessageContactDAO {

  public boolean contactHasSeenMessageType(MessageContactSearchCriteria criteria) {
    String callableSQL = "{call notfctr.contact_has_seen_message_type(?,?,?)}";

    try (Connection conn = DriverManager.getConnection(DatabaseHelper.getURL());
         CallableStatement callableStatement = conn.prepareCall(callableSQL)) {

      callableStatement.setString(1, criteria.getSourceEntity());
      callableStatement.setString(2, criteria.getSourceEntityValue());
      callableStatement.setString(3, criteria.getMessageType());

      //register multiple output parameters to match all return values
      callableStatement.registerOutParameter(1, Types.INTEGER);
      callableStatement.execute();
      int callableReturn = callableStatement.getInt(1);

      // If there is already a value for the source_entity, source_entity_value and message_type
      if (callableReturn == 0) {
        return true;
      }
    } catch (SQLException ex) {
      log.error("contact_has_seen_message_type callable statement failed for source_entity={}, source_entity_value={} and message_type={}",
          criteria.getSourceEntity(),
          criteria.getSourceEntityValue(),
          criteria.getMessageType(),
          ex);
    }
    return false;
  }

  @Data
  public static class MessageContactSearchCriteria {
    private final String sourceEntity;
    private final String sourceEntityValue;
    private String messageType;

    public MessageContactSearchCriteria(String sourceEntity, String sourceEntityValue, String messageType) {
      this.sourceEntity = sourceEntity;
      this.sourceEntityValue = sourceEntityValue;
      this.messageType = messageType;
    }
  }
}
