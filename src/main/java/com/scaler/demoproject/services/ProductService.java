package com.scaler.demoproject.services;

import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;

import java.util.List;

public interface ProductService  {
    Product getSingleProduct(Long productId) throws ProductNotFoundException;
    List<Product> getAllProducts() ;
    Product createProduct(Product product);
    List<Category> getAllCategories();
    Product updateSingleProduct(Long productId, Product product) throws ProductNotFoundException;
    void deleteSingleProduct(Long productId) throws ProductNotFoundException;
}
