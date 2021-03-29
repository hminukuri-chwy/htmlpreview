package com.chewy.util.metrics;//package example.util.metrics;

import com.chewy.util.Strings;
import io.micronaut.core.annotation.Introspected;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Set;

/**
 * Collection of util functions specific to the Dogdog metric integration being
 * used by the notification platform.
 */
@Slf4j
@Introspected
public class MetricUtils {

  // TODO: if we have more lambda functions that have more logic to them other than api
  // layers over the db we should maybe pull this metrics util stuff out into a
  // library so we don't have to have the same function multiple places

  private static final String LOG_PREFIX = "MONITORING";
  private static final String DELIMITER = "|";
  private static final String TAG_LIST_PREFIX = "#";
  // counter is likely the most commonly used metric type so establish some default values
  // so logging can be invoked with an abbreviated method signature
  private static final MetricType DEFAULT_METRIC_TYPE = MetricType.COUNTER;
  // to support a default metric type of counter we set a default increment of 1 for the
  // default metric type
  private static final String DEFAULT_METRIC_VALUE = "1";

  // prefix used on all metrics sent to datadog from this lambda function
  // this should result in metrics with names like below:
  // notification.lamba.<metric_name>
  private static final String METRIC_PREFIX = "notification.lambda.";

  // collection of tags attached to various metrics sent to datadog from this lambda function
  public static final String METRIC_NAME_REQUESTS = "requests";
  public static final String METRIC_NAME_RESPONSES = "responses";
  public static final String METRIC_NAME_EVENTS = "events";
  public static final String METRIC_NAME_EVENTS_INVALID = "invalidEvents";
  public static final String METRIC_NAME_EVENTS_EXPIRED = "expiredEvents";
  public static final String METRIC_NAME_EXCEPTIONS = "exceptions";
  public static final String METRIC_NAME_FAILED_EMAIL = "failedEmail";
  public static final String METRIC_NAME_FAILED_PUSH = "failedPush";
  public static final String METRIC_NAME_SUCCESSFUL_EMAIL = "successfulEmail";
  public static final String METRIC_NAME_SUCCESSFUL_FAX = "successfulFax";
  public static final String METRIC_NAME_FAILED_FAX = "failedFax";

  public enum MetricType {
    CHECK("check"),
    COUNTER("count"),
    GAUGE("gauge"),
    HISTOGRAM("histogram");

    private String value;

    MetricType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  /**
   * Defines a helper function to emit custom metrics in specifically formatted log lines
   * https://docs.datadoghq.com/integrations/amazon_lambda/
   * designed to be ingested by Datadog's AWS Lambda integration.  Per the datadog documentation
   * this is the preferred method of emitting custom metrics from your AWS Lambda functions to
   * Datadog when you are not using the Python runtime.
   */
  public static void logCustomMetric(String metricName, Set<String> tags) {
    logCustomMetric(metricName, DEFAULT_METRIC_TYPE, DEFAULT_METRIC_VALUE, tags);
  }

  public static void logCustomMetric(
      String metricName, MetricType metricType, String metricValue, Set<String> tags) {
    log.info(LOG_PREFIX + DELIMITER
        + getEpochSeconds() + DELIMITER
        + metricValue + DELIMITER
        + metricType.value + DELIMITER
        + METRIC_PREFIX + metricName + DELIMITER
        + TAG_LIST_PREFIX + formatMetricTags(tags));
  }

  private static long getEpochSeconds() {
    return Instant.now().getEpochSecond();
  }

  /**
   * Returns a map of metric tags as a comma separeated string
   * @param tags map of tags to be attached to a specific metric
   * @return the tags map represented as a comma separated string
   */
  private static String formatMetricTags(Set<String> tags) {
    String tagsString = "";

    if (tags == null || tags.isEmpty()) {
      return tagsString;
    } else {
      for (String tag : tags) {
        if (Strings.isNotBlank(tagsString)) {
          tagsString += "," + tag;
        } else {
          tagsString += tag;
        }
      }
    }
    return tagsString;
  }

}
