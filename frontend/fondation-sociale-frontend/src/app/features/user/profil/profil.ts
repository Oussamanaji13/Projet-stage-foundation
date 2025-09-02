import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Auth } from '../../../core/services/auth';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './profil.html',
  styleUrls: ['./profil.scss']
})
export class ProfilComponent implements OnInit {
getServiceLabel() {
throw new Error('Method not implemented.');
}
  profil = {
    prenom: 'Jean',
    nom: 'Dupont',
    email: 'jean.dupont@ministere.gouv.fr',
    telephone: '01 23 45 67 89',
    matricule: 'MIN2024001234',
    service: 'dg',
    adresse: '123 Rue de la Paix\n75001 Paris, France',
    dateNaissance: '1985-06-15',
    situationFamiliale: 'marie',
    nombreEnfants: 2,
    notificationsEmail: true,
    actualitesNewsletter: true,
    invitationsEvenements: false
  };

  services = [
    { value: 'dg', label: 'Direction Générale' },
    { value: 'cabinet', label: 'Cabinet du Ministre' },
    { value: 'finances', label: 'Direction des Finances' },
    { value: 'projets', label: 'Direction des Projets' },
    { value: 'rh', label: 'Ressources Humaines' }
  ];

  situationsFamiliales = [
    { value: 'marie', label: 'Marié(e)' },
    { value: 'celibataire', label: 'Célibataire' },
    { value: 'divorce', label: 'Divorcé(e)' },
    { value: 'veuf', label: 'Veuf/Veuve' }
  ];

  constructor(private auth: Auth) {}

  ngOnInit() {
    // Charger le profil de l'utilisateur connecté
    const user = this.auth.getCurrentUser();
    if (user) {
      this.profil.prenom = user.prenom;
      this.profil.nom = user.nom;
      this.profil.email = user.email;
      this.profil.matricule = user.matricule;
    }
  }

  onSubmit() {
    console.log('Profil mis à jour:', this.profil);
    alert('Profil mis à jour avec succès !');
  }

  changerPhoto() {
    console.log('Changement de photo demandé');
    // Logique pour changer la photo de profil
  }

  get initiales(): string {
    return `${this.profil.prenom[0]}${this.profil.nom[0]}`;
  }
}
