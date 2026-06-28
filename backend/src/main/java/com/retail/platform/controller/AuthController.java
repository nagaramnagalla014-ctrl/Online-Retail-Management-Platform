package com.retail.platform.controller;

import com.retail.platform.dto.ApiResponse;
import com.retail.platform.dto.LoginRequest;
import com.retail.platform.dto.RegisterRequest;
import com.retail.platform.model.Customer;
import com.retail.platform.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Customer>> register(@RequestBody RegisterRequest req) {
        try {
            Customer customer = new Customer();
            customer.setFirstName(req.getFirstName());
            customer.setLastName(req.getLastName());
            customer.setEmail(req.getEmail());
            customer.setPassword(req.getPassword());
            customer.setPhone(req.getPhone());
            Customer saved = customerService.register(customer);
            return ResponseEntity.ok(ApiResponse.ok("Registered successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Customer>> login(@RequestBody LoginRequest req, HttpSession session) {
        Optional<Customer> opt = customerService.login(req.getEmail(), req.getPassword());
        if (opt.isPresent()) {
            session.setAttribute("customerId", opt.get().getCustomerId());
            session.setAttribute("customerEmail", opt.get().getEmail());
            return ResponseEntity.ok(ApiResponse.ok("Login successful", opt.get()));
        }
        return ResponseEntity.status(401).body(ApiResponse.error("Invalid email or password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.ok("Logged out", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Customer>> me(HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null)
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        Customer customer = customerService.getById(customerId);
        return ResponseEntity.ok(ApiResponse.ok(customer));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Customer>> updateProfile(@RequestBody Customer updated, HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null)
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        Customer saved = customerService.update(customerId, updated);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", saved));
    }
}
