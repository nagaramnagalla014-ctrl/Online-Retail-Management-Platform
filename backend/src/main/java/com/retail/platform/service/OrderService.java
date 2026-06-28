package com.retail.platform.service;

import com.retail.platform.dto.OrderRequest;
import com.retail.platform.model.*;
import com.retail.platform.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartService cartService;
    @Autowired private ProductService productService;
    @Autowired private PromotionService promotionService;

    public Order placeOrder(Customer customer, OrderRequest req) {
        Cart cart = cartService.getOrCreateCart(customer);
        if (cart.getItems().isEmpty())
            throw new RuntimeException("Cart is empty");

        // Calculate subtotal
        BigDecimal subtotal = cart.getItems().stream()
            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Apply promotion
        BigDecimal discount = BigDecimal.ZERO;
        if (req.getPromoCode() != null && !req.getPromoCode().isBlank()) {
            discount = promotionService.applyPromotion(req.getPromoCode(), subtotal);
        }

        BigDecimal shippingCharge = subtotal.compareTo(BigDecimal.valueOf(500)) >= 0 ?
            BigDecimal.ZERO : BigDecimal.valueOf(49);
        BigDecimal total = subtotal.subtract(discount).add(shippingCharge);

        // Create order
        Order order = new Order();
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setCustomer(customer);
        order.setSubtotal(subtotal);
        order.setDiscount(discount);
        order.setShippingCharge(shippingCharge);
        order.setTotalAmount(total);
        order.setShippingAddress(req.getShippingAddress());
        order.setShippingCity(req.getShippingCity());
        order.setShippingState(req.getShippingState());
        order.setShippingPincode(req.getShippingPincode());
        order.setNotes(req.getNotes());

        // Create order items and deduct stock
        for (CartItem cartItem : cart.getItems()) {
            productService.updateStock(cartItem.getProduct().getProductId(), cartItem.getQuantity());
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setProductName(cartItem.getProduct().getName());
            oi.setQuantity(cartItem.getQuantity());
            oi.setUnitPrice(cartItem.getProduct().getPrice());
            oi.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.getItems().add(oi);
        }

        Order saved = orderRepository.save(order);
        cartService.clearCart(customer);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        return orderRepository.calculateTotalRevenue();
    }

    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return orderRepository.countByStatus(status);
    }
}
