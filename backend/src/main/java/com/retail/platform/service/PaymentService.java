package com.retail.platform.service;

import com.retail.platform.model.Order;
import com.retail.platform.model.Payment;
import com.retail.platform.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private OrderService orderService;

    public Payment processPayment(Long orderId, String method) {
        Order order = orderService.getById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(method);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(new Date());

        Payment saved = paymentRepository.save(payment);
        orderService.updateStatus(orderId, "CONFIRMED");
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<Payment> getByOrderId(Long orderId) {
        Order order = orderService.getById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return paymentRepository.findByOrder(order);
    }
}
