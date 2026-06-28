import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product, Category } from '../../models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  selectedCategory: number = null;
  searchTerm = '';
  loading = true;
  addingId: number = null;
  successMsg = '';

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.productService.getCategories().subscribe(res => {
      if (res.success) this.categories = res.data;
    });
    this.route.queryParams.subscribe(params => {
      this.searchTerm = params['search'] || '';
      this.selectedCategory = params['categoryId'] ? +params['categoryId'] : null;
      this.loadProducts();
    });
  }

  loadProducts() {
    this.loading = true;
    this.productService.getProducts(this.searchTerm, this.selectedCategory).subscribe(res => {
      if (res.success) this.products = res.data;
      this.loading = false;
    });
  }

  filterByCategory(categoryId: number) {
    this.selectedCategory = categoryId;
    this.router.navigate([], { queryParams: { categoryId }, queryParamsHandling: 'merge' });
  }

  clearFilter() {
    this.selectedCategory = null;
    this.router.navigate([], { queryParams: {} });
  }

  addToCart(productId: number) {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.addingId = productId;
    this.cartService.addItem(productId, 1).subscribe(res => {
      this.addingId = null;
      if (res.success) {
        this.successMsg = 'Added to cart!';
        setTimeout(() => this.successMsg = '', 2000);
      }
    }, () => this.addingId = null);
  }
}
