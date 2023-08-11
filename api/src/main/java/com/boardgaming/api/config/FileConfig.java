package com.boardgaming.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {
    private final String endPoint;
    private final String accessKey;
    private final String secretKey;

    public FileConfig(
        @Value("${r2.endPoint}") final String endPoint,
        @Value("${r2.accessKey}") final String accessKey,
        @Value("${r2.secretKey}") final String secretKey
    ) {
        this.endPoint = endPoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Bean
    public AmazonS3 r2Client() {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, null))
            .withPathStyleAccessEnabled(true)
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                accessKey,
                secretKey
            )))
            .build();
    }
}
