package com.retail.platform.service;

import com.retail.platform.model.Category;
import com.retail.platform.model.Product;
import com.retail.platform.repository.CategoryRepository;
import com.retail.platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        return productRepository.findByCategoryAndIsActiveTrue(category);
    }

    @Transactional(readOnly = true)
    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword);
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Long id, Product updated) {
        Product existing = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setBrand(updated.getBrand());
        existing.setImageUrl(updated.getImageUrl());
        existing.setIsActive(updated.getIsActive());
        if (updated.getCategory() != null)
            existing.setCategory(updated.getCategory());
        return productRepository.save(existing);
    }

    public void updateStock(Long productId, int quantity) {
        Product p = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        if (p.getStockQuantity() < quantity)
            throw new RuntimeException("Insufficient stock for: " + p.getName());
        p.setStockQuantity(p.getStockQuantity() - quantity);
        productRepository.save(p);
    }

    public void delete(Long id) {
        Product p = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        p.setIsActive(false);
        productRepository.save(p);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts(5);
    }

    @Transactional(readOnly = true)
    public long countActiveProducts() {
        return productRepository.countByIsActiveTrue();
    }
}
