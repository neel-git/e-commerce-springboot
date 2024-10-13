package com.scaler.demoproject.services;

import com.scaler.demoproject.dto.FakeStoreProductDto;
import com.scaler.demoproject.exceptions.ExternalApiException;
import com.scaler.demoproject.exceptions.ProductNotFoundException;
import com.scaler.demoproject.model.Category;
import com.scaler.demoproject.model.Product;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    private RestTemplate restTemplate;
    private final String fakeStoreUrl = "https://fakestoreapi.com/products";

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        try {
            FakeStoreProductDto fakeStoreProductDto = restTemplate
                    .getForObject(fakeStoreUrl + "/" + productId, FakeStoreProductDto.class);

            if (fakeStoreProductDto == null) {
                throw new ProductNotFoundException("Product Not Found with id " + productId);
            }
            return fakeStoreProductDto.toProduct();
        } catch (HttpClientErrorException e) {
            throw new ProductNotFoundException("Product not found: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Unable to reach the product service: " + e.getMessage());
        } catch (RestClientException e) {
            throw new RuntimeException("An error occurred while fetching product details", e);
        }

    }

    @Override
    public List<Product> getAllProducts() throws ExternalApiException {
        List<Product> products = new ArrayList<>();
        try {
            FakeStoreProductDto[] response = restTemplate.getForObject(fakeStoreUrl, FakeStoreProductDto[].class);
            if (response == null || response.length == 0) {
                throw new ExternalApiException("No products found at the external service.");
            }
            for (FakeStoreProductDto fs : response) {
                products.add(fs.toProduct());
            }

            return products;
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException("Error fetching products: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Unable to reach the external product service: " + e.getMessage(), e);
        } catch (RestClientException e) {
            // Handle any other RestClient exceptions
            throw new ExternalApiException("An error occurred while fetching products from the external service.", e);
        }
    }

    @Override
    public Product createProduct(Product product) {
        try {
            FakeStoreProductDto fs = new FakeStoreProductDto();
            fs.setCategory(product.getCategory().getTitle());
            fs.setTitle(product.getTitle());
            fs.setId(product.getId());
            fs.setDescription(product.getDescription());
            fs.setImage(product.getImageUrl());
            fs.setPrice(product.getPrice());

            FakeStoreProductDto response = restTemplate.patchForObject(fakeStoreUrl, fs, FakeStoreProductDto.class);
            if (response == null) {
                throw new ExternalApiException("Failed to create product: No response from external API.");
            }
            return response.toProduct();
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException("Error creating product: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Unable to reach external product service: " + e.getMessage(), e);
        } catch (RestClientException e) {
            // Handle general RestTemplate errors
            throw new ExternalApiException("An error occurred while creating product in external service.", e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        ResponseEntity<Category[]> response = restTemplate.getForEntity(fakeStoreUrl + "/categories", Category[].class);

        return Arrays.asList(response.getBody());
    }

    @Override
    public Product updateSingleProduct(Long productId, Product newProduct) {
        try {
            FakeStoreProductDto fs = new FakeStoreProductDto();
            fs.setCategory(newProduct.getCategory().getTitle());
            fs.setTitle(newProduct.getTitle());
            fs.setId(newProduct.getId());
            fs.setDescription(newProduct.getDescription());
            fs.setImage(newProduct.getImageUrl());
            fs.setPrice(newProduct.getPrice());

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HttpEntity
            HttpEntity<FakeStoreProductDto> entity = new HttpEntity<>(fs, headers);

            // Make PUT request to third-party API
            restTemplate.exchange(fakeStoreUrl + "/" + productId, HttpMethod.PUT, entity, Void.class);

            return newProduct;
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException("Error updating product: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Unable to reach the external product service: " + e.getMessage(), e);
        } catch (RestClientException e) {
            throw new ExternalApiException("An error occurred while updating the product in the external service.", e);
        }
    }

    @Override
    public void deleteSingleProduct(Long productId) throws ExternalApiException {
        try {
            restTemplate.delete(fakeStoreUrl + "/" + productId);
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException("Error deleting product: HTTP " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            throw new ExternalApiException("Unable to reach the external product service: " + e.getMessage(), e);
        } catch (RestClientException e) {
            throw new ExternalApiException("An error occurred while deleting the product in the external service.", e);
        }
    }

}