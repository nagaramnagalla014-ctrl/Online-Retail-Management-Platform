import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Product, Category } from '../models/product.model';

@Injectable()
export class ProductService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getProducts(search?: string, categoryId?: number): Observable<any> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    if (categoryId) params = params.set('categoryId', String(categoryId));
    return this.http.get<any>(`${this.apiUrl}/products`, { params });
  }

  getProduct(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/products/${id}`);
  }

  getCategories(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/products/categories`);
  }

  // Admin
  createProduct(product: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/admin/products`, product, { withCredentials: true });
  }

  updateProduct(id: number, product: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/admin/products/${id}`, product, { withCredentials: true });
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/admin/products/${id}`, { withCredentials: true });
  }

  getLowStockProducts(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/products/low-stock`, { withCredentials: true });
  }
}
