import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { Product, Category } from '../../../models/product.model';

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html'
})
export class ProductManagementComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  loading = true;
  showForm = false;
  editingId: number = null;
  successMsg = '';
  error = '';

  model: any = {
    name: '', sku: '', description: '', price: 0, stockQuantity: 0,
    brand: '', imageUrl: '', isActive: true, category: { categoryId: null }
  };

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.loadProducts();
    this.productService.getCategories().subscribe(res => {
      if (res.success) this.categories = res.data;
    });
  }

  loadProducts() {
    this.loading = true;
    this.productService.getProducts().subscribe(res => {
      if (res.success) this.products = res.data;
      this.loading = false;
    });
  }

  resetForm() {
    this.model = {
      name: '', sku: '', description: '', price: 0, stockQuantity: 0,
      brand: '', imageUrl: '', isActive: true, category: { categoryId: null }
    };
    this.editingId = null;
  }

  openCreate() {
    this.resetForm();
    this.showForm = true;
  }

  editProduct(product: Product) {
    this.model = {
      name: product.name, sku: product.sku, description: product.description,
      price: product.price, stockQuantity: product.stockQuantity, brand: product.brand,
      imageUrl: product.imageUrl, isActive: product.isActive,
      category: { categoryId: product.category ? product.category.categoryId : null }
    };
    this.editingId = product.productId;
    this.showForm = true;
  }

  save() {
    this.error = '';
    const obs = this.editingId
      ? this.productService.updateProduct(this.editingId, this.model)
      : this.productService.createProduct(this.model);

    obs.subscribe(res => {
      if (res.success) {
        this.successMsg = this.editingId ? 'Product updated.' : 'Product created.';
        setTimeout(() => this.successMsg = '', 2000);
        this.showForm = false;
        this.loadProducts();
      } else {
        this.error = res.message;
      }
    }, err => this.error = 'Operation failed.');
  }

  delete(id: number) {
    if (!confirm('Deactivate this product?')) return;
    this.productService.deleteProduct(id).subscribe(() => this.loadProducts());
  }
}
