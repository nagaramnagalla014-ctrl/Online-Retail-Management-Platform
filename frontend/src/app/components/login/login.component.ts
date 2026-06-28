import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  error = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.loading = true;
    this.error = '';
    this.authService.login(this.email, this.password).subscribe(
      res => {
        this.loading = false;
        if (res.success) this.router.navigate(['/']);
        else this.error = res.message;
      },
      err => {
        this.loading = false;
        this.error = 'Invalid email or password';
      }
    );
  }
}
