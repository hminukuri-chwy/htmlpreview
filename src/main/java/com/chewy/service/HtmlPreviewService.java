package com.chewy.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.chewy.domain.aws.AWSGatewayRequest;
import com.chewy.domain.aws.AWSGatewayResponse;

import java.io.IOException;

public interface HtmlPreviewService {

     APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent awsGatewayRequest) throws IOException;

}
