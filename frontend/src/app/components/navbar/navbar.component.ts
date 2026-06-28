import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {
  currentUser: Customer;
  cartCount = 0;
  searchTerm = '';

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => this.currentUser = user);
    this.cartService.cart$.subscribe(() => this.cartCount = this.cartService.itemCount);
  }

  search() {
    if (this.searchTerm.trim()) {
      this.router.navigate(['/products'], { queryParams: { search: this.searchTerm } });
    }
  }

  logout() {
    this.authService.logout().subscribe(() => this.router.navigate(['/']));
  }
}
