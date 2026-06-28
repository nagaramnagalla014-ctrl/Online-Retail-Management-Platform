package com.retail.platform.repository;

import com.retail.platform.model.Category;
import com.retail.platform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();
    List<Product> findByCategoryAndIsActiveTrue(Category category);
    Optional<Product> findBySku(String sku);
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stockQuantity <= :threshold")
    List<Product> findLowStockProducts(int threshold);

    long countByIsActiveTrue();
}
