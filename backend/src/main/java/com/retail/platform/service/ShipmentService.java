package com.retail.platform.service;

import com.retail.platform.model.Order;
import com.retail.platform.model.Shipment;
import com.retail.platform.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ShipmentService {

    @Autowired private ShipmentRepository shipmentRepository;
    @Autowired private OrderService orderService;

    public Shipment createShipment(Long orderId) {
        Order order = orderService.getById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber("TRK" + System.currentTimeMillis());
        shipment.setCarrier("RetailExpress");
        shipment.setStatus("PROCESSING");
        shipment.setEstimatedDelivery(cal.getTime());

        orderService.updateStatus(orderId, "PROCESSING");
        return shipmentRepository.save(shipment);
    }

    public Shipment updateStatus(Long shipmentId, String status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new RuntimeException("Shipment not found: " + shipmentId));
        shipment.setStatus(status);
        if ("SHIPPED".equals(status)) {
            shipment.setShippedAt(new Date());
            orderService.updateStatus(shipment.getOrder().getOrderId(), "SHIPPED");
        } else if ("DELIVERED".equals(status)) {
            shipment.setDeliveredAt(new Date());
            orderService.updateStatus(shipment.getOrder().getOrderId(), "DELIVERED");
        }
        return shipmentRepository.save(shipment);
    }

    @Transactional(readOnly = true)
    public Optional<Shipment> trackByOrderId(Long orderId) {
        Order order = orderService.getById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return shipmentRepository.findByOrder(order);
    }

    @Transactional(readOnly = true)
    public Optional<Shipment> trackByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber);
    }
}
