import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin.service';
import { Order } from '../../../models/order.model';

@Component({
  selector: 'app-order-management',
  templateUrl: './order-management.component.html'
})
export class OrderManagementComponent implements OnInit {
  orders: Order[] = [];
  loading = true;
  successMsg = '';
  error = '';

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.loading = true;
    this.adminService.getAllOrders().subscribe(res => {
      if (res.success) this.orders = res.data;
      this.loading = false;
    });
  }

  updateStatus(orderId: number, status: string) {
    this.adminService.updateOrderStatus(orderId, status).subscribe(res => {
      if (res.success) {
        this.successMsg = 'Order status updated.';
        setTimeout(() => this.successMsg = '', 2000);
        this.loadOrders();
      }
    }, () => this.error = 'Update failed.');
  }

  ship(orderId: number) {
    this.adminService.shipOrder(orderId).subscribe(res => {
      if (res.success) {
        this.successMsg = 'Shipment created.';
        setTimeout(() => this.successMsg = '', 2000);
        this.loadOrders();
      }
    }, () => this.error = 'Failed to create shipment.');
  }

  getStatusClass(status: string): string {
    const map = {
      'PENDING': 'warning', 'CONFIRMED': 'info', 'PROCESSING': 'primary',
      'SHIPPED': 'success', 'DELIVERED': 'success', 'CANCELLED': 'danger'
    };
    return 'badge badge-' + (map[status] || 'secondary');
  }
}
