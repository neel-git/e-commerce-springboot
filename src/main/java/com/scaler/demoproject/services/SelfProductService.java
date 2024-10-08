package com.scaler.demoproject.services;

import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import com.scaler.demoproject.repositories.CategoryRepository;
import com.scaler.demoproject.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService{
    private ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository,CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> p = productRepository.findById(productId);
        if(p.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found");
        }

        return p.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        Category cat = categoryRepository.findByTitle(product.getCategory().getTitle());
        if(cat == null) {
            // No category with our title in the database
            Category newCategory = new Category();
            newCategory.setTitle(product.getCategory().getTitle());
            Category newRow = categoryRepository.save(newCategory);
            product.setCategory(newRow);
        } else {
            product.setCategory(cat);
        }
        Product savedProduct = productRepository.save(product);
        return product;
    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    public Product updateSingleProduct(Long productId, Product product) throws ProductNotFoundException {
       Optional<Product> p = productRepository.findById(productId);
       if(p.isEmpty()){
           throw new ProductNotFoundException("Product not Found");
       }
       Product currProduct = p.get();
       currProduct.setId(product.getId());
       currProduct.setPrice(product.getPrice());
       currProduct.setImageUrl(product.getImageUrl());
       currProduct.setDescription(product.getDescription());
       currProduct.setCategory(product.getCategory());
      // productRepository.updateById(productId,currProduct);

       return currProduct;
    }

    @Override
    public void deleteSingleProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
