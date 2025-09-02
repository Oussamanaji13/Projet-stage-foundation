import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

interface Statistique {
  label: string;
  valeur: number;
  couleur: string;
  icone: string;
}

interface Notification {
  icone: string;
  couleur: string;
  titre: string;
  description: string;
  temps: string;
}
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
 statistiques: Statistique[] = [
    {
      label: 'Demandes actives',
      valeur: 3,
      couleur: 'text-primary-600 bg-primary-100',
      icone: 'fas fa-file-alt'
    },
    {
      label: 'Prestations utilisées',
      valeur: 7,
      couleur: 'text-blue-600 bg-blue-100',
      icone: 'fas fa-heart'
    },
    {
      label: 'Événements inscrits',
      valeur: 2,
      couleur: 'text-green-600 bg-green-100',
      icone: 'fas fa-calendar'
    },
    {
      label: 'Messages non lus',
      valeur: 3,
      couleur: 'text-orange-600 bg-orange-100',
      icone: 'fas fa-envelope'
    }
  ];

  demandes = [
    {
      titre: 'Aide au logement',
      date: '10 déc. 2024',
      statut: 'En cours',
      couleur: 'bg-yellow-50 border-yellow-400',
      badge: 'bg-yellow-100 text-yellow-800'
    },
    {
      titre: 'Bourse formation',
      date: '5 déc. 2024',
      statut: 'Approuvée',
      couleur: 'bg-green-50 border-green-400',
      badge: 'bg-green-100 text-green-800'
    }
  ];

  notifications: Notification[] = [
    {
      icone: 'fas fa-check',
      couleur: 'bg-primary-100 text-primary-600',
      titre: 'Demande approuvée',
      description: 'Votre demande de bourse formation a été approuvée',
      temps: 'Il y a 2 heures'
    },
    {
      icone: 'fas fa-calendar',
      couleur: 'bg-blue-100 text-blue-600',
      titre: 'Nouvel événement',
      description: 'Assemblée générale le 20 janvier 2025',
      temps: 'Il y a 1 jour'
    },
    {
      icone: 'fas fa-exclamation',
      couleur: 'bg-orange-100 text-orange-600',
      titre: 'Document manquant',
      description: 'Merci de compléter votre dossier d\'aide au logement',
      temps: 'Il y a 3 jours'
    }
  ];

  ngOnInit() {
    // Initialisation des données
  }
}
