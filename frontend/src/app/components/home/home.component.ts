import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product, Category } from '../../models/product.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  featuredProducts: Product[] = [];
  categories: Category[] = [];
  loading = true;

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.getProducts().subscribe(res => {
      if (res.success) this.featuredProducts = res.data.slice(0, 8);
      this.loading = false;
    });
    this.productService.getCategories().subscribe(res => {
      if (res.success) this.categories = res.data;
    });
  }
}
