package com.scaler.demoproject.service;

import com.scaler.demoproject.dto.FakeStoreProductDto;
import com.scaler.demoproject.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    @Override
    public Product getSingleProduct(Long productId) {
        FakeStoreProductDto fakeStoreProductDto = restTemplate
                .getForObject("https://fakestoreapi.com/products/"+productId,FakeStoreProductDto.class);

        // assert fakeStoreProductDto != null;
        return fakeStoreProductDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }
}
