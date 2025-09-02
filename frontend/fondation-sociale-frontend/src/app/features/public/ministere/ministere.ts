import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Statistique {
  valeur: string;
  label: string;
}

interface Organisation {
  titre: string;
  description: string;
  icone: string;
  couleur: string;
}

interface Historique {
  annee: string;
  titre: string;
  description: string;
}

@Component({
  selector: 'app-ministere',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ministere.html',
  styleUrls: ['./ministere.scss']
})
export class MinistereComponent {
  statistiques: Statistique[] = [
    { valeur: '2.8B€', label: 'Investissements mobilisés' },
    { valeur: '147', label: 'Projets financés' },
    { valeur: '45K', label: 'Emplois créés' },
    { valeur: '89', label: 'Villes impactées' }
  ];

  organisation: Organisation[] = [
    {
      titre: 'Cabinet du Ministre',
      description: 'Direction stratégique et coordination des politiques d\'investissement',
      icone: 'fas fa-user-tie',
      couleur: 'bg-primary-100 text-primary-600'
    },
    {
      titre: 'Direction Générale',
      description: 'Pilotage opérationnel et mise en œuvre des programmes',
      icone: 'fas fa-cogs',
      couleur: 'bg-blue-100 text-blue-600'
    },
    {
      titre: 'Directions Régionales',
      description: 'Déploiement territorial et accompagnement local',
      icone: 'fas fa-map-marked-alt',
      couleur: 'bg-green-100 text-green-600'
    }
  ];

  historique: Historique[] = [
    {
      annee: '2020',
      titre: 'Création du Ministère',
      description: 'Établissement du Ministère de l\'Investissement pour coordonner les politiques d\'investissement public et privé.'
    },
    {
      annee: '2021',
      titre: 'Premiers grands projets',
      description: 'Lancement des premiers programmes d\'investissement dans les infrastructures et l\'innovation.'
    },
    {
      annee: '2023',
      titre: 'Expansion numérique',
      description: 'Déploiement du plan national de digitalisation et création de la Fondation Sociale.'
    }
  ];
}