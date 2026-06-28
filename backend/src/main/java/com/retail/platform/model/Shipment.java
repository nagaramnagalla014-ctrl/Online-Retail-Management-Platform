package com.retail.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(unique = true, length = 100)
    private String trackingNumber;

    @Column(length = 50)
    private String carrier;

    @Column(nullable = false, length = 30)
    private String status = "PROCESSING";

    @Temporal(TemporalType.DATE)
    private Date estimatedDelivery;

    @Temporal(TemporalType.TIMESTAMP)
    private Date shippedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(Date estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
    public Date getShippedAt() { return shippedAt; }
    public void setShippedAt(Date shippedAt) { this.shippedAt = shippedAt; }
    public Date getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Date deliveredAt) { this.deliveredAt = deliveredAt; }
    public Date getCreatedAt() { return createdAt; }
}
