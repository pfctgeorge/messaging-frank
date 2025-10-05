package com.frank.messaging.configuration;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AWSClientProvider {

    @Bean
    public CloudWatchClient cloudWatchClient() {
        System.out.println("Creating a cloudwatch client");
        return CloudWatchClient.builder().region(Region.US_EAST_1).build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(Region.US_EAST_1).build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.US_EAST_1)
                .s3Client(s3Client())
                .build();
    }

    @Bean
    public ApiGatewayManagementApiClient apiGatewayManagementApiClient() {
        return ApiGatewayManagementApiClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("https://jlyl843aik.execute-api.us-east-1.amazonaws.com/production"))
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
