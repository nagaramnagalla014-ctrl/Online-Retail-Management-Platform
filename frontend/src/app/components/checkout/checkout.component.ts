import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html'
})
export class CheckoutComponent implements OnInit {
  customer: Customer;
  cartTotal = 0;
  shippingCharge = 0;
  orderTotal = 0;

  model = {
    shippingAddress: '',
    shippingCity: '',
    shippingState: '',
    shippingPincode: '',
    promoCode: '',
    paymentMethod: 'CREDIT_CARD',
    notes: ''
  };

  error = '';
  loading = false;
  discount = 0;

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit() {
    this.customer = this.authService.currentUser;
    if (this.customer) {
      this.model.shippingAddress = this.customer.address || '';
      this.model.shippingCity = this.customer.city || '';
      this.model.shippingState = this.customer.state || '';
      this.model.shippingPincode = this.customer.pincode || '';
    }
    this.cartService.cart$.subscribe(cart => {
      this.cartTotal = this.cartService.cartTotal;
      this.shippingCharge = this.cartTotal >= 500 ? 0 : 49;
      this.orderTotal = this.cartTotal - this.discount + this.shippingCharge;
    });
  }

  placeOrder() {
    this.loading = true;
    this.error = '';
    this.orderService.placeOrder(this.model).subscribe(
      res => {
        this.loading = false;
        if (res.success) {
          this.router.navigate(['/orders']);
        } else {
          this.error = res.message;
        }
      },
      err => {
        this.loading = false;
        this.error = 'Order placement failed. Please try again.';
      }
    );
  }
}
