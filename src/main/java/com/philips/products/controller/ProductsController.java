package com.philips.products.controller;

import com.philips.products.models.ErrorMapper;
import com.philips.products.models.ErrorMessage;
import com.philips.products.models.ProductCatalog;
import com.philips.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/")
public class ProductsController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private ProductService productService;
    private ErrorMapper errorMapper = new ErrorMapper();
    /*
    * Exposing an Endpoint to get all the product catalog records
    * */
    @GetMapping(value = "/supply-chain", produces = "application/json")
    public ResponseEntity<?> getAllProductDetails(@RequestHeader HttpHeaders headers) throws Exception {
        List<ProductCatalog> productCatalogList = null;
        try {
            productCatalogList = productService.getAllProductInfo();
            return new ResponseEntity<>(productCatalogList, HttpStatus.OK);
        } catch (Exception ex) {

        return new ResponseEntity<>(productCatalogList, HttpStatus.SERVICE_UNAVAILABLE);
    }

    }

    /*
     * Exposing an Endpoint to save the new product information.
     * */
    @PostMapping(value = "/supply-chain", produces = "application/json")
    public ResponseEntity<?> acceptProducts(@RequestHeader HttpHeaders headers,
                                            @RequestBody ProductCatalog product, final Errors errors) throws Exception {
        if (errors.hasErrors()) {
            ErrorMessage errorMessage = errorMapper.mapErrors(product, errors);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        } else {
            LOG.info("input payload={}", product);
            productService.saveProductInfo(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    /*
     * Exposing an Endpoint to get the product details based on the product id
     * */
    @GetMapping(value = "/supply-chain/{id}", produces = "application/json")
    public ResponseEntity<?> getProductDetails(@RequestHeader HttpHeaders headers,
                                               @PathVariable("id") String id) throws Exception {
        LOG.info("get details for the product id={}", id);
        Optional<ProductCatalog> productCatalogs =  productService.getProductInfo(id);
        if(productCatalogs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(productCatalogs.get(), HttpStatus.OK);
        }
    }

    /*
     * Exposing an Endpoint to delete the product details based on the product id
     * */
    @DeleteMapping(value = "/supply-chain/{id}", produces = "application/json")
    public ResponseEntity<?> deleteProductDetails(@RequestHeader HttpHeaders headers,
                                                  @PathVariable("id") String id) throws Exception {
       try {
            LOG.info("delete request for the product id={}", id);
            productService.deleteProductInfo(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            LOG.info("No records found in the db for product id={}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
