package com.retail.platform.controller;

import com.retail.platform.dto.ApiResponse;
import com.retail.platform.model.Shipment;
import com.retail.platform.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired private ShipmentService shipmentService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<Shipment>> trackByOrder(@PathVariable Long orderId, HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        if (customerId == null)
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        return shipmentService.trackByOrderId(orderId)
            .map(s -> ResponseEntity.ok(ApiResponse.ok(s)))
            .orElse(ResponseEntity.notFound().<ApiResponse<Shipment>>build());
    }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ApiResponse<Shipment>> trackByNumber(@PathVariable String trackingNumber) {
        return shipmentService.trackByTrackingNumber(trackingNumber)
            .map(s -> ResponseEntity.ok(ApiResponse.ok(s)))
            .orElse(ResponseEntity.notFound().<ApiResponse<Shipment>>build());
    }
}
