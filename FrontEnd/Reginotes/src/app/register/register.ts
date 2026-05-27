import { Component } from '@angular/core';
import { RegisterService } from './register.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule,RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  username = '';
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
