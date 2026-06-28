package com.retail.platform.controller;

import com.retail.platform.dto.ApiResponse;
import com.retail.platform.dto.CartItemRequest;
import com.retail.platform.model.Cart;
import com.retail.platform.model.Customer;
import com.retail.platform.service.CartService;
import com.retail.platform.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private CustomerService customerService;

    private Customer getCustomer(HttpSession session) {
        Long id = (Long) session.getAttribute("customerId");
        if (id == null) throw new RuntimeException("Not authenticated");
        return customerService.getById(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(HttpSession session) {
        try {
            Cart cart = cartService.getOrCreateCart(getCustomer(session));
            return ResponseEntity.ok(ApiResponse.ok(cart));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addItem(@RequestBody CartItemRequest req, HttpSession session) {
        try {
            Cart cart = cartService.addItem(getCustomer(session), req.getProductId(), req.getQuantity());
            return ResponseEntity.ok(ApiResponse.ok("Item added", cart));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse<Cart>> updateItem(@PathVariable Long cartItemId,
                                                         @RequestParam int quantity,
                                                         HttpSession session) {
        try {
            Cart cart = cartService.updateQuantity(getCustomer(session), cartItemId, quantity);
            return ResponseEntity.ok(ApiResponse.ok("Cart updated", cart));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse<Cart>> removeItem(@PathVariable Long cartItemId, HttpSession session) {
        try {
            Cart cart = cartService.removeItem(getCustomer(session), cartItemId);
            return ResponseEntity.ok(ApiResponse.ok("Item removed", cart));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(HttpSession session) {
        try {
            cartService.clearCart(getCustomer(session));
            return ResponseEntity.ok(ApiResponse.ok("Cart cleared", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
