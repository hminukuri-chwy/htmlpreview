package com.chewy;


import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import edu.umd.cs.findbugs.annotations.Nullable;

import com.chewy.domain.aws.AWSGatewayRequest;
import com.chewy.domain.aws.AWSGatewayResponse;
import io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime;

import java.net.MalformedURLException;

/**
 * define the lambda input and out here with your own custom pojo OR you can use existing aws one
 * in package {@link com.amazonaws.services.lambda.runtime.events} (e.g. SNSEvent, S3Event, SQSEvent etc.. )
 */
public class LambdaRuntime extends AbstractMicronautLambdaRuntime<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent, APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static void main(String[] args) {
        try {
            new LambdaRuntime().run(args);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    protected RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
    createRequestHandler(String... args) {
        return new HtmlRequestHandler();
    }
}
