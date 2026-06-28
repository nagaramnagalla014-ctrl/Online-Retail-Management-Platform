package com.retail.platform.controller;

import com.retail.platform.dto.ApiResponse;
import com.retail.platform.model.*;
import com.retail.platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;
    @Autowired private ShipmentService shipmentService;
    @Autowired private PromotionService promotionService;

    // Dashboard stats
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", orderService.getTotalRevenue());
        stats.put("pendingOrders", orderService.countByStatus("PENDING"));
        stats.put("activeProducts", productService.countActiveProducts());
        stats.put("totalCustomers", customerService.count());
        stats.put("lowStockProducts", productService.getLowStockProducts());
        stats.put("allOrders", orderService.getAllOrders().stream().limit(10));
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    // Product management
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(ApiResponse.ok(productService.save(product)));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(productService.update(id, product)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Product deactivated", null));
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<ApiResponse<List<Product>>> lowStock() {
        return ResponseEntity.ok(ApiResponse.ok(productService.getLowStockProducts()));
    }

    // Category management
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        // reuse product service since it holds category repo
        return ResponseEntity.ok(ApiResponse.ok(null)); // placeholder — category save via productService
    }

    // Order management
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getAllOrders()));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@PathVariable Long id,
                                                                 @RequestParam String status) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(orderService.updateStatus(id, status)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/orders/{id}/ship")
    public ResponseEntity<ApiResponse<Shipment>> shipOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(shipmentService.createShipment(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/shipments/{id}/status")
    public ResponseEntity<ApiResponse<Shipment>> updateShipmentStatus(@PathVariable Long id,
                                                                        @RequestParam String status) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(shipmentService.updateStatus(id, status)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Promotion management
    @GetMapping("/promotions")
    public ResponseEntity<ApiResponse<List<Promotion>>> getAllPromotions() {
        return ResponseEntity.ok(ApiResponse.ok(promotionService.getAll()));
    }

    @PostMapping("/promotions")
    public ResponseEntity<ApiResponse<Promotion>> createPromotion(@RequestBody Promotion promotion) {
        return ResponseEntity.ok(ApiResponse.ok(promotionService.save(promotion)));
    }

    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable Long id) {
        promotionService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Promotion deleted", null));
    }
}
