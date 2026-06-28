import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-tracking',
  templateUrl: './order-tracking.component.html'
})
export class OrderTrackingComponent implements OnInit {
  trackingNumber = '';
  shipment: any = null;
  error = '';
  loading = false;

  constructor(private orderService: OrderService, private route: ActivatedRoute) {}

  ngOnInit() {
    const orderId = this.route.snapshot.queryParams['order'];
    if (orderId) {
      this.trackByOrderId(+orderId);
    }
  }

  private trackByOrderId(orderId: number) {
    this.loading = true;
    this.orderService.trackShipmentByOrder(orderId).subscribe(
      res => {
        this.loading = false;
        if (res.success) this.shipment = res.data;
        else this.error = 'No shipment found for this order.';
      },
      () => { this.loading = false; this.error = 'Shipment not found.'; }
    );
  }

  track() {
    if (!this.trackingNumber.trim()) return;
    this.loading = true;
    this.error = '';
    this.orderService.trackShipmentByTracking(this.trackingNumber).subscribe(
      res => {
        this.loading = false;
        if (res.success) this.shipment = res.data;
        else this.error = 'Tracking number not found.';
      },
      () => { this.loading = false; this.error = 'Tracking number not found.'; }
    );
  }
}
