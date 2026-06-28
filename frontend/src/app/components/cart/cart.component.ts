import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { Cart } from '../../models/cart.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent implements OnInit {
  cart: Cart = null;
  loading = true;

  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit() {
    this.cartService.getCart().subscribe(res => {
      this.loading = false;
    });
    this.cartService.cart$.subscribe(cart => this.cart = cart);
  }

  get total(): number {
    return this.cartService.cartTotal;
  }

  updateQuantity(cartItemId: number, quantity: number) {
    this.cartService.updateQuantity(cartItemId, quantity).subscribe();
  }

  removeItem(cartItemId: number) {
    this.cartService.removeItem(cartItemId).subscribe();
  }

  checkout() {
    this.router.navigate(['/checkout']);
  }
}
