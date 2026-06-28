import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html'
})
export class ProductDetailComponent implements OnInit {
  product: Product = null;
  quantity = 1;
  loading = true;
  adding = false;
  successMsg = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.productService.getProduct(id).subscribe(res => {
      if (res.success) this.product = res.data;
      this.loading = false;
    });
  }

  addToCart() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.adding = true;
    this.cartService.addItem(this.product.productId, this.quantity).subscribe(res => {
      this.adding = false;
      if (res.success) {
        this.successMsg = 'Added to cart!';
        setTimeout(() => this.successMsg = '', 2000);
      }
    }, () => this.adding = false);
  }
}
