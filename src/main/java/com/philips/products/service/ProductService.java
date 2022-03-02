package com.philips.products.service;

import com.philips.products.models.ProductCatalog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ProductService {
     void saveProductInfo(ProductCatalog product);
     void deleteProductInfo(String id);
     Optional<ProductCatalog>  getProductInfo(String id);
     List<ProductCatalog> getAllProductInfo();
}
