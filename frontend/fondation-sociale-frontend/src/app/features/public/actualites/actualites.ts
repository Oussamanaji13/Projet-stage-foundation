import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

interface Article {
  id: number;
  titre: string;
  date: string;
  categorie: string;
  couleur: string;
  description: string;
  imageGradient: string;
}

@Component({
  selector: 'app-actualites',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './actualites.html',
  styleUrls: ['./actualites.scss']
})
export class ActualitesComponent implements OnInit {
  articles: Article[] = [
    {
      id: 1,
      titre: 'Nouvelle aide au logement',
      date: '15 Déc 2024',
      categorie: 'Prestation',
      couleur: 'bg-primary-100 text-primary-600',
      description: 'Une nouvelle prestation pour accompagner les jeunes fonctionnaires dans leur recherche de logement...',
      imageGradient: 'from-primary-400 to-primary-600'
    },
    {
      id: 2,
      titre: 'Assemblée Générale 2025',
      date: '20 Jan 2025',
      categorie: 'Événement',
      couleur: 'bg-blue-100 text-blue-600',
      description: 'Participez à notre assemblée générale annuelle et découvrez nos projets pour l\'année 2025...',
      imageGradient: 'from-blue-400 to-blue-600'
    },
    {
      id: 3,
      titre: 'Vacances d\'hiver 2025',
      date: '8 Déc 2024',
      categorie: 'Actualité',
      couleur: 'bg-purple-100 text-purple-600',
      description: 'Découvrez nos offres spéciales pour les vacances d\'hiver et profitez de tarifs préférentiels...',
      imageGradient: 'from-purple-400 to-purple-600'
    },
    {
      id: 4,
      titre: 'Aide à la formation',
      date: '5 Déc 2024',
      categorie: 'Prestation',
      couleur: 'bg-green-100 text-green-600',
      description: 'Nouveau dispositif d\'aide à la formation professionnelle pour tous nos adhérents...',
      imageGradient: 'from-green-400 to-green-600'
    },
    {
      id: 5,
      titre: 'Forum Emploi',
      date: '15 Fév 2025',
      categorie: 'Événement',
      couleur: 'bg-orange-100 text-orange-600',
      description: 'Rencontrez des employeurs et découvrez de nouvelles opportunités de carrière...',
      imageGradient: 'from-orange-400 to-orange-600'
    },
    {
      id: 6,
      titre: 'Bilan annuel 2024',
      date: '1 Déc 2024',
      categorie: 'Actualité',
      couleur: 'bg-red-100 text-red-600',
      description: 'Découvrez le bilan de nos actions et réalisations pour l\'année 2024...',
      imageGradient: 'from-red-400 to-red-600'
    }
  ];

  articlesFiltres: Article[] = [];
  filtreActif = 'Tous';
  categories = ['Tous', 'Actualités', 'Événements', 'Prestations'];

  ngOnInit() {
    this.articlesFiltres = [...this.articles];
  }

  filtrerArticles(categorie: string) {
    this.filtreActif = categorie;
    if (categorie === 'Tous') {
      this.articlesFiltres = [...this.articles];
    } else {
      this.articlesFiltres = this.articles.filter(article => 
        article.categorie.toLowerCase() === categorie.toLowerCase().slice(0, -1)
      );
    }
  }
}
