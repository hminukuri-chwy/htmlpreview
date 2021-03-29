//package com.chewy.service.client;
//
//import com.chewy.authenticate.TokenCache;
//import com.chewy.config.AuthConfig;
//import com.chewy.domain.AuthResponse;
//import com.chewy.exception.NPCErrorCode;
//import com.chewy.exception.NPCException;
//import com.chewy.helper.RestTemplateFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpStatusCodeException;
//import org.springframework.web.client.RestClientResponseException;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.Instant;
//import java.util.Date;
//import java.util.Optional;
//
//import static java.lang.Integer.parseInt;
//
//@Slf4j
//public abstract class AbstractAuthenticatedRestClient {
//  public static final String BASIC = "Basic ";
//  public static final String GRANT_TYPE = "grant_type";
//  public static final String CLIENT_CREDENTIALS = "client_credentials";
//  public static final String SCOPE = "scope";
//  public static final String RESOURCE_SERVER_READ = "resource-server:/*:read";
//  public static final String BEARER = "Bearer ";
//  private final RestTemplateFactory restTemplateFactory;
//  protected final AuthConfig authConfig;
//
//  private static final String ACCESS_KEY = System.getenv("ACCESS_KEY");
//  private static final String SECRET_KEY = System.getenv("SECRET_KEY");
//  private static final long EXPONENTIAL_RETRY_INTERVAL = Long.parseLong(Optional.ofNullable(System.getenv("EXPONENTIAL_RETRY_INTERVAL")).orElse("2000"));
//  private static TokenCache tokenCache = new TokenCache();
//  private static int attemptedRetries;
//  private static int MAX_ALLOWED_RETRIES = parseInt(Optional.ofNullable(System.getenv("AUTH_RETRIES")).orElse("2"));
//
//  @Autowired
//  protected AbstractAuthenticatedRestClient(RestTemplateFactory restTemplateFactory, AuthConfig authConfig) {
//    this.restTemplateFactory = restTemplateFactory;
//    this.authConfig = authConfig;
//  }
//
//  public <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpHeaders httpHeaders,
//                                        Class<T> responseType, Object... uriVariables) throws NPCException {
//    return exchange(url, httpMethod, httpHeaders, responseType, null, uriVariables);
//  }
//
//  public <T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpHeaders httpHeaders,
//                                        Class<T> responseType,
//                                        String jsonBody, Object... uriVariables) throws NPCException {
//    HttpEntity<?> httpEntity = null;
//    attemptedRetries = 0;
//    while (MAX_ALLOWED_RETRIES >= attemptedRetries) {
//      try {
//        if (attemptedRetries > 0) {
//          long sleepTime = (long) Math.pow(2, attemptedRetries) * EXPONENTIAL_RETRY_INTERVAL;
//          log.info("delaying before making exchange call with url={} with sleep_time={} and retry_count={}", url, sleepTime, attemptedRetries);
//          Thread.sleep(sleepTime);
//        }
//
//        if (isTokenInvalid()) {
//          log.info("token is expired or missing, requesting new token");
//          refreshToken();
//        }
//
//
//        httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER + tokenCache.get());
//        if (jsonBody != null) {
//          httpEntity = new HttpEntity<>(jsonBody, httpHeaders);
//        } else {
//          httpEntity = new HttpEntity<>(httpHeaders);
//        }
//
//        return restTemplateFactory.restTemplate().exchange(url, httpMethod, httpEntity, responseType, uriVariables);
//      } catch (RestClientResponseException e) {
//        if (e instanceof HttpStatusCodeException && MAX_ALLOWED_RETRIES >= attemptedRetries) {
//          HttpStatusCodeException exception = (HttpStatusCodeException) e;
//          HttpStatus httpStatus = exception.getStatusCode();
//          if (httpStatus == HttpStatus.UNAUTHORIZED) {
//            log.info("unauthorized response refreshing token");
//            attemptedRetries++;
//          } else {
//            log.error("exception making exchange call with url={} http_status={} retry_count={}", url, httpStatus, attemptedRetries, e);
//            attemptedRetries++;
//            throw e;
//          }
//        } else {
//          log.error("exception making exchange call with url={} throwing exception", url, e);
//          throw e;
//        }
//      } catch (Exception e) {
//        log.error("exception making exchange call with url={} retry_count={}", url, attemptedRetries, e);
//        attemptedRetries++;
//      }
//    }
//    //TODO: improve logging passing service type call to abstract class?
//    log.warn("retries exhausted authenticating");
//    throw new NPCException(NPCErrorCode.SERVICE_FAILURE, "retries exhausted authenticating");
//  }
//
//  private void refreshToken() {
//    String clientCreds = ACCESS_KEY + ":" + SECRET_KEY;
//    byte[] encodedClientCreds = Base64.encodeBase64(clientCreds.getBytes());
//
//    String authHeader = BASIC + new String(encodedClientCreds);
//    HttpHeaders httpHeaders = new HttpHeaders();
//    httpHeaders.add(HttpHeaders.AUTHORIZATION, authHeader);
//    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
//
//    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//    map.add(GRANT_TYPE, CLIENT_CREDENTIALS);
//    map.add(SCOPE, RESOURCE_SERVER_READ);
//
//    HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(map, httpHeaders);
//
//    AuthResponse authResponse;
//      authResponse = restTemplateFactory.restTemplate().exchange(
//          authConfig.getUrl(), HttpMethod.POST, httpRequest, AuthResponse.class).getBody();
//
//    // Subtract off 60 seconds so the token is re-fetched ~1 minute before it expires
//    int expiresInSeconds = parseInt(authResponse.getExpires_in()) - 60;
//    tokenCache.put(authResponse.getAccess_token(), Date.from(Instant.now().plusSeconds(expiresInSeconds)));
//    log.info("refreshed token");
//  }
//
//  private boolean isTokenInvalid() {
//    return tokenCache.isCleared() || tokenCache.isExpired();
//  }
//
//}
