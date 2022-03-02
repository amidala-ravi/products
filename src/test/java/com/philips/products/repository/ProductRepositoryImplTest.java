package com.philips.products.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.products.models.ProductCatalog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryImplTest {
    @InjectMocks
    private ProductRepositoryImpl productRepository;
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    private ObjectMapper objectMapper;
    ProductCatalog productCatalog;
    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        productCatalog = objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream("products.json"), ProductCatalog.class);
    }

    @Test
    public void saveProductsTest() throws Exception {
        Mockito.doNothing().when(dynamoDBMapper).save(Mockito.any());
        productRepository.saveProductInfo(productCatalog);
        Mockito.verify(dynamoDBMapper, Mockito.times(1)).save(Mockito.any());
    }

}
