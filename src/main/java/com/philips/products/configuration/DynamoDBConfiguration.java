package com.philips.products.configuration;


import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DynamoDBConfiguration {
    @Value("${dynamodb.endpoint}")
    private String dynamoDBEndpoint;
    @Value("${dynamoddb.region}")
    private String dynamoRegion;
    @Value("${isLocal}")
    private boolean isLocal;
    @Bean(name="DynamoDBConfiguration")
    public DynamoDBMapper mapper() {
        return new DynamoDBMapper(amazonDynamoDBConfig());
    }

    public AmazonDynamoDB amazonDynamoDBConfig() {
        //if(isLocal) {
            return AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, dynamoRegion))
                    .build();
        //}
    }

    @Bean
    public DynamoDB provideDynamoDB() {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBConfig());
        return dynamoDB;
    }
}