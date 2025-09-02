import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Prestation {
  id: number;
  nom: string;
  categorie: string;
  description: string;
  montantMax: string;
  duree: string;
  conditions: string;
  couleur: string;
  icone: string;
}

@Component({
  selector: 'app-prestations',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './prestations.html',
  styleUrl: './prestations.scss'
})
export class Prestations implements OnInit {
prestations: Prestation[] = [
    {
      id: 1,
      nom: 'Aide au logement',
      categorie: 'Logement',
      description: 'Aide financière pour l\'accès au logement et les charges locatives pour les jeunes fonctionnaires.',
      montantMax: '800€/mois',
      duree: '24 mois',
      conditions: 'Âge < 30 ans',
      couleur: 'bg-blue-100',
      icone: 'fas fa-home text-blue-600'
    },
    {
      id: 2,
      nom: 'Bourse de formation',
      categorie: 'Formation',
      description: 'Financement des formations professionnelles et certifications pour développer vos compétences.',
      montantMax: '3 000€',
      duree: '12 mois',
      conditions: 'Ancienneté > 2 ans',
      couleur: 'bg-green-100',
      icone: 'fas fa-graduation-cap text-green-600'
    },
    {
      id: 3,
      nom: 'Aide à la garde d\'enfants',
      categorie: 'Famille',
      description: 'Participation aux frais de garde d\'enfants pour faciliter la conciliation vie professionnelle/familiale.',
      montantMax: '400€/mois',
      duree: 'Jusqu\'à 3 ans',
      conditions: 'Enfant < 6 ans',
      couleur: 'bg-purple-100',
      icone: 'fas fa-baby text-purple-600'
    },
    {
      id: 4,
      nom: 'Complémentaire santé',
      categorie: 'Santé',
      description: 'Mutuelle complémentaire à tarifs préférentiels pour vous et votre famille.',
      montantMax: 'Jusqu\'à 30%',
      duree: 'Famille entière',
      conditions: 'Adhérent actif',
      couleur: 'bg-red-100',
      icone: 'fas fa-heartbeat text-red-600'
    },
    {
      id: 5,
      nom: 'Chèques vacances',
      categorie: 'Loisirs',
      description: 'Chèques vacances à tarifs avantageux pour financer vos congés et loisirs.',
      montantMax: '2 000€/an',
      duree: '20% de remise',
      conditions: 'Selon revenus',
      couleur: 'bg-orange-100',
      icone: 'fas fa-umbrella-beach text-orange-600'
    },
    {
      id: 6,
      nom: 'Équipement informatique',
      categorie: 'Équipement',
      description: 'Aide à l\'achat d\'équipement informatique pour le télétravail et la formation.',
      montantMax: '1 500€',
      duree: 'Tous les 3 ans',
      conditions: 'Télétravail > 2j/sem',
      couleur: 'bg-indigo-100',
      icone: 'fas fa-laptop text-indigo-600'
    }
  ];

  prestationsFiltrees: Prestation[] = [];
  categories: string[] = ['Toutes', 'Logement', 'Formation', 'Famille', 'Santé', 'Loisirs', 'Équipement'];
  categorieSelectionnee = 'Toutes';

  ngOnInit() {
    this.prestationsFiltrees = [...this.prestations];
  }

  filtrerPrestations(categorie: string) {
    this.categorieSelectionnee = categorie;
    if (categorie === 'Toutes') {
      this.prestationsFiltrees = [...this.prestations];
    } else {
      this.prestationsFiltrees = this.prestations.filter(p => p.categorie === categorie);
    }
  }

  faireDemande(prestationId: number) {
    // Naviguer vers la page de demande avec l'ID de la prestation
    console.log('Faire une demande pour la prestation:', prestationId);
  }
}
