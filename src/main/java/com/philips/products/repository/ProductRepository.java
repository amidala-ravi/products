package com.philips.products.repository;

import com.philips.products.models.ProductCatalog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository {
    public void saveProductInfo(ProductCatalog product);
    public Optional<ProductCatalog> getProductInfo(String id);
    public void deleteProductInfo(String id);
    public List<ProductCatalog> getAllProductInfo();
}
