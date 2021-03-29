# Prerequistites
1. Install Docker Desktop(https://www.docker.com/products/docker-desktop)
2. Change username and password in gradle.properties
3. Do ./gradlew clean build

# htmlpreview
aws lambda function with graalvm custom runtime.
# Layout
```
- src 
   |-main
      |- java [application source code]
      |- resources [resource required to run the application]
   |-test [test location]
```
# Build Graalvm Custom Runtime Function

1. Make docker-build.sh an executable file

   **chmod +x docker-build.sh**
2. Run make image from root, this will generate function.zip under build directory

   **make image**
3. Deploy this function.zip file to AWS Lambda

# Build java jar file

1. **./gradlew clean build**

# Code
The project uses [micronaut](https://micronaut.io/) as the Dependency Injection framework.
This framework is compile time injection and reduces the lambda start up time.

This project also takes advantage of graalvm runtime
### Feature aws-lambda-custom-runtime documentation

- [Micronaut Custom AWS Lambda runtime documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes)

- [https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html)


## Handler

[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)

Handler: com.chewy.LambdaRuntime

# Micronaut Documentation
Here is a link covering how to use Micronaut (very similar to spring boot). it covers http clients,
aws, security, dependency injection, configuration setup, etc...
https://docs.micronaut.io/latest/guide/index.html





