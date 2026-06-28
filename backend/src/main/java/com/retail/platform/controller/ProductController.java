package com.retail.platform.controller;

import com.retail.platform.dto.ApiResponse;
import com.retail.platform.model.Category;
import com.retail.platform.model.Product;
import com.retail.platform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired private ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        List<Product> products;
        if (search != null && !search.isBlank()) {
            products = productService.search(search);
        } else if (categoryId != null) {
            products = productService.getByCategory(categoryId);
        } else {
            products = productService.getAllActiveProducts();
        }
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getById(@PathVariable Long id) {
        Optional<Product> product = productService.getById(id);
        return product.map(p -> ResponseEntity.ok(ApiResponse.ok(p)))
            .orElse(ResponseEntity.notFound().<ApiResponse<Product>>build());
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok(productService.getAllCategories()));
    }
}
