package com.chewy.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.chewy.domain.events.oracle.ImageData;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor = @__(@Inject))
@Introspected
public class OracleApiClient {
  private final ObjectMapper mapper;
  private static final String FILE_ATTACHMENTS = "/fileAttachments/";
  private static final String DATA = "/data";
  private static final String ORACLE_API_INCIDENTS_URL = System.getenv("ORACLE_API_INCIDENTS_URL");
  private static final String ORACLE_API_USERNAME = System.getenv("ORACLE_API_USERNAME");
  private static final String ORACLE_API_PASSWORD = System.getenv("ORACLE_API_PASSWORD");

  public List<String> getAttachmentContent(Integer incidentId, List<Integer> attachmentIds) {
    List<String> attachmentIdImageContent = new ArrayList<>();
    for (Integer attachmentId : attachmentIds) {
      String content = getBase64ContentFromJson(getIncidentAttachmentContent(incidentId, attachmentId));
      if (null != content) {
        attachmentIdImageContent.add(content);
      }
    }
    return attachmentIdImageContent;
  }

  private String getIncidentAttachmentContent(Integer incidentId, Integer attachmentId) {
    try {
      HttpClient httpClient = HttpClientBuilder.create().build();
      HttpGet httpGet = new HttpGet(ORACLE_API_INCIDENTS_URL +
              incidentId +
              FILE_ATTACHMENTS +
              attachmentId +
              DATA
      );
      Header header = new BasicScheme(StandardCharsets.UTF_8).authenticate(
          new UsernamePasswordCredentials(ORACLE_API_USERNAME, ORACLE_API_PASSWORD),
          httpGet, null);
      httpGet.addHeader(header);

      HttpResponse httpResponse = httpClient.execute(httpGet);
      HttpEntity responseEntity = httpResponse.getEntity();

      BufferedReader in =
          new BufferedReader(new InputStreamReader(responseEntity.getContent()));
      String line;
      StringBuilder content = new StringBuilder();
      while ((line = in.readLine()) != null) {
        content.append(line);
      }
      return content.toString();
    } catch(Exception e) {
      log.error("exception downloading attachment image from oracle api", e);
    }
    return null;
  }

  private String getBase64ContentFromJson(String jsonContent) {
    if (null == jsonContent) {
      log.error("jsonContent is null, issue getting incident attachment content");
      return null;
    }
    try {
      ImageData imageData = mapper.readValue(jsonContent, ImageData.class);
      return imageData.getData();
    } catch (Exception e) {
      log.error("failed to read value from json", e);
    }
    return null;
  }
}
