package com.philips.products.service;

import com.philips.products.models.ProductCatalog;
import com.philips.products.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Qualifier("ProductServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public void deleteProductInfo(String id) {
        productRepository.deleteProductInfo(id);
    }

    @Override
    public Optional<ProductCatalog> getProductInfo(String id) {
        return productRepository.getProductInfo(id);
    }

    @Override
    public List<ProductCatalog> getAllProductInfo() {
        return productRepository.getAllProductInfo();
    }

    @Override
    public void saveProductInfo(ProductCatalog prodcutsInfo) {
        productRepository.saveProductInfo(prodcutsInfo);
    }
}
