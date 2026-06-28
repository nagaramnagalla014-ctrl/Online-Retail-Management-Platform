import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Customer } from '../models/customer.model';

@Injectable()
export class AuthService {
  private currentUserSubject = new BehaviorSubject<Customer>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadCurrentUser();
  }

  private loadCurrentUser() {
    this.http.get<any>(`${environment.apiUrl}/auth/me`, { withCredentials: true })
      .subscribe(res => {
        if (res.success) this.currentUserSubject.next(res.data);
      }, () => {});
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, { email, password }, { withCredentials: true })
      .pipe(tap(res => {
        if (res.success) this.currentUserSubject.next(res.data);
      }));
  }

  register(data: any): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/register`, data, { withCredentials: true });
  }

  logout(): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/logout`, {}, { withCredentials: true })
      .pipe(tap(() => this.currentUserSubject.next(null)));
  }

  updateProfile(data: any): Observable<any> {
    return this.http.put<any>(`${environment.apiUrl}/auth/me`, data, { withCredentials: true })
      .pipe(tap(res => {
        if (res.success) this.currentUserSubject.next(res.data);
      }));
  }

  get currentUser(): Customer {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }
}
