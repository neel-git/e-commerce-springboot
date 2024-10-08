package com.scaler.demoproject.services;

import com.scaler.demoproject.dto.FakeStoreCategoryDto;
import com.scaler.demoproject.dto.FakeStoreProductDto;
import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;
    private final String fakeStoreUrl = "https://fakestoreapi.com/products";
    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
           FakeStoreProductDto fakeStoreProductDto = restTemplate
                .getForObject(fakeStoreUrl+"/"+productId,FakeStoreProductDto.class);

//         ResponseEntity<FakeStoreProductDto> fakeStoreProductDto = restTemplate.getForEntity
//                ("https://fakestoreapi.com/products/"+productId,FakeStoreProductDto.class);
        if(fakeStoreProductDto == null){
            throw new ProductNotFoundException("Product Not Found with id " + productId);
        }
        // assert fakeStoreProductDto != null;
        return fakeStoreProductDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        FakeStoreProductDto[] res = restTemplate.getForObject(fakeStoreUrl, FakeStoreProductDto[].class);

        for(FakeStoreProductDto fs : res) {
            products.add(fs.toProduct());
        }

        return products;
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto fs = new FakeStoreProductDto();
        fs.setCategory(product.getCategory().getTitle());
        fs.setTitle(product.getTitle());
        fs.setId(product.getId());
        fs.setDescription(product.getDescription());
        fs.setImage(product.getImageUrl());
        fs.setPrice(product.getPrice());

        FakeStoreProductDto response = restTemplate.patchForObject(fakeStoreUrl,fs, FakeStoreProductDto.class);

        return response.toProduct();
    }

    @Override
    public List<Category> getAllCategories() {
        ResponseEntity<Category[]> response = restTemplate.getForEntity(fakeStoreUrl+"/categories",Category[].class);

        return Arrays.asList(response.getBody());
    }

    @Override
    public Product updateSingleProduct(Long productId, Product newProduct) {
        FakeStoreProductDto fs = new FakeStoreProductDto();
        fs.setCategory(newProduct.getCategory().getTitle());
        fs.setTitle(newProduct.getTitle());
        fs.setId(newProduct.getId());
        fs.setDescription(newProduct.getDescription());
        fs.setImage(newProduct.getImageUrl());
        fs.setPrice(newProduct.getPrice());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FakeStoreProductDto> entity = new HttpEntity<>(fs, headers);
        restTemplate.exchange(fakeStoreUrl + "/" + productId, HttpMethod.PUT, entity, Void.class);
        return newProduct;
    }

    @Override
    public void deleteSingleProduct(Long productId) {
        restTemplate.delete(fakeStoreUrl+"/"+productId);
    }
}
