import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

interface StatistiqueAdmin {
  titre: string;
  valeur: number;
  evolution: string;
  tendance: 'hausse' | 'baisse' | 'stable';
  icone: string;
  couleur: string;
}

interface ActiviteRecente {
  id: number;
  type: string;
  description: string;
  utilisateur: string;
  temps: string;
  icone: string;
  couleur: string;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  statistiques: StatistiqueAdmin[] = [
    {
      titre: 'Total Adhérents',
      valeur: 2847,
      evolution: '+12%',
      tendance: 'hausse',
      icone: 'fas fa-users',
      couleur: 'text-blue-600 bg-blue-100'
    },
    {
      titre: 'Demandes En Cours',
      valeur: 156,
      evolution: '+8%',
      tendance: 'hausse',
      icone: 'fas fa-file-alt',
      couleur: 'text-orange-600 bg-orange-100'
    },
    {
      titre: 'Prestations Actives',
      valeur: 423,
      evolution: '+15%',
      tendance: 'hausse',
      icone: 'fas fa-heart',
      couleur: 'text-green-600 bg-green-100'
    },
    {
      titre: 'Budget Utilisé',
      valeur: 87,
      evolution: '+5%',
      tendance: 'hausse',
      icone: 'fas fa-euro-sign',
      couleur: 'text-purple-600 bg-purple-100'
    }
  ];

  activitesRecentes: ActiviteRecente[] = [
    {
      id: 1,
      type: 'Nouvelle demande',
      description: 'Demande d\'aide au logement déposée',
      utilisateur: 'Marie Dubois',
      temps: 'Il y a 5 min',
      icone: 'fas fa-plus-circle',
      couleur: 'text-green-600'
    },
    {
      id: 2,
      type: 'Adhérent inscrit',
      description: 'Nouveau fonctionnaire inscrit',
      utilisateur: 'Pierre Martin',
      temps: 'Il y a 15 min',
      icone: 'fas fa-user-plus',
      couleur: 'text-blue-600'
    },
    {
      id: 3,
      type: 'Demande approuvée',
      description: 'Bourse formation validée',
      utilisateur: 'Julie Leroy',
      temps: 'Il y a 30 min',
      icone: 'fas fa-check-circle',
      couleur: 'text-green-600'
    },
    {
      id: 4,
      type: 'Événement créé',
      description: 'Assemblée générale programmée',
      utilisateur: 'Admin System',
      temps: 'Il y a 1h',
      icone: 'fas fa-calendar-plus',
      couleur: 'text-purple-600'
    }
  ];

  demandesEnAttente = [
    { nom: 'Jean Dupont', prestation: 'Aide logement', date: '15 déc.', priorite: 'haute' },
    { nom: 'Marie Martin', prestation: 'Formation', date: '14 déc.', priorite: 'moyenne' },
    { nom: 'Paul Durand', prestation: 'Garde enfants', date: '13 déc.', priorite: 'basse' }
  ];

  ngOnInit() {
    // Initialisation des données
  }

  getPrioriteClass(priorite: string): string {
    switch (priorite) {
      case 'haute': return 'bg-red-100 text-red-800';
      case 'moyenne': return 'bg-yellow-100 text-yellow-800';
      case 'basse': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getTendanceIcon(tendance: string): string {
    switch (tendance) {
      case 'hausse': return 'fas fa-arrow-up text-green-500';
      case 'baisse': return 'fas fa-arrow-down text-red-500';
      default: return 'fas fa-minus text-gray-500';
    }
  }
}
