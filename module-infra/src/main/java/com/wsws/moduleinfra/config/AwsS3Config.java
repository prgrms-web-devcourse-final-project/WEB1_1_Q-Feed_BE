package com.wsws.moduleinfra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                System.getenv("AWS_ACCESS_KEY"), // 환경 변수에서 읽어오기
                System.getenv("AWS_SECRET_KEY")
        );

        return S3Client.builder()
                .region(Region.of(System.getenv("AWS_REGION"))) // 환경 변수에서 리전 읽기
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
