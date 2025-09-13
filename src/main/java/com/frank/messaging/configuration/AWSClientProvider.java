package com.frank.messaging.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
public class AWSClientProvider {

    @Bean
    public CloudWatchClient cloudWatchClient() {
        System.out.println("Creating a cloudwatch client");
        return CloudWatchClient.builder().region(Region.US_EAST_1).build();
    }
}
