import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html'
})
export class OrderHistoryComponent implements OnInit {
  orders: Order[] = [];
  loading = true;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.orderService.getMyOrders().subscribe(res => {
      if (res.success) this.orders = res.data;
      this.loading = false;
    });
  }

  getStatusClass(status: string): string {
    const map = {
      'PENDING': 'warning', 'CONFIRMED': 'info', 'PROCESSING': 'primary',
      'SHIPPED': 'success', 'DELIVERED': 'success', 'CANCELLED': 'danger'
    };
    return 'badge badge-' + (map[status] || 'secondary');
  }
}
