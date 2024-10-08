package com.scaler.demoproject.controller;

import com.scaler.demoproject.dto.ErrorDto;
import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import com.scaler.demoproject.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.LockSupport;


@RestController
public class ProductController {
    private ProductService productService;


    public ProductController(@Qualifier("selfProductService") ProductService productService){
        this.productService = productService;
    }
    //POST /product
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        Product postRequestResponse = productService.createProduct(product);
        return postRequestResponse;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long productId) throws ProductNotFoundException {
        Product currentProduct = productService.getSingleProduct(productId);;
        ResponseEntity<Product> response = new ResponseEntity<>(currentProduct, HttpStatus.OK);



        return response;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productService.getAllProducts();
        ResponseEntity<List<Product>> response = new ResponseEntity<>(products,HttpStatus.OK);

        return response;
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId, @RequestBody Product newProduct) throws ProductNotFoundException {
        return new ResponseEntity<>(productService.updateSingleProduct(productId,newProduct),HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteSingleProduct(@PathVariable("id") Long productId) {
        productService.deleteSingleProduct(productId);
        return new ResponseEntity<>("Product with ID " + productId + " deleted successfully.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(Exception e){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
    }
}
