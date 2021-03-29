//package com.chewy.service.client;
//
//
//import com.chewy.exception.NPCErrorCode;
//import com.chewy.exception.NPCException;
//import com.chewy.util.metrics.MetricUtils;
//import com.chewy.returns.model.ReturnLabelDTO;
//import com.google.common.collect.ImmutableSet;
//import com.chewy.util.metrics.MetricsTags;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClientException;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//
//
//@Component
//@Slf4j
//public class ReturnsClientHelper {
//
//  private final ReturnsClient returnsClient;
//
//  @Autowired
//  public ReturnsClientHelper(ReturnsClient returnsClient) {
//    this.returnsClient = returnsClient;
//  }
//
//  public List<ReturnLabelDTO> retrieveReturnLabels(String returnId) throws NPCException {
//
//    try {
//      ResponseEntity<ReturnLabelDTO[]> response = returnsClient.getReturnLablesByReturnId(returnId);
//
//      if (response.getStatusCode() == HttpStatus.OK) {
//        return Arrays.asList(Objects.requireNonNull(response.getBody()));
//      } else {
//        throw new NPCException(NPCErrorCode.UNSUCCESSFUL_RESPONSE_CODE,
//            "failed to retrieve return label for return_id=" + returnId
//                + " status code was status_code=" + response.getStatusCode().toString());
//      }
//    } catch (RestClientException ex) {
//      MetricUtils.logCustomMetric(
//          MetricUtils.METRIC_NAME_EXCEPTIONS,
//          ImmutableSet.of(
//                  MetricsTags.TAG_RETURNS_SERVICE,
//                  MetricsTags.METHOD_TAG_BASE + "getReturnLabels"));
//      throw new NPCException("exception retrieving return labels for return_id=" + returnId, ex);
//    }
//  }
//}
