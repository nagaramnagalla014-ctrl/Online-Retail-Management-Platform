import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class AdminService {
  private adminUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  getDashboard(): Observable<any> {
    return this.http.get<any>(`${this.adminUrl}/dashboard`, { withCredentials: true });
  }

  getAllOrders(): Observable<any> {
    return this.http.get<any>(`${this.adminUrl}/orders`, { withCredentials: true });
  }

  updateOrderStatus(orderId: number, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.put<any>(`${this.adminUrl}/orders/${orderId}/status`, null,
      { params, withCredentials: true });
  }

  shipOrder(orderId: number): Observable<any> {
    return this.http.post<any>(`${this.adminUrl}/orders/${orderId}/ship`, {}, { withCredentials: true });
  }

  getAllPromotions(): Observable<any> {
    return this.http.get<any>(`${this.adminUrl}/promotions`, { withCredentials: true });
  }

  createPromotion(promo: any): Observable<any> {
    return this.http.post<any>(`${this.adminUrl}/promotions`, promo, { withCredentials: true });
  }

  deletePromotion(id: number): Observable<any> {
    return this.http.delete<any>(`${this.adminUrl}/promotions/${id}`, { withCredentials: true });
  }
}
