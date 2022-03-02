package com.philips.products.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.products.models.ProductCatalog;
import com.philips.products.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ProductsControllerTest {
    @InjectMocks
    private ProductsController productsController;
    private MockMvc mockMvc;
    @Mock
    private ProductService productService;
    private ObjectMapper objectMapper;
    ProductCatalog productCatalog;
    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        mockMvc = standaloneSetup(productsController).build();
        productCatalog = objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream("products.json"), ProductCatalog.class);
    }
    // Test case to test 200OK positive scenario to get all the product details
    @Test
    public void getAllProductsList200OKTest() throws Exception {
        String uri = "/supply-chain";
        List<ProductCatalog> productCatalogList = new ArrayList<>();
        productCatalogList.add(productCatalog);
        Mockito.when(productService.getAllProductInfo()).thenReturn(productCatalogList);
        MvcResult mvcResult = mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        Mockito.verify(productService, Mockito.times(1)).getAllProductInfo();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(content);
    }
    // Test case to test 500 Internal error negative scenario to get all the product details
    @Test(expected = Exception.class)
    public void getAllProductsList500ErrorTest() throws Exception {
        String uri = "/supply-chain";
        List<ProductCatalog> productCatalogList = new ArrayList<>();
        productCatalogList.add(productCatalog);
        Mockito.when(productService.getAllProductInfo()).thenThrow(new Exception("Internal error!"));
        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        Mockito.verify(productService, Mockito.times(1)).getAllProductInfo();

    }
    // Test case to test 200OK positive scenario to get the product details based on productId
    @Test
    public void getProductInfo200OKTest() throws Exception {
        String uri = "/supply-chain/product123";
        Optional<ProductCatalog> optionalProductCatalog = Optional.of(productCatalog);
        Mockito.when(productService.getProductInfo(Mockito.anyString())).thenReturn(optionalProductCatalog);
        MvcResult mvcResult = mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(content);
        Mockito.verify(productService, Mockito.times(1)).getProductInfo(Mockito.anyString());
    }
    // Test case to test 400 record not found positive scenario, if no record found in the db
    @Test
    public void getProductInfo400EmptyTest() throws Exception {
        String uri = "/supply-chain/product123";
        Optional<ProductCatalog> optionalProductCatalog = Optional.empty();
        Mockito.when(productService.getProductInfo(Mockito.anyString())).thenReturn(optionalProductCatalog);
        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
        Mockito.verify(productService, Mockito.times(1)).getProductInfo(Mockito.anyString());
    }
    // Test case to test 201 Created positive scenario to save the details in the DB
    @Test
    public void postProductDetails201Test() throws Exception {
        String uri = "/supply-chain";
        Mockito.doNothing().when(productService).saveProductInfo(Mockito.any());
        String inputJson = objectMapper.writeValueAsString(productCatalog);
        mockMvc.perform(post(uri)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        Mockito.verify(productService, Mockito.times(1)).saveProductInfo(Mockito.any());
    }
    // Test case to test 400 Bad request scenario
    @Test
    public void postProductDetails400Test() throws Exception {
        String uri = "/supply-chain";
        String inputJson = "";
        mockMvc.perform(post(uri)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void deleteProductInfo204Test() throws Exception {
        String uri = "/supply-chain/product123";
        Mockito.doNothing().when(productService).deleteProductInfo(Mockito.anyString());
        mockMvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        Mockito.verify(productService, Mockito.times(1)).deleteProductInfo(Mockito.anyString());
    }

    @Test
    public void deleteProductInfoNotFoundTest() throws Exception {
        String uri = "/supply-chain/product123";
        Mockito.doThrow(new NoSuchElementException()).when(productService).deleteProductInfo(Mockito.anyString());
        mockMvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        Mockito.verify(productService, Mockito.times(1)).deleteProductInfo(Mockito.anyString());
    }

}
