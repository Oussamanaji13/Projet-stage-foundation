import { Component } from '@angular/core';
import { Auth } from '../../../core/services/auth';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
 user = {
    prenom: '',
    nom: '',
    email: '',
    matricule: '',
    service: '',
    telephone: '',
    password: '',
    confirmPassword: '',
    acceptConditions: false
  };

  services = [
    { value: 'cabinet', label: 'Cabinet du Ministre' },
    { value: 'dg', label: 'Direction Générale' },
    { value: 'finances', label: 'Direction des Finances' },
    { value: 'projets', label: 'Direction des Projets' },
    { value: 'rh', label: 'Ressources Humaines' }
  ];

  constructor(
    private auth: Auth,
    private router: Router
  ) {}

  onSubmit() {
    if (this.user.password !== this.user.confirmPassword) {
      alert('Les mots de passe ne correspondent pas');
      return;
    }

    this.auth.register(this.user)
      .subscribe({
        next: (response) => {
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Erreur d\'inscription:', error);
        }
      });
  }
}
