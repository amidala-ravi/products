package com.philips.products.repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.philips.products.models.ProductCatalog;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@NoArgsConstructor
@Qualifier("ProductServiceImpl")
public class ProductRepositoryImpl implements ProductRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRepositoryImpl.class);
    private DynamoDB dynamoDB;
    public static String tableName="product.catalog";
    public ProductRepositoryImpl(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    @Autowired
    @Qualifier("DynamoDBConfiguration")
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public void saveProductInfo(ProductCatalog prodcutsInfo) {
        dynamoDBMapper.save(prodcutsInfo);
    }

    @Override
    public Optional<ProductCatalog> getProductInfo(String id) {
        LOG.info("Repository get the product info for id={}", id);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":id", new AttributeValue().withS(id));

        DynamoDBQueryExpression<ProductCatalog> queryExpression = new DynamoDBQueryExpression<ProductCatalog>()
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(eav)
                .withScanIndexForward(false)
                .withConsistentRead(false);

        return dynamoDBMapper.query(ProductCatalog.class, queryExpression).stream().findFirst();
    }

    @Override
    public void deleteProductInfo(String id) {
        try {
            Table table = dynamoDB.getTable(tableName);
            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey(new PrimaryKey("id", id));
            DeleteItemOutcome deleteItemOutcome =  table.deleteItem(deleteItemSpec);
            LOG.info("Successfully deleted the product record for id = {} and deleteItemOutcome={}", id, deleteItemOutcome);
        } catch (AmazonServiceException e) {
            LOG.info("Error while deleting the record error={}", e.getErrorMessage());
        }

    }

    @Override
    public List<ProductCatalog> getAllProductInfo() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<ProductCatalog> scanResult = dynamoDBMapper.scan(ProductCatalog.class, scanExpression);
        LOG.info("All products count={} and data={}", scanResult.size(), scanResult);
        return scanResult;
    }
}
