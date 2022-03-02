package com.philips.products.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName = "product.catalog")
public class ProductCatalog {
    @NotNull
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    private double price;
    private int quantity;

    @DynamoDBHashKey(attributeName="id")
    @DynamoDBIndexHashKey(attributeName = "id", globalSecondaryIndexName = "catalogId")
    public String getId() { return id; }
    public void setId(String id) {this.id = id; }

    @DynamoDBAttribute(attributeName="name")
    public String getName() { return name; }
    public void setName(String name) {this.name = name; }

    @DynamoDBAttribute(attributeName="quantiry")
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {this.quantity = quantity; }

    @DynamoDBAttribute(attributeName="price")
    public Double getPrice() { return price; }
    public void setPrice(Double price) {this.price = price; }
}
    //docker run  amazon/dynamodb-local
//aws dynamodb list-tables --endpoint-url http://localhost:8000