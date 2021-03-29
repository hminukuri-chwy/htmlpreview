package com.chewy.helper;

import com.chewy.util.Strings;
import io.micronaut.core.annotation.Introspected;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Introspected
public class DatabaseHelper {

  private static final String DB_HOSTNAME_ENV_VAR = "RDS_HOSTNAME";
  private static final String DB_PORT_ENV_VAR = "RDS_PORT";
  private static final String DB_NAME_ENV_VAR = "RDS_DB_NAME";
  private static final String DB_USER_ENV_VAR = "RDS_USERNAME";
  private static final String DB_PASSWORD_ENV_VAR = "RDS_PASSWORD";

  public static String getURL() {
    String dbHost = System.getenv(DB_HOSTNAME_ENV_VAR);
    String dbPort = System.getenv(DB_PORT_ENV_VAR);
    String dbName = System.getenv(DB_NAME_ENV_VAR);
    String dbUser = System.getenv(DB_USER_ENV_VAR);
    String dbPassword = System.getenv(DB_PASSWORD_ENV_VAR);

    validateEnvironmentConfig(dbHost, dbPort, dbName, dbUser, dbPassword);

    return "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName
        + "?user=" + dbUser
        + "&password=" + dbPassword;
  }

  private static void validateEnvironmentConfig(
      String dbHost, String dbPort, String dbName, String dbUser, String dbPassword)
      throws IllegalArgumentException {
    // assumes we will always require a pw for any user connecting to the db
    if (Strings.isBlank(dbHost) || Strings.isBlank(dbPort)
        || Strings.isBlank(dbName) || Strings.isBlank(dbUser) || Strings.isBlank(dbPassword)) {
      log.error("one or more of the environment variables " +
          "required to connect to the database has not be provided");
      throw new IllegalArgumentException(
          "one or more of the environment variables " +
              "required to connect to the database has not be provided");
    }
  }
}
