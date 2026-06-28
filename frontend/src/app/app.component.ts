import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { CartService } from './services/cart.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  constructor(private authService: AuthService, private cartService: CartService) {}

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.cartService.loadCart();
    }
  }
}
