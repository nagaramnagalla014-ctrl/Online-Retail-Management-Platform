import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class OrderService {
  constructor(private http: HttpClient) {}

  placeOrder(orderData: any): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/orders`, orderData, { withCredentials: true });
  }

  getMyOrders(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/orders`, { withCredentials: true });
  }

  getOrderByNumber(orderNumber: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/orders/${orderNumber}`, { withCredentials: true });
  }

  trackShipmentByOrder(orderId: number): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/shipments/order/${orderId}`, { withCredentials: true });
  }

  trackShipmentByTracking(trackingNumber: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/shipments/track/${trackingNumber}`);
  }
}
