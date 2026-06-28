import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  model = { firstName: '', lastName: '', email: '', password: '', phone: '' };
  error = '';
  success = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.loading = true;
    this.error = '';
    this.authService.register(this.model).subscribe(
      res => {
        this.loading = false;
        if (res.success) {
          this.success = 'Registration successful! Please login.';
          setTimeout(() => this.router.navigate(['/login']), 1500);
        } else {
          this.error = res.message;
        }
      },
      err => {
        this.loading = false;
        this.error = 'Registration failed. Please try again.';
      }
    );
  }
}
