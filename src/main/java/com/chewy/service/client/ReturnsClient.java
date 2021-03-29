//package com.chewy.service.client;
//
//import com.chewy.config.AuthConfig;
//import com.chewy.config.ReturnsClientConfig;
//import com.chewy.exception.NPCException;
//import com.chewy.helper.RestTemplateFactory;
//import com.chewy.util.ServiceConstants;
//import com.chewy.returns.model.ReturnLabelDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import javax.inject.Inject;
//
//@Component
//@Slf4j
//public class ReturnsClient extends  AbstractAuthenticatedRestClient{
//
//  private final ReturnsClientConfig returnsClientConfig;
//
//  @Inject
//  protected ReturnsClient(RestTemplateFactory restTemplateFactory, AuthConfig authConfig, ReturnsClientConfig returnsClientConfig) {
//    super(restTemplateFactory, authConfig);
//    this.returnsClientConfig = returnsClientConfig;
//  }
//
//
//  public ResponseEntity<ReturnLabelDTO[]> getReturnLablesByReturnId(String returnId) throws NPCException {
//
//    log.info("retrieving return label information for return_id={}", returnId);
//
//    UriComponents uriComponents = UriComponentsBuilder.newInstance()
//        .scheme(ServiceConstants.SCHEME)
//        .host(returnsClientConfig.getHost())
//        .path(returnsClientConfig.getLabelsPath())
//        .buildAndExpand(returnId);
//
//    HttpHeaders httpHeaders = new HttpHeaders();
//
//    return exchange(
//        uriComponents.toString(), HttpMethod.GET, httpHeaders, ReturnLabelDTO[].class);
//  }
//}
