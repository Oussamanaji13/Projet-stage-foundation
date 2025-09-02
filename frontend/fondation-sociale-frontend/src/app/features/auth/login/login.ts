import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule,Router } from '@angular/router';
import { Auth } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,RouterModule,FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  credentials = {
    email: '',
    password: '',
    rememberMe: false
  };

  constructor(
    private auth: Auth,
    private router: Router
  ) {}

  onSubmit() {
    this.auth.login(this.credentials.email, this.credentials.password)
      .subscribe({
        next: (response) => {
          this.router.navigate(['/user/dashboard']);
        },
        error: (error) => {
          console.error('Erreur de connexion:', error);
        }
      });
  }
}
