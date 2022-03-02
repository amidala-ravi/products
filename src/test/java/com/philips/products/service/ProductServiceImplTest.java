package com.philips.products.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.products.models.ProductCatalog;
import com.philips.products.repository.ProductRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepositoryImpl productRepository;
    private ObjectMapper objectMapper;
    ProductCatalog productCatalog;
    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
        productCatalog = objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream("products.json"), ProductCatalog.class);
    }
    @Test
    public void getAllProductsTest() throws Exception {
        List<ProductCatalog> productCatalogList = new ArrayList<>();
        productCatalogList.add(productCatalog);
        Mockito.when(productRepository.getAllProductInfo()).thenReturn(productCatalogList);
        productService.getAllProductInfo();
        Mockito.verify(productRepository, Mockito.times(1)).getAllProductInfo();
    }

    @Test
    public void getProductsTest() throws Exception {
        Optional<ProductCatalog> optionalProductCatalog = Optional.of(productCatalog);
        Mockito.when(productRepository.getProductInfo(Mockito.anyString())).thenReturn(optionalProductCatalog);
        productService.getProductInfo("id123");
        Mockito.verify(productRepository, Mockito.times(1)).getProductInfo(Mockito.anyString());
    }

    @Test
    public void saveProductsTest() throws Exception {
        Mockito.doNothing().when(productRepository).saveProductInfo(Mockito.any());
        productService.saveProductInfo(productCatalog);
        Mockito.verify(productRepository, Mockito.times(1)).saveProductInfo(Mockito.any());
    }

    @Test
    public void deleteProductsTest() throws Exception {
        Mockito.doNothing().when(productRepository).deleteProductInfo(Mockito.anyString());
        productService.deleteProductInfo("id123");
        Mockito.verify(productRepository, Mockito.times(1)).deleteProductInfo(Mockito.anyString());
    }

}
