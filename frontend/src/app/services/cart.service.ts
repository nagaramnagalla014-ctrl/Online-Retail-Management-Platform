import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Cart } from '../models/cart.model';

@Injectable()
export class CartService {
  private cartSubject = new BehaviorSubject<Cart>(null);
  cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadCart(): void {
    this.http.get<any>(`${environment.apiUrl}/cart`, { withCredentials: true })
      .subscribe(res => {
        if (res.success) this.cartSubject.next(res.data);
      }, () => {});
  }

  getCart(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/cart`, { withCredentials: true })
      .pipe(tap(res => { if (res.success) this.cartSubject.next(res.data); }));
  }

  addItem(productId: number, quantity: number = 1): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/cart/add`,
      { productId, quantity }, { withCredentials: true })
      .pipe(tap(res => { if (res.success) this.cartSubject.next(res.data); }));
  }

  updateQuantity(cartItemId: number, quantity: number): Observable<any> {
    const params = new HttpParams().set('quantity', String(quantity));
    return this.http.put<any>(`${environment.apiUrl}/cart/item/${cartItemId}`,
      null, { params, withCredentials: true })
      .pipe(tap(res => { if (res.success) this.cartSubject.next(res.data); }));
  }

  removeItem(cartItemId: number): Observable<any> {
    return this.http.delete<any>(`${environment.apiUrl}/cart/item/${cartItemId}`, { withCredentials: true })
      .pipe(tap(res => { if (res.success) this.cartSubject.next(res.data); }));
  }

  clearCart(): Observable<any> {
    return this.http.delete<any>(`${environment.apiUrl}/cart`, { withCredentials: true })
      .pipe(tap(() => this.cartSubject.next(null)));
  }

  get itemCount(): number {
    const cart = this.cartSubject.value;
    if (!cart || !cart.items) return 0;
    return cart.items.reduce((sum, item) => sum + item.quantity, 0);
  }

  get cartTotal(): number {
    const cart = this.cartSubject.value;
    if (!cart || !cart.items) return 0;
    return cart.items.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
  }
}
