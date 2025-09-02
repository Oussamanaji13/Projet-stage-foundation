import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

interface Actualite {
  id: number;
  titre: string;
  date: string;
  categorie: string;
  couleur: string;
  description: string;
}

interface Evenement {
  id: number;
  titre: string;
  date: string;
  heure: string;
  description: string;
  icone: string;
  couleur: string;
}

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './accueil.html',
  styleUrl: './accueil.scss'
})
export class Accueil {
 actualitesRecentes: Actualite[] = [
    {
      id: 1,
      titre: 'Nouvelle prestation logement',
      date: '15 Décembre 2024',
      categorie: 'Prestation',
      couleur: 'from-primary-400 to-primary-600',
      description: 'Découvrez notre nouvelle aide au logement pour les jeunes fonctionnaires...'
    },
    {
      id: 2,
      titre: 'Assemblée générale 2025',
      date: '12 Décembre 2024',
      categorie: 'Événement',
      couleur: 'from-blue-400 to-blue-600',
      description: 'Participez à notre assemblée générale annuelle le 20 janvier 2025...'
    },
    {
      id: 3,
      titre: 'Vacances d\'hiver',
      date: '8 Décembre 2024',
      categorie: 'Actualité',
      couleur: 'from-purple-400 to-purple-600',
      description: 'Profitez de nos offres spéciales pour les vacances d\'hiver...'
    }
  ];

  evenementsAvenir: Evenement[] = [
    {
      id: 1,
      titre: 'Assemblée Générale',
      date: '20 Janvier 2025',
      heure: '14h00',
      description: 'Venez participer à notre assemblée générale annuelle et découvrir les projets 2025.',
      icone: 'fas fa-calendar',
      couleur: 'bg-primary-600'
    },
    {
      id: 2,
      titre: 'Forum Emploi',
      date: '15 Février 2025',
      heure: '9h00',
      description: 'Rencontrez des employeurs et découvrez de nouvelles opportunités de carrière.',
      icone: 'fas fa-users',
      couleur: 'bg-blue-600'
    }
  ];

  ngOnInit() {
    // Initialisation si nécessaire
  }
}
