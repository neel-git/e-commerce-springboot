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
        return productRepository.findAll();
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
        return categoryRepository.findAll();
    }

    @Override
    public Product updateSingleProduct(Long productId, Product updateProduct) throws ProductNotFoundException {
       Optional<Product> optionalProduct = productRepository.findById(productId);
       if(optionalProduct.isEmpty()){
           throw new ProductNotFoundException("Product not Found");
       }
       Product currProduct = optionalProduct.get();
       currProduct.setTitle(updateProduct.getTitle());
       currProduct.setPrice(updateProduct.getPrice());
       currProduct.setImageUrl(updateProduct.getImageUrl());
       currProduct.setDescription(updateProduct.getDescription());
       currProduct.setCategory(updateProduct.getCategory());
       productRepository.save(currProduct);

       return currProduct;
    }

    @Override
    public void deleteSingleProduct(Long productId) throws ProductNotFoundException {
       if(productRepository.existsById(productId)) {
           productRepository.deleteById(productId);
       } else {
           throw new ProductNotFoundException("Product with id "+ productId+ " not found");
       }
    }
}
