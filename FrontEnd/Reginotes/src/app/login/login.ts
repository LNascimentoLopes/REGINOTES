import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginService } from './login.services';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  
  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.loginService.login(this.email, this.password).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.Token);
        localStorage.setItem('refreshToken', response.refreshToken);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Login failed', err);
        this.errorMessage = 'Invalid email or password. Please try again.';
      }
    });
  }
}


