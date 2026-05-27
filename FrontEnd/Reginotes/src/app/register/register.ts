import { Component } from '@angular/core';
import { RegisterService } from './register.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule,RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  username = '';
  private readonly API_URL = environment.apiUrl;
  email = '';
  password = '';
  confirmPassword = '';
  error = '';

  constructor(
    private registerService: RegisterService,
    private router: Router
  ) {}

  onSubmit(): void {
    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match.';
      return;
    }

    this.registerService.register(this.username, this.email, this.password).subscribe({
      next: (res) => {
        console.log('next disparou:', res);
        this.router.navigate(['/login']);
      },
      error: () => this.error = 'Registration failed. Try again.'
    });
  }
}
