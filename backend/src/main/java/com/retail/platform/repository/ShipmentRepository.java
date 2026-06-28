package com.retail.platform.repository;

import com.retail.platform.model.Order;
import com.retail.platform.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrder(Order order);
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
}
