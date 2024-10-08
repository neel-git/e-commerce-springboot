package com.scaler.demoproject.repositories;

import com.scaler.demoproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product save(Product product);
    Product findByTitle(String title);
    Product findByDescription(String description);
    List<Product> getProductById(Long id);

    void deleteById(Long id);
    // Product updateById(Long id, Product product);

    // HQL
    @Query("select p from Product p where p.category.id = :categoryId")
    List<Product> getProductByCategoryId(@Param("categoryId") Long categoryId);

    // Native Queries
    @Query(value = "select * from product p where p.catrgory_id = :categoryId" , nativeQuery = true)
    List<Product> getProductByCategoryIdWithNativeQuery(@Param("categoryId") Long catrgoryId);
}
