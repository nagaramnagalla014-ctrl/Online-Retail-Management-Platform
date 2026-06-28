package com.retail.platform.controller;

import com.retail.platform.dto.ApiResponse;
import com.retail.platform.dto.OrderRequest;
import com.retail.platform.model.Customer;
import com.retail.platform.model.Order;
import com.retail.platform.service.CustomerService;
import com.retail.platform.service.OrderService;
import com.retail.platform.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;
    @Autowired private PaymentService paymentService;

    private Customer getCustomer(HttpSession session) {
        Long id = (Long) session.getAttribute("customerId");
        if (id == null) throw new RuntimeException("Not authenticated");
        return customerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> placeOrder(@RequestBody OrderRequest req, HttpSession session) {
        try {
            Customer customer = getCustomer(session);
            Order order = orderService.placeOrder(customer, req);
            if (req.getPaymentMethod() != null && !req.getPaymentMethod().isBlank()) {
                paymentService.processPayment(order.getOrderId(), req.getPaymentMethod());
                order = orderService.getById(order.getOrderId()).orElse(order);
            }
            return ResponseEntity.ok(ApiResponse.ok("Order placed successfully", order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> myOrders(HttpSession session) {
        try {
            List<Order> orders = orderService.getOrdersByCustomer(getCustomer(session));
            return ResponseEntity.ok(ApiResponse.ok(orders));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable String orderNumber, HttpSession session) {
        try {
            getCustomer(session);
            return orderService.getByOrderNumber(orderNumber)
                .map(o -> ResponseEntity.ok(ApiResponse.ok(o)))
                .orElse(ResponseEntity.notFound().<ApiResponse<Order>>build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ApiResponse.error(e.getMessage()));
        }
    }
}
